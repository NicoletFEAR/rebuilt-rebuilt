package frc.robot.subsystems.drive;

import static frc.lib.units.AngleUnit.Rotations;
import static frc.lib.units.AngularVelocityUnit.RadiansPerSecond;
import static frc.lib.units.DistanceUnit.Inches;
import static frc.lib.units.DistanceUnit.Meters;
import static frc.lib.units.TimeUnit.Seconds;
import static frc.lib.units.VelocityUnit.MetersPerSecond;

import frc.lib.constants.Constants;
import frc.lib.estimate.OdometryMeasurement;
import frc.lib.motor.FeedforwardValues;
import frc.lib.statemachine.State;
import frc.lib.statemachine.StateMachine;
import frc.lib.units.Angle;
import frc.lib.units.AngularVelocity;
import frc.lib.units.Distance;
import frc.lib.units.Velocity;
import frc.robot.RobotState;
import frc.robot.subsystems.drive.SwerveModule.SwerveModuleState;
import org.littletonrobotics.junction.Logger;
import org.wpilib.driverstation.Alliance;
import org.wpilib.driverstation.MatchState;
import org.wpilib.math.estimator.SwerveDrivePoseEstimator;
import org.wpilib.math.geometry.Pose2d;
import org.wpilib.math.geometry.Rotation2d;
import org.wpilib.math.geometry.Translation2d;
import org.wpilib.math.kinematics.ChassisVelocities;
import org.wpilib.math.kinematics.SwerveDriveKinematics;
import org.wpilib.math.kinematics.SwerveModulePosition;
import org.wpilib.math.kinematics.SwerveModuleVelocity;
import org.wpilib.math.linalg.VecBuilder;
import org.wpilib.system.Timer;

/**
 * Swerve drive subsystem.
 *
 * <p>Owns the four swerve modules and a pose estimator. The pose estimator is initialized with a
 * heading of 0 for the blue alliance and pi for the red alliance. Note that the gyro reading passed
 * to the estimator is hardcoded to {@link Rotation2d#ZERO}; no gyroscope is wired up yet.
 *
 * <p>Module order in the {@link #modules} array is front-left, front-right, back-left, back-right.
 */
public class Drive extends StateMachine<Drive.DriveState> {
    private final RobotState robotState;
    private final SwerveModule[] modules;

    private final SwerveDriveKinematics kinematics;
    private final SwerveDrivePoseEstimator poseEstimator;

    /**
     * Creates the drive subsystem.
     *
     * @param robotState shared robot state that receives odometry measurements
     * @param frontLeft front-left swerve module
     * @param frontRight front-right swerve module
     * @param backLeft back-left swerve module
     * @param backRight back-right swerve module
     */
    public Drive(
            RobotState robotState,
            SwerveModule frontLeft,
            SwerveModule frontRight,
            SwerveModule backLeft,
            SwerveModule backRight) {
        super("Drive", DriveState.OFF);
        this.robotState = robotState;
        modules = new SwerveModule[4];
        modules[0] = frontLeft;
        modules[1] = frontRight;
        modules[2] = backLeft;
        modules[3] = backRight;

        Distance halfTrackWidthX = DriveConstants.TRACK_WIDTH_X.div(2.0);
        Distance halfTrackWidthY = DriveConstants.TRACK_WIDTH_Y.div(2.0);
        kinematics =
                new SwerveDriveKinematics(
                        new Translation2d[] {
                            new Translation2d(halfTrackWidthX.in(Meters), halfTrackWidthY.in(Meters)),
                            new Translation2d(halfTrackWidthX.in(Meters), halfTrackWidthY.negative().in(Meters)),
                            new Translation2d(halfTrackWidthX.negative().in(Meters), halfTrackWidthY.in(Meters)),
                            new Translation2d(
                                    halfTrackWidthX.negative().in(Meters), halfTrackWidthY.negative().in(Meters)),
                        });
        poseEstimator =
                new SwerveDrivePoseEstimator(
                        kinematics,
                        MatchState.getAlliance().orElse(Alliance.BLUE) == Alliance.BLUE
                                ? Rotation2d.fromRadians(0.0)
                                : Rotation2d.fromRadians(Math.PI),
                        getModulePositions(),
                        new Pose2d(),
                        // These values mean we trust the gyroscope and wheel odometry much more
                        // than the camera, since the standard deviations for the camera measurements
                        // are much higher.
                        VecBuilder.fill(0.1, 0.1, 0.0),
                        VecBuilder.fill(0.9, 0.9, 9999999));
    }

    /** States of the drive subsystem. */
    public static sealed interface DriveState extends State permits DriveState.Off, DriveState.Teleop {
        /** Drive motors are off. */
        static record Off() implements DriveState {}

        /** Drive teleop using the given driver input parameters. */
        static record Teleop(DriveParameters parameters) implements DriveState {}

        /** Shared constant for the {@link Off} record. */
        static final Off OFF = new Off();
    }

    /**
     * Logs module positions and velocities, updates the pose estimator, applies the current drive
     * state, runs each module's periodic logic, and logs the drive state.
     *
     * <p>In {@link DriveState.Teleop}, the driver input is scaled by the maximum drive velocity and
     * maximum angular velocity, discretized to this loop period, converted to module setpoints using
     * swerve kinematics, and desaturated so no wheel exceeds the maximum velocity.
     */
    public void periodic() {
        Logger.recordOutput("Drive/ModulePositions", getModulePositions());
        Logger.recordOutput("Drive/ModuleStates", getModuleStates());
        poseEstimator.updateWithTime(Timer.getTimestamp(), Rotation2d.ZERO, getModulePositions());
        robotState.addOdometryMeasurement(getOdometryMeasurement());

        switch (state) {
            case DriveState.Off() -> {
                for (int i = 0; i < 4; i++) {
                    modules[i].setState(SwerveModuleState.OFF);
                }
            }

            case DriveState.Teleop(DriveParameters parameters) -> {
                Translation2d linearVelocity =
                        new Translation2d(parameters.x(), parameters.y())
                                .times(DriveConstants.MAX_VELOCITY.in(MetersPerSecond));
                double omega =
                        parameters.omega() * DriveConstants.MAX_ANGULAR_VELOCITY.in(RadiansPerSecond);
                ChassisVelocities velocities =
                        new ChassisVelocities(linearVelocity.getX(), linearVelocity.getY(), omega);
                velocities = velocities.discretize(Constants.LOOP_PERIOD.reciprocal().in(Seconds));
                SwerveModuleVelocity[] setpointStates = kinematics.toSwerveModuleVelocities(velocities);
                setpointStates =
                        SwerveDriveKinematics.desaturateWheelVelocities(
                                setpointStates, DriveConstants.MAX_VELOCITY.in(MetersPerSecond));
                Logger.recordOutput("Drive/DesiredModuleStates", setpointStates);

                for (int i = 0; i < 4; i++) {
                    modules[i].setState(new SwerveModuleState.Drive(setpointStates[i]));
                }
            }
        }

        for (int i = 0; i < 4; i++) {
            modules[i].periodic();
        }

        logState();
    }

    /**
     * Wraps the pose estimator's current estimate and current timestamp in an {@link
     * OdometryMeasurement}.
     */
    private OdometryMeasurement getOdometryMeasurement() {
        return new OdometryMeasurement(
                Seconds.timestamp(Timer.getTimestamp()), poseEstimator.getEstimatedPosition());
    }

    /** Returns the current position of each module, in the same order as {@link #modules}. */
    private SwerveModulePosition[] getModulePositions() {
        SwerveModulePosition[] result = new SwerveModulePosition[4];

        for (int i = 0; i < 4; i++) {
            result[i] = modules[i].getPosition();
        }

        return result;
    }

    /** Returns the current state of each module, in the same order as {@link #modules}. */
    private SwerveModuleVelocity[] getModuleStates() {
        SwerveModuleVelocity[] result = new SwerveModuleVelocity[4];

        for (int i = 0; i < 4; i++) {
            result[i] = modules[i].getState();
        }

        return result;
    }

    /** Constants for the drive base (gear ratios, encoder offsets, gains, and limits). */
    public static final class DriveConstants {
        /** Radius of the drive wheels. */
        private static final Distance WHEEL_RADIUS = Inches.of(2.0);

        /** Rotor-to-mechanism ratio for the drive, in rotor radians per meter of wheel travel. */
        public static final double DRIVE_ROTOR_TO_MECHANISM_RATIO =
                425.0 / 63.0 / WHEEL_RADIUS.in(Meters);

        /** Rotor-to-mechanism ratio for the turn, in motor rotations per mechanism rotation. */
        public static final double TURN_ROTOR_TO_MECHANISM_RATIO = 150.0 / 7.0;

        /** Absolute encoder offset for the front-left turn mechanism, in rotations. */
        public static final Angle FRONT_LEFT_OFFSET = Rotations.of(-0.8896484375);

        /** Absolute encoder offset for the front-right turn mechanism, in rotations. */
        public static final Angle FRONT_RIGHT_OFFSET = Rotations.of(-0.09716796875);

        /** Absolute encoder offset for the back-left turn mechanism, in rotations. */
        public static final Angle BACK_LEFT_OFFSET = Rotations.of(-0.721923828125);

        /** Absolute encoder offset for the back-right turn mechanism, in rotations. */
        public static final Angle BACK_RIGHT_OFFSET = Rotations.of(-0.37841796875);

        /** Feedforward and feedback gains for the drive motors. */
        public static final FeedforwardValues DRIVE_FEEDFORWARD_VALUES =
                new FeedforwardValues(2.0, 0.0, 0.0, 0.0, 0.0, 0.0);

        /** Feedforward and feedback gains for the turn motors. */
        public static final FeedforwardValues TURN_FEEDFORWARD_VALUES =
                new FeedforwardValues(0.03, 0.0, 0.0, 0.0, 0.0, 0.0);

        /** Longitudinal (front-to-back) distance between the front and back module centers. */
        private static final Distance TRACK_WIDTH_X = Inches.of(20.753888);

        /** Lateral (left-to-right) distance between the left and right module centers. */
        private static final Distance TRACK_WIDTH_Y = Inches.of(20.753888);

        /** Maximum translational speed of the drive base. */
        private static final Velocity MAX_VELOCITY = MetersPerSecond.of(4.8);

        /** Maximum angular velocity of the drive base. */
        private static final AngularVelocity MAX_ANGULAR_VELOCITY = RadiansPerSecond.of(Math.PI * 3.0);

        private DriveConstants() {
            /* Keep this constructor empty */
        }
    }
}

package frc.robot.subsystems.drivebase;

import static frc.lib.units.AngularVelocityUnit.RadiansPerSecond;
import static frc.lib.units.DistanceUnit.Inches;
import static frc.lib.units.DistanceUnit.Meters;
import static frc.lib.units.TimeUnit.Seconds;
import static frc.lib.units.VelocityUnit.MetersPerSecond;

import frc.lib.estimate.OdometryMeasurement;
import frc.lib.statemachine.State;
import frc.lib.statemachine.StateMachine;
import frc.lib.units.AngularVelocity;
import frc.lib.units.Distance;
import frc.lib.units.Velocity;
import frc.robot.ExecutionConstants;
import frc.robot.RobotState;
import frc.robot.subsystems.drivebase.SwerveModule.SwerveModuleState;
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
public final class Drivebase extends StateMachine<Drivebase.DrivebaseState> {
    private final RobotState robotState;
    private final SwerveModule[] modules = new SwerveModule[4];

    private final SwerveDriveKinematics kinematics;
    private final SwerveDrivePoseEstimator poseEstimator;

    private SwerveModuleVelocity[] desiredVelocities = new SwerveModuleVelocity[4];

    /**
     * Creates the drive subsystem.
     *
     * @param robotState shared robot state that receives odometry measurements
     */
    public Drivebase(RobotState robotState) {
        super("Drive", DrivebaseState.OFF);
        this.robotState = robotState;
        modules[0] = new SwerveModule(SwerveModuleName.FRONT_LEFT);
        modules[1] = new SwerveModule(SwerveModuleName.FRONT_RIGHT);
        modules[2] = new SwerveModule(SwerveModuleName.BACK_LEFT);
        modules[3] = new SwerveModule(SwerveModuleName.BACK_RIGHT);

        Distance halfTrackWidthX = DrivebaseConstants.TRACK_WIDTH_X.div(2.0);
        Distance halfTrackWidthY = DrivebaseConstants.TRACK_WIDTH_Y.div(2.0);
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
    public static sealed interface DrivebaseState extends State
            permits DrivebaseState.Off, DrivebaseState.Drive {
        /** Drive motors are off. */
        static record Off() implements DrivebaseState {}

        /** Drive teleop using the given driver input parameters. */
        static record Drive(DriveParameters parameters) implements DrivebaseState {}

        /** Shared constant for the {@link Off} record to avoid unnecessary allocations. */
        static final Off OFF = new Off();
    }

    /** Updates the inputs from all swerve modules, updates the pose estimator, and updates the robot state. */
    public void updateInputs() {
        for (int i = 0; i < 4; i++) {
            modules[i].updateInputs();
        }

        poseEstimator.updateWithTime(Timer.getTimestamp(), Rotation2d.ZERO, getModulePositions());
        robotState.addOdometryMeasurement(getOdometryMeasurement());
    }

    /**
     * Chooses the states for the swerve modules.
     *
     * <p>In {@link DrivebaseState.Drive}, the driver input is scaled by the maximum drive velocity
     * and maximum angular velocity, discretized to this loop period, converted to module setpoints
     * using swerve kinematics, and desaturated so no wheel exceeds the maximum velocity.
     */
    public void applyState() {
        switch (state) {
            case DrivebaseState.Off() -> {
                for (int i = 0; i < 4; i++) {
                    modules[i].setState(SwerveModuleState.OFF);
                }
            }

            case DrivebaseState.Drive(DriveParameters parameters) -> {
                Translation2d linearVelocity =
                        new Translation2d(parameters.x(), parameters.y())
                                .times(DrivebaseConstants.MAX_VELOCITY.in(MetersPerSecond));
                double omega =
                        parameters.omega() * DrivebaseConstants.MAX_ANGULAR_VELOCITY.in(RadiansPerSecond);
                ChassisVelocities velocities =
                        new ChassisVelocities(linearVelocity.getX(), linearVelocity.getY(), omega);
                velocities = velocities.discretize(ExecutionConstants.LOOP_PERIOD.reciprocal().in(Seconds));
                desiredVelocities = kinematics.toSwerveModuleVelocities(velocities);
                desiredVelocities =
                        SwerveDriveKinematics.desaturateWheelVelocities(
                                desiredVelocities, DrivebaseConstants.MAX_VELOCITY.in(MetersPerSecond));

                for (int i = 0; i < 4; i++) {
                    modules[i].setState(new SwerveModuleState.Drive(desiredVelocities[i]));
                }
            }
        }

        for (int i = 0; i < 4; i++) {
            modules[i].applyState();
        }
    }

    /** Runs the swerve modules. */
    public void execute() {
        for (int i = 0; i < 4; i++) {
            modules[i].execute();
        }
    }

    /** Logs any desired outputs from the drivebase. */
    public void logOutputs() {
        logState();
        Logger.recordOutput(name + "/Module Positions", getModulePositions());
        Logger.recordOutput(name + "/Module Velocities", getModuleVelocities());
        Logger.recordOutput(name + "/Desired Module Velocities", desiredVelocities);

        for (int i = 0; i < 4; i++) {
            modules[i].logOutputs();
        }
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
    private SwerveModuleVelocity[] getModuleVelocities() {
        SwerveModuleVelocity[] result = new SwerveModuleVelocity[4];

        for (int i = 0; i < 4; i++) {
            result[i] = modules[i].getState();
        }

        return result;
    }

    /** Constants for the drive base (dimensions and limits). */
    static final class DrivebaseConstants {
        /** Radius of the drive wheels. */
        static final Distance WHEEL_RADIUS = Inches.of(2.0);

        /** Longitudinal (front-to-back) distance between the front and back module centers. */
        private static final Distance TRACK_WIDTH_X = Inches.of(20.753888);

        /** Lateral (left-to-right) distance between the left and right module centers. */
        private static final Distance TRACK_WIDTH_Y = Inches.of(20.753888);

        /** Maximum translational speed of the drive base. */
        private static final Velocity MAX_VELOCITY = MetersPerSecond.of(4.8);

        /** Maximum angular velocity of the drive base. */
        private static final AngularVelocity MAX_ANGULAR_VELOCITY = RadiansPerSecond.of(Math.PI * 3.0);

        private DrivebaseConstants() {
            /* Keep this constructor empty. */
        }
    }
}

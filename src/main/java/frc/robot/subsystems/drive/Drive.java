package frc.robot.subsystems.drive;

import static org.wpilib.units.Units.Inches;
import static org.wpilib.units.Units.Meters;
import static org.wpilib.units.Units.MetersPerSecond;
import static org.wpilib.units.Units.RadiansPerSecond;
import static org.wpilib.units.Units.Rotations;
import static org.wpilib.units.Units.Seconds;

import frc.lib.constants.Constants;
import frc.lib.estimate.OdometryMeasurement;
import frc.lib.motor.FeedforwardValues;
import frc.robot.RobotState;
import frc.robot.subsystems.drive.SwerveModule.SwerveModuleState;
import org.littletonrobotics.junction.Logger;
import org.wpilib.driverstation.Alliance;
import org.wpilib.driverstation.MatchState;
import org.wpilib.math.estimator.SwerveDrivePoseEstimator;
import org.wpilib.math.geometry.Pose2d;
import org.wpilib.math.geometry.Rotation2d;
import org.wpilib.math.geometry.Transform2d;
import org.wpilib.math.geometry.Translation2d;
import org.wpilib.math.kinematics.ChassisVelocities;
import org.wpilib.math.kinematics.SwerveDriveKinematics;
import org.wpilib.math.kinematics.SwerveModulePosition;
import org.wpilib.math.kinematics.SwerveModuleVelocity;
import org.wpilib.math.linalg.VecBuilder;
import org.wpilib.system.Timer;
import org.wpilib.units.measure.Angle;
import org.wpilib.units.measure.AngularVelocity;
import org.wpilib.units.measure.Distance;
import org.wpilib.units.measure.LinearVelocity;

public class Drive {
    private final RobotState robotState;
    private final SwerveModule[] modules;

    private final SwerveDriveKinematics kinematics;
    private final SwerveDrivePoseEstimator poseEstimator;

    private DriveState state;

    public Drive(
            RobotState robotState,
            SwerveModule frontLeft,
            SwerveModule frontRight,
            SwerveModule backLeft,
            SwerveModule backRight) {
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
                            new Translation2d(halfTrackWidthX, halfTrackWidthY),
                            new Translation2d(halfTrackWidthX, halfTrackWidthY.unaryMinus()),
                            new Translation2d(halfTrackWidthX.unaryMinus(), halfTrackWidthY),
                            new Translation2d(halfTrackWidthX.unaryMinus(), halfTrackWidthY.unaryMinus()),
                        });
        poseEstimator =
                new SwerveDrivePoseEstimator(
                        kinematics,
                        MatchState.getAlliance().orElse(Alliance.BLUE) == Alliance.BLUE
                                ? Rotation2d.fromRadians(0.0)
                                : Rotation2d.fromRadians(Math.PI),
                        getModulePositions(),
                        new Pose2d(),
                        VecBuilder.fill(0.1, 0.1, 0.0),
                        VecBuilder.fill(0.9, 0.9, 9999999));

        state = DriveState.OFF;
    }

    public enum DriveState {
        OFF,
        DRIVE,
    }

    public void periodic() {
        Logger.recordOutput("Drive/State", state);
        Logger.recordOutput("Drive/ModulePositions", getModulePositions());
        Logger.recordOutput("Drive/ModuleStates", getModuleStates());
        Logger.recordOutput("Drive/DesiredModuleStates", getDesiredModuleStates());
        poseEstimator.updateWithTime(Timer.getTimestamp(), Rotation2d.ZERO, getModulePositions());
        robotState.addOdometryMeasurement(getOdometryMeasurement());

        switch (state) {
            case OFF -> {
                for (int i = 0; i < 4; i++) {
                    modules[i].setState(SwerveModuleState.OFF);
                }
            }

            case DRIVE -> {
                for (int i = 0; i < 4; i++) {
                    modules[i].setState(SwerveModuleState.DRIVE);
                }
            }
        }

        for (int i = 0; i < 4; i++) {
            modules[i].periodic();
        }
    }

    public void setState(DriveState state) {
        this.state = state;
    }

    public void applySpeedsFromControls(double x, double y, double omega) {
        Translation2d linearVelocity =
                new Pose2d(Translation2d.ZERO, new Rotation2d(x, y))
                        .transformBy(new Transform2d(Math.hypot(x, y), 0.0, Rotation2d.ZERO))
                        .getTranslation()
                        .times(DriveConstants.MAX_VELOCITY.in(MetersPerSecond));
        omega *= DriveConstants.MAX_ANGULAR_VELOCITY.in(RadiansPerSecond);
        applySpeeds(new ChassisVelocities(linearVelocity.getX(), linearVelocity.getY(), omega));
    }

    private void applySpeeds(ChassisVelocities speeds) {
        ChassisVelocities discreteSpeeds =
                speeds.discretize(Constants.LOOP_PERIOD.asPeriod().in(Seconds));
        SwerveModuleVelocity[] setpointStates = kinematics.toSwerveModuleVelocities(discreteSpeeds);
        setpointStates =
                SwerveDriveKinematics.desaturateWheelVelocities(
                        setpointStates, DriveConstants.MAX_VELOCITY);
        applyStates(setpointStates);
    }

    private void applyStates(SwerveModuleVelocity[] states) {
        for (int i = 0; i < 4; i++) {
            modules[i].applyState(states[i]);
        }
    }

    private OdometryMeasurement getOdometryMeasurement() {
        return new OdometryMeasurement(
                Seconds.of(Timer.getTimestamp()), poseEstimator.getEstimatedPosition());
    }

    private SwerveModulePosition[] getModulePositions() {
        SwerveModulePosition[] result = new SwerveModulePosition[4];

        for (int i = 0; i < 4; i++) {
            result[i] = modules[i].getPosition();
        }

        return result;
    }

    private SwerveModuleVelocity[] getModuleStates() {
        SwerveModuleVelocity[] result = new SwerveModuleVelocity[4];

        for (int i = 0; i < 4; i++) {
            result[i] = modules[i].getState();
        }

        return result;
    }

    private SwerveModuleVelocity[] getDesiredModuleStates() {
        SwerveModuleVelocity[] result = new SwerveModuleVelocity[4];

        for (int i = 0; i < 4; i++) {
            result[i] = modules[i].getDesiredState();
        }

        return result;
    }

    public static final class DriveConstants {
        private static final Distance WHEEL_RADIUS = Meters.of(0.0508);

        public static final double DRIVE_ROTOR_TO_MECHANISM_RATIO =
                425.0 / 63.0 * WHEEL_RADIUS.in(Meters);
        public static final double TURN_ROTOR_TO_MECHANISM_RATIO = 150.0 / 7.0;

        public static final Angle FRONT_LEFT_OFFSET = Rotations.of(-0.8896484375);
        public static final Angle FRONT_RIGHT_OFFSET = Rotations.of(-0.09716796875);
        public static final Angle BACK_LEFT_OFFSET = Rotations.of(-0.721923828125);
        public static final Angle BACK_RIGHT_OFFSET = Rotations.of(-0.37841796875);

        public static final FeedforwardValues DRIVE_FEEDFORWARD_VALUES =
                new FeedforwardValues(2.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        public static final FeedforwardValues TURN_FEEDFORWARD_VALUES =
                new FeedforwardValues(0.03, 0.0, 0.0, 0.0, 0.0, 0.0);

        private static final Distance TRACK_WIDTH_X = Inches.of(20.753888);
        private static final Distance TRACK_WIDTH_Y = Inches.of(20.753888);

        private static final LinearVelocity MAX_VELOCITY = MetersPerSecond.of(4.8);
        private static final AngularVelocity MAX_ANGULAR_VELOCITY = RadiansPerSecond.of(Math.PI * 3.0);

        private DriveConstants() {
            /* Keep this contructor empty */
        }
    }
}

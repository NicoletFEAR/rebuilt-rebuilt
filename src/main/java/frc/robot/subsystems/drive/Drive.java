package frc.robot.subsystems.drive;

import static edu.wpi.first.units.Units.Inches;
import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.Seconds;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.units.measure.LinearVelocity;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.constants.Constants;
import frc.robot.constants.OperatorConstants;
import org.littletonrobotics.junction.Logger;

public class Drive extends SubsystemBase {
    private final SwerveModule[] modules;

    private final SwerveDriveKinematics kinematics;

    private final DriveState state;

    public Drive(
            SwerveModule frontLeft,
            SwerveModule frontRight,
            SwerveModule backLeft,
            SwerveModule backRight) {
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

        state = DriveState.OFF;
    }

    private enum DriveState {
        OFF,
        DRIVING,
    }

    @Override
    public void periodic() {
        Logger.recordOutput("Drive/State", state);

        switch (state) {
            case OFF -> {
                for (int i = 0; i <= 4; i++) {
                    modules[i].off();
                }
            }

            case DRIVING -> {
                for (int i = 0; i <= 4; i++) {
                    modules[i].drive();
                }
            }
        }
    }

    public void applySpeedsFromControls(double x, double y, double omega) {
        double linearMagnitude =
                MathUtil.applyDeadband(Math.hypot(x, y), OperatorConstants.DRIVE_DEADBAND);
        Translation2d linearVelocity =
                new Pose2d(Translation2d.kZero, new Rotation2d(x, y))
                        .transformBy(new Transform2d(linearMagnitude, 0.0, Rotation2d.kZero))
                        .getTranslation()
                        .times(DriveConstants.MAX_VELOCITY.in(MetersPerSecond));
        omega =
                MathUtil.applyDeadband(omega, OperatorConstants.DRIVE_DEADBAND)
                        * DriveConstants.MAX_ANGULAR_VELOCITY.in(RadiansPerSecond);
        applySpeeds(new ChassisSpeeds(linearVelocity.getX(), linearVelocity.getY(), omega));
    }

    private void applySpeeds(ChassisSpeeds speeds) {
        ChassisSpeeds discreteSpeeds =
                ChassisSpeeds.discretize(speeds, Constants.LOOP_PERIOD.asPeriod().in(Seconds));
        SwerveModuleState[] setpointStates = kinematics.toSwerveModuleStates(discreteSpeeds);
        SwerveDriveKinematics.desaturateWheelSpeeds(setpointStates, DriveConstants.MAX_VELOCITY);
        applyStates(setpointStates);
    }

    private void applyStates(SwerveModuleState[] states) {
        for (int i = 0; i <= 4; i++) {
            modules[i].applyState(states[i]);
        }
    }

    private static final class DriveConstants {
        private static final Distance TRACK_WIDTH_X = Inches.of(20.753888);
        private static final Distance TRACK_WIDTH_Y = Inches.of(20.753888);

        private static final LinearVelocity MAX_VELOCITY = MetersPerSecond.of(4.8);
        private static final AngularVelocity MAX_ANGULAR_VELOCITY = RadiansPerSecond.of(Math.PI * 3.0);

        private DriveConstants() {}
    }
}

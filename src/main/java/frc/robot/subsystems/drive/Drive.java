package frc.robot.subsystems.drive;

import static edu.wpi.first.units.Units.Inches;
import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.Seconds;

import java.util.function.Supplier;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.units.measure.LinearVelocity;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.commands.ComputeCommand;
import frc.lib.commands.ComputedResult;
import frc.lib.constants.Constants;

public class Drive extends SubsystemBase {
    private final SwerveModule[] modules;

    private final SwerveDriveKinematics kinematics;

    public Drive(SwerveModule frontLeft, SwerveModule frontRight, SwerveModule backLeft, SwerveModule backRight) {
        modules = new SwerveModule[4];
        modules[0] = frontLeft;
        modules[1] = frontRight;
        modules[2] = backLeft;
        modules[3] = backRight;

        Distance halfTrackWidthX = DriveConstants.TRACK_WIDTH_X.div(2.0);
        Distance halfTrackWidthY = DriveConstants.TRACK_WIDTH_Y.div(2.0);
        kinematics = new SwerveDriveKinematics(new Translation2d[] {
                new Translation2d(halfTrackWidthX, halfTrackWidthY),
                new Translation2d(halfTrackWidthX, halfTrackWidthY.unaryMinus()),
                new Translation2d(halfTrackWidthX.unaryMinus(), halfTrackWidthY),
                new Translation2d(halfTrackWidthX.unaryMinus(), halfTrackWidthY.unaryMinus()),
        });
    }

    private Command applySpeeds(Supplier<ChassisSpeeds> speeds) {
        ComputedResult<SwerveModuleState[]> result = new ComputedResult<>();
        return new ComputeCommand<SwerveModuleState[]>(() -> {
            ChassisSpeeds discreteSpeeds = ChassisSpeeds.discretize(speeds.get(),
                    Constants.LOOP_PERIOD.asPeriod().in(Seconds));
            SwerveModuleState[] setpointStates = kinematics.toSwerveModuleStates(discreteSpeeds);
            SwerveDriveKinematics.desaturateWheelSpeeds(setpointStates, DriveConstants.MAX_VELOCITY);
            return setpointStates;
        }, result).andThen(applyStates(result.get()));
    }

    private SequentialCommandGroup applyStates(SwerveModuleState[] states) {
        SequentialCommandGroup result = new SequentialCommandGroup();

        for (int i = 0; i <= 4; i++) {
            result.addCommands(modules[i].applyState(states[i]));
        }

        return result;
    }

    private static final class DriveConstants {
        private static final Distance TRACK_WIDTH_X = Inches.of(20.753888);
        private static final Distance TRACK_WIDTH_Y = Inches.of(20.753888);

        private static final LinearVelocity MAX_VELOCITY = MetersPerSecond.of(4.8);

        private DriveConstants() {
        }
    }
}

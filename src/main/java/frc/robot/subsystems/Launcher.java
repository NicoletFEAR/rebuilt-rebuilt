package frc.robot.subsystems;

import static edu.wpi.first.units.Units.Radians;
import static edu.wpi.first.units.Units.RadiansPerSecond;

import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.cancoder.CanCoderIO;
import frc.lib.motor.MotorIO;

public class Launcher extends SubsystemBase {
    private final Flywheels flywheels;
    private final Indexer indexer;
    private final Hood hood;

    public Launcher(MotorIO leftFlywheelMotor, MotorIO rightFlywheelMotor, MotorIO indexMotor, MotorIO hoodMotor,
            CanCoderIO hoodCanCoder) {
        flywheels = new Flywheels(leftFlywheelMotor, new MotorIO[] { rightFlywheelMotor });
        indexer = new Indexer(indexMotor);
        hood = new Hood(hoodMotor, hoodCanCoder);
    }

    public Command off() {
        return flywheels.off().alongWith(indexer.off()).alongWith(hood.off());
    }

    public Command launch() {
        return flywheels.launch(LauncherConstants.FLYWHEEL_LAUNCHING_SPEED)
                .alongWith(hood.runToPosition(LauncherConstants.HOOD_MEDIUM_POSITION)).andThen(indexer.index());
    }

    public static final class LauncherConstants {
        private static final AngularVelocity FLYWHEEL_LAUNCHING_SPEED = RadiansPerSecond.of(Math.PI * 200.0);

        private static final Angle HOOD_MEDIUM_POSITION = Radians.of(Math.PI / 6.0);

        private LauncherConstants() {
        }
    }
}

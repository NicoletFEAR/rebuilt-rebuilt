package frc.robot.subsystems;

import static edu.wpi.first.units.Units.Volts;

import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.wpilibj2.command.Command;
import frc.lib.module.MultiAngularVelocityModule;
import frc.lib.motor.MotorIO;

public class Flywheels extends MultiAngularVelocityModule {
    public Flywheels(MotorIO motor, MotorIO[] followers) {
        super("Launcher/Flywheels", motor, followers, FlywheelConstants.ROTOR_TO_MECHANISM_RATIO);
    }

    public Command off() {
        return super.setVoltage(Volts.of(0.0));
    }

    public Command launch(AngularVelocity velocity) {
        return super.runToVelocity(velocity);
    }

    public static final class FlywheelConstants {
        private static final double ROTOR_TO_MECHANISM_RATIO = 0.8;

        private FlywheelConstants() {
        }
    }
}

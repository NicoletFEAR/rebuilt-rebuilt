package frc.robot.subsystems.launcher;

import static edu.wpi.first.units.Units.Volts;

import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.wpilibj2.command.Command;
import frc.lib.module.MultiAngularVelocityModule;
import frc.lib.motor.MotorIO;

class Flywheels extends MultiAngularVelocityModule {
    Flywheels(MotorIO motor, MotorIO[] followers) {
        super("Launcher/Flywheels", motor, followers, FlywheelConstants.ROTOR_TO_MECHANISM_RATIO);
    }

    Command off() {
        return super.setVoltage(Volts.of(0.0));
    }

    Command launch(AngularVelocity velocity) {
        return super.runToVelocity(velocity);
    }

    private static final class FlywheelConstants {
        private static final double ROTOR_TO_MECHANISM_RATIO = 0.8;

        private FlywheelConstants() {
        }
    }
}

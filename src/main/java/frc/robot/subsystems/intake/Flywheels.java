package frc.robot.subsystems.intake;

import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.Volts;

import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.wpilibj2.command.Command;
import frc.lib.module.AngularVelocityModule;
import frc.lib.motor.MotorIO;

class Flywheels extends AngularVelocityModule {
    Flywheels(MotorIO motor) {
        super("Intake/Flywheels", motor, FlywheelConstants.ROTOR_TO_MECHANISM_RATIO);
    }

    Command off() {
        return super.setVoltage(Volts.of(0.0));
    }

    Command jostle() {
        return super.setVelocitySetpoint(FlywheelConstants.JOSTLE_VELOCITY);
    }

    Command intake() {
        return super.setVelocitySetpoint(FlywheelConstants.INTAKE_VELOCITY);
    }

    private static final class FlywheelConstants {
        private static final double ROTOR_TO_MECHANISM_RATIO = 0.6;

        private static final AngularVelocity JOSTLE_VELOCITY = RadiansPerSecond.of(Math.PI * 15.0);
        private static final AngularVelocity INTAKE_VELOCITY = RadiansPerSecond.of(Math.PI * 100.0);

        private FlywheelConstants() {
        }
    }
}

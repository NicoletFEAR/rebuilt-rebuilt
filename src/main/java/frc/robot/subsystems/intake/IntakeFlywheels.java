package frc.robot.subsystems.intake;

import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.Volts;

import edu.wpi.first.units.measure.AngularVelocity;
import frc.lib.module.velocity.AngularVelocityModule;
import frc.lib.motor.MotorIO;
import org.littletonrobotics.junction.Logger;

class IntakeFlywheels extends AngularVelocityModule {
    private FlywheelState state;

    IntakeFlywheels(MotorIO motor) {
        super("Intake/Flywheels", motor, FlywheelConstants.ROTOR_TO_MECHANISM_RATIO);
        state = FlywheelState.OFF;
    }

    private enum FlywheelState {
        OFF,
        JOSTLING,
        INTAKING,
    }

    @Override
    public void periodic() {
        super.periodic();
        Logger.recordOutput(name + "/State", state);

        switch (state) {
            case OFF -> super.setVoltage(Volts.of(0.0));
            case JOSTLING -> super.setVelocitySetpoint(FlywheelConstants.JOSTLE_VELOCITY);
            case INTAKING -> super.setVelocitySetpoint(FlywheelConstants.INTAKE_VELOCITY);
        }
    }

    void off() {
        state = FlywheelState.OFF;
    }

    void jostle() {
        state = FlywheelState.JOSTLING;
    }

    void intake() {
        state = FlywheelState.INTAKING;
    }

    private static final class FlywheelConstants {
        private static final double ROTOR_TO_MECHANISM_RATIO = 0.6;

        private static final AngularVelocity JOSTLE_VELOCITY = RadiansPerSecond.of(Math.PI * 15.0);
        private static final AngularVelocity INTAKE_VELOCITY = RadiansPerSecond.of(Math.PI * 100.0);

        private FlywheelConstants() {
        }
    }
}

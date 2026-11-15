package frc.robot.subsystems.intake;

import static frc.lib.units.AngularVelocityUnit.RadiansPerSecond;
import static frc.lib.units.VoltageUnit.Volts;

import frc.lib.module.velocity.AngularVelocityModule;
import frc.lib.motor.FeedforwardValues;
import frc.lib.motor.MotorIO;
import frc.lib.statemachine.State;
import frc.lib.statemachine.StateMachine;
import frc.lib.units.AngularVelocity;

public class Wheels extends StateMachine<Wheels.WheelState> {
    private AngularVelocityModule module;
    
    Wheels(MotorIO motor) {
        super("Intake/Wheels", WheelState.OFF);
        module = new AngularVelocityModule(name, motor, WheelConstants.ROTOR_TO_MECHANISM_RATIO);
    }

    enum WheelState implements State {
        OFF,
        JOSTLE,
        INTAKE,
        EXTAKE,
    }

    public void periodic() {
        module.updateInputs();

        switch (state) {
            case OFF -> module.setVoltage(Volts.of(0.0));
            case JOSTLE -> module.setVelocitySetpoint(WheelConstants.JOSTLE_VELOCITY);
            case INTAKE -> module.setVelocitySetpoint(WheelConstants.INTAKE_VELOCITY);
            case EXTAKE -> module.setVelocitySetpoint(WheelConstants.EXTAKE_VELOCITY);
        }

        logState();
    }

    public static final class WheelConstants {
        public static final FeedforwardValues FEEDFORWARD_VALUES =
                new FeedforwardValues(1.0, 0.0, 0.0, 0.0, 0.0, 0.0);

        private static final double ROTOR_TO_MECHANISM_RATIO = 0.6;

        private static final AngularVelocity JOSTLE_VELOCITY = RadiansPerSecond.of(Math.PI * 15.0);
        private static final AngularVelocity INTAKE_VELOCITY = RadiansPerSecond.of(Math.PI * 100.0);
        private static final AngularVelocity EXTAKE_VELOCITY = RadiansPerSecond.of(Math.PI * -100.0);

        private WheelConstants() {
            /* Keep this constructor empty */
        }
    }
}

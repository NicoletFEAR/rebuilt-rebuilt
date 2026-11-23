package frc.robot.subsystems.intake;

import static frc.lib.units.AngularVelocityUnit.RadiansPerSecond;
import static frc.lib.units.VoltageUnit.Volts;

import frc.lib.module.velocity.AngularVelocityModule;
import frc.lib.motor.FeedforwardValues;
import frc.lib.motor.MotorConfig;
import frc.lib.motor.MotorIO;
import frc.lib.statemachine.State;
import frc.lib.statemachine.StateMachine;
import frc.lib.units.AngularVelocity;
import frc.robot.subsystems.DeviceIds;

/**
 * Intake wheels.
 *
 * <p>The wheels are driven by an {@link AngularVelocityModule}; each state maps to either zero
 * voltage or a velocity setpoint defined in {@link WheelConstants}.
 */
final class Wheels extends StateMachine<Wheels.WheelState> {
    private final AngularVelocityModule module;

    /** Creates the wheels. */
    Wheels() {
        super("Intake/Wheels", WheelState.OFF);
        module =
                new AngularVelocityModule(
                        name,
                        MotorIO.buildMotor(
                                new MotorConfig(
                                        DeviceIds.INTAKE_WHEELS,
                                        WheelConstants.ROTOR_TO_MECHANISM_RATIO,
                                        WheelConstants.FEEDFORWARD_VALUES)));
    }

    /** States of the wheels. */
    enum WheelState implements State {
        OFF,
        JOSTLE,
        INTAKE,
        EXTAKE,
    }

    /** Updates the motor inputs. */
    void updateInputs() {
        module.updateInputs();
    }

    /** Applies the behavior for the current state. */
    void execute() {
        switch (state) {
            case OFF -> module.setVoltage(Volts.of(0.0));
            case JOSTLE -> module.setVelocitySetpoint(WheelConstants.JOSTLE_VELOCITY);
            case INTAKE -> module.setVelocitySetpoint(WheelConstants.INTAKE_VELOCITY);
            case EXTAKE -> module.setVelocitySetpoint(WheelConstants.EXTAKE_VELOCITY);
        }
    }

    /** Logs any desired outputs from the wheels. */
    void logOutputs() {
        logState();
    }

    /** Constants for the intake wheels. */
    private static final class WheelConstants {

        /** Feedforward and feedback gains for the wheel motor. */
        private static final FeedforwardValues FEEDFORWARD_VALUES =
                new FeedforwardValues(0.1, 0.0, 0.0, 0.1, 0.1, 0.1);

        /** Sensor-to-mechanism ratio for the wheels. */
        private static final double ROTOR_TO_MECHANISM_RATIO = 0.6;

        /** Velocity commanded in the {@link WheelState#JOSTLE} state. */
        private static final AngularVelocity JOSTLE_VELOCITY = RadiansPerSecond.of(Math.PI * 15.0);

        /** Velocity commanded in the {@link WheelState#INTAKE} state. */
        private static final AngularVelocity INTAKE_VELOCITY = RadiansPerSecond.of(Math.PI * 100.0);

        /** Velocity commanded in the {@link WheelState#EXTAKE} state. */
        private static final AngularVelocity EXTAKE_VELOCITY = RadiansPerSecond.of(Math.PI * -100.0);

        private WheelConstants() {
            /* Keep this constructor empty */
        }
    }
}

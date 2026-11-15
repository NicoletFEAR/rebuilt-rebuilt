package frc.robot.subsystems.launcher;

import static frc.lib.units.AngularVelocityUnit.RadiansPerSecond;
import static frc.lib.units.VoltageUnit.Volts;

import frc.lib.module.velocity.AngularVelocityModule;
import frc.lib.motor.FeedforwardValues;
import frc.lib.motor.MotorIO;
import frc.lib.statemachine.State;
import frc.lib.statemachine.StateMachine;
import frc.lib.units.AngularVelocity;

/**
 * Launcher indexer that feeds game pieces into the flywheels.
 *
 * <p>The indexer is driven by an {@link AngularVelocityModule}; each state maps to either zero
 * voltage or a velocity setpoint defined in {@link IndexerConstants}.
 */
public class Indexer extends StateMachine<Indexer.IndexerState> {
    private AngularVelocityModule module;

    /**
     * Creates the indexer.
     *
     * @param motor interface used to communicate with the motor
     */
    Indexer(MotorIO motor) {
        super("Launcher/Indexer", IndexerState.OFF);
        module = new AngularVelocityModule(name, motor, IndexerConstants.ROTOR_TO_MECHANISM_RATIO);
    }

    /** States of the indexer. */
    enum IndexerState implements State {
        OFF,
        INDEX,
        EXTAKE,
    }

    /** Updates the motor inputs, applies the behavior for the current state, and logs the state. */
    public void periodic() {
        module.updateInputs();

        switch (state) {
            case OFF -> module.setVoltage(Volts.of(0.0));
            case INDEX -> module.setVelocitySetpoint(IndexerConstants.INDEX_VELOCITY);
            case EXTAKE -> module.setVelocitySetpoint(IndexerConstants.EXTAKE_VELOCITY);
        }

        logState();
    }

    /** Constants for the indexer. */
    public static final class IndexerConstants {

        /** Feedforward and feedback gains for the indexer motor. */
        public static final FeedforwardValues FEEDFORWARD_VALUES =
                new FeedforwardValues(1.0, 0.0, 0.0, 0.0, 0.0, 0.0);

        /** Rotor-to-mechanism ratio for the indexer. */
        private static final double ROTOR_TO_MECHANISM_RATIO = 1.0;

        /** Velocity commanded in the {@link IndexerState#INDEX} state. */
        private static final AngularVelocity INDEX_VELOCITY = RadiansPerSecond.of(Math.PI * 100.0);

        /** Velocity commanded in the {@link IndexerState#EXTAKE} state. */
        private static final AngularVelocity EXTAKE_VELOCITY = RadiansPerSecond.of(Math.PI * -100.0);

        private IndexerConstants() {
            /* Keep this constructor empty */
        }
    }
}

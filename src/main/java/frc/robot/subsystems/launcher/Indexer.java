package frc.robot.subsystems.launcher;

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
 * Launcher indexer that feeds game pieces into the flywheels.
 *
 * <p>The indexer is driven by an {@link AngularVelocityModule}; each state maps to either zero
 * voltage or a velocity setpoint defined in {@link IndexerConstants}.
 */
final class Indexer extends StateMachine<Indexer.IndexerState> {
    private final AngularVelocityModule module;

    /** Creates the indexer. */
    Indexer() {
        super("Launcher/Indexer", IndexerState.OFF);
        module =
                new AngularVelocityModule(
                        name,
                        MotorIO.buildMotor(
                                new MotorConfig(
                                        DeviceIds.INDEXER,
                                        IndexerConstants.SENSOR_TO_MECHANISM_RATIO,
                                        IndexerConstants.FEEDFORWARD_VALUES)));
    }

    /** States of the indexer. */
    enum IndexerState implements State {
        OFF,
        INDEX,
        EXTAKE,
    }

    /** Updates the motor inputs, applies the behavior for the current state, and logs the state. */
    void periodic() {
        module.updateInputs();

        switch (state) {
            case OFF -> module.setVoltage(Volts.of(0.0));
            case INDEX -> module.setVelocitySetpoint(IndexerConstants.INDEX_VELOCITY);
            case EXTAKE -> module.setVelocitySetpoint(IndexerConstants.EXTAKE_VELOCITY);
        }

        logState();
    }

    /** Constants for the indexer. */
    private static final class IndexerConstants {

        /** Feedforward and feedback gains for the indexer motor. */
        private static final FeedforwardValues FEEDFORWARD_VALUES =
                new FeedforwardValues(1.0, 0.0, 0.0, 0.0, 0.0, 0.0);

        /** Sensor-to-mechanism ratio for the indexer. */
        private static final double SENSOR_TO_MECHANISM_RATIO = 1.0;

        /** Velocity commanded in the {@link IndexerState#INDEX} state. */
        private static final AngularVelocity INDEX_VELOCITY = RadiansPerSecond.of(Math.PI * 100.0);

        /** Velocity commanded in the {@link IndexerState#EXTAKE} state. */
        private static final AngularVelocity EXTAKE_VELOCITY = RadiansPerSecond.of(Math.PI * -100.0);

        private IndexerConstants() {
            /* Keep this constructor empty */
        }
    }
}

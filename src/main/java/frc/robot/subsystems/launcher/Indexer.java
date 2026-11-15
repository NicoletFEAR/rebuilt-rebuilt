package frc.robot.subsystems.launcher;

import static frc.lib.units.AngularVelocityUnit.RadiansPerSecond;
import static frc.lib.units.VoltageUnit.Volts;

import frc.lib.module.velocity.AngularVelocityModule;
import frc.lib.motor.FeedforwardValues;
import frc.lib.motor.MotorIO;
import frc.lib.statemachine.State;
import frc.lib.statemachine.StateMachine;
import frc.lib.units.AngularVelocity;

public class Indexer extends StateMachine<Indexer.IndexerState> {
    private AngularVelocityModule module;

    Indexer(MotorIO motor) {
        super("Launcher/Indexer", IndexerState.OFF);
        module = new AngularVelocityModule("Launcher/Indexer", motor, IndexerConstants.ROTOR_TO_MECHANISM_RATIO);
    }

    enum IndexerState implements State {
        OFF,
        INDEX,
        EXTAKE,
    }

    public void periodic() {
        module.updateInputs();

        switch (state) {
            case OFF -> module.setVoltage(Volts.of(0.0));
            case INDEX -> module.setVelocitySetpoint(IndexerConstants.INDEX_VELOCITY);
            case EXTAKE -> module.setVelocitySetpoint(IndexerConstants.EXTAKE_VELOCITY);
        }

        logState();
    }

    public static final class IndexerConstants {
        public static final FeedforwardValues FEEDFORWARD_VALUES =
                new FeedforwardValues(1.0, 0.0, 0.0, 0.0, 0.0, 0.0);

        private static final double ROTOR_TO_MECHANISM_RATIO = 1.0;
        private static final AngularVelocity INDEX_VELOCITY = RadiansPerSecond.of(Math.PI * 100.0);
        private static final AngularVelocity EXTAKE_VELOCITY = RadiansPerSecond.of(Math.PI * -100.0);

        private IndexerConstants() {
            /* Keep this constructor empty */
        }
    }
}

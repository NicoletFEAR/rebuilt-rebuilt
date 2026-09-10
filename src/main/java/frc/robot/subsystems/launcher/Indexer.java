package frc.robot.subsystems.launcher;

import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.Volts;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.units.measure.AngularVelocity;
import frc.lib.module.AngularVelocityModule;
import frc.lib.motor.FeedforwardValues;
import frc.lib.motor.MotorIO;

public class Indexer extends AngularVelocityModule {
    private IndexerState state;

    Indexer(MotorIO motor) {
        super("Launcher/Indexer", motor, IndexerConstants.ROTOR_TO_MECHANISM_RATIO);
        state = IndexerState.OFF;
    }

    private enum IndexerState {
        OFF,
        INDEXING,
    }

    @Override
    public void periodic() {
        super.periodic();
        Logger.recordOutput(name + "/State", state);

        switch (state) {
            case OFF -> super.setVoltage(Volts.of(0.0));
            case INDEXING -> super.setVelocitySetpoint(IndexerConstants.INDEX_VELOCITY);
        }
    }

    void off() {
        state = IndexerState.OFF;
    }

    void index() {
        state = IndexerState.INDEXING;
    }

    public static final class IndexerConstants {
        public static final FeedforwardValues FEEDFORWARD_VALUES = new FeedforwardValues(1.0, 0.0, 0.0, 0.0, 0.0, 0.0);

        private static final double ROTOR_TO_MECHANISM_RATIO = 1.0;
        private static final AngularVelocity INDEX_VELOCITY = RadiansPerSecond.of(Math.PI * 100.0);

        private IndexerConstants() {
        }
    }
}

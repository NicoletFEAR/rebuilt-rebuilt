package frc.robot.subsystems;

import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.Volts;

import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.wpilibj2.command.Command;
import frc.lib.module.AngularVelocityModule;
import frc.lib.motor.MotorIO;

public class Indexer extends AngularVelocityModule {
    public Indexer(MotorIO motor) {
        super("Launcher/Indexer", motor, IndexerConstants.ROTOR_TO_MECHANISM_RATIO);
    }

    public Command off() {
        return super.setVoltage(Volts.of(0.0));
    }

    public Command index() {
        return super.setVelocitySetpoint(IndexerConstants.INDEX_VELOCITY);
    }

    public static final class IndexerConstants {
        private static final double ROTOR_TO_MECHANISM_RATIO = 1.0;

        private static final AngularVelocity INDEX_VELOCITY = RadiansPerSecond.of(Math.PI * 100.0);

        private IndexerConstants() {
        }
    }
}

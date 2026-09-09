package frc.robot.subsystems.launcher;

import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.Volts;

import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.wpilibj2.command.Command;
import frc.lib.module.velocity.AngularVelocityModule;
import frc.lib.motor.MotorIO;

class Indexer extends AngularVelocityModule {
    Indexer(MotorIO motor) {
        super("Launcher/Indexer", motor, IndexerConstants.ROTOR_TO_MECHANISM_RATIO);
    }

    Command off() {
        return super.setVoltage(Volts.of(0.0));
    }

    Command index() {
        return super.setVelocitySetpoint(IndexerConstants.INDEX_VELOCITY);
    }

    private static final class IndexerConstants {
        private static final double ROTOR_TO_MECHANISM_RATIO = 1.0;

        private static final AngularVelocity INDEX_VELOCITY = RadiansPerSecond.of(Math.PI * 100.0);

        private IndexerConstants() {
        }
    }
}

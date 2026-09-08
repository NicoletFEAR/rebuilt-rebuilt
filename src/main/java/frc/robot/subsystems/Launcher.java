package frc.robot.subsystems;

import static edu.wpi.first.units.Units.Radians;
import static edu.wpi.first.units.Units.RadiansPerSecond;

import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.wpilibj2.command.Command;
import frc.lib.cancoder.CanCoderIO;
import frc.lib.module.CanCoderAngularPositionModule;
import frc.lib.module.MultiVelocityModule;
import frc.lib.module.VelocityModule;
import frc.lib.motor.MotorIO;

public class Launcher {
    private final MultiVelocityModule flywheels;
    private final VelocityModule indexer;
    private final CanCoderAngularPositionModule hood;

    public Launcher(MotorIO leftFlywheelMotor, MotorIO rightFlywheelMotor, MotorIO indexMotor, MotorIO hoodMotor,
            CanCoderIO hoodCanCoder) {
        flywheels = new MultiVelocityModule("Launcher/Flywheels", leftFlywheelMotor,
                new MotorIO[] { rightFlywheelMotor }, 0.8);
        indexer = new VelocityModule("Launcher/Indexer", indexMotor, 1.0);
        hood = new CanCoderAngularPositionModule("Launcher/Hood", hoodMotor, hoodCanCoder, 30.0);
    }

    public Command off() {
        return flywheelsOff().alongWith(indexerOff()).alongWith(lowerHood());
    }

    public Command launch() {
        return flywheelsLaunch().alongWith(raiseHoodToMediumPosition()).andThen(index());
    }

    private Command flywheelsOff() {
        return flywheels.setVelocitySetpoint(LauncherConstants.FLYWHEEL_OFF_SPEED);
    }

    private Command flywheelsLaunch() {
        return flywheels.runToVelocity(LauncherConstants.FLYWHEEL_LAUNCHING_SPEED);
    }

    private Command indexerOff() {
        return indexer.setVelocitySetpoint(LauncherConstants.INDEXER_OFF_SPEED);
    }

    private Command index() {
        return indexer.setVelocitySetpoint(LauncherConstants.INDEXER_INDEXING_SPEED);
    }

    private Command lowerHood() {
        return hood.setPositionSetpoint(LauncherConstants.HOOD_HOME_POSITION);
    }

    private Command raiseHoodToMediumPosition() {
        return hood.runToPosition(LauncherConstants.HOOD_MEDIUM_POSITION);
    }

    public static final class LauncherConstants {
        private static final AngularVelocity FLYWHEEL_OFF_SPEED = RadiansPerSecond.of(0.0);
        private static final AngularVelocity FLYWHEEL_LAUNCHING_SPEED = RadiansPerSecond.of(Math.PI * 200.0);

        private static final AngularVelocity INDEXER_OFF_SPEED = RadiansPerSecond.of(0.0);
        private static final AngularVelocity INDEXER_INDEXING_SPEED = RadiansPerSecond.of(Math.PI * 100.0);

        private static final Angle HOOD_HOME_POSITION = Radians.of(0.0);
        private static final Angle HOOD_MEDIUM_POSITION = Radians.of(Math.PI / 6.0);

        private LauncherConstants() {
        }
    }
}

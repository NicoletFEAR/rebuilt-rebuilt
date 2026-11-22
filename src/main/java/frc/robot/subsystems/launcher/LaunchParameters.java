package frc.robot.subsystems.launcher;

import frc.lib.logging.LoggableOutputs;
import frc.lib.units.Angle;
import frc.lib.units.AngularVelocity;

/**
 * Launcher parameters for setting aim.
 *
 * @param flywheelVelocity velocity at which to spin the flywheels
 * @param hoodAngle angle to which to move the hood
 */
public record LaunchParameters(AngularVelocity flywheelVelocity, Angle hoodAngle)
        implements LoggableOutputs {
    /**
     * Logs the launch parameters at the given key.
     *
     * @param key the key at which to log the parameters
     */
    @Override
    public void log(String key) {
        flywheelVelocity.log(key + "/Flywheel Velocity");
        hoodAngle.log(key + "/Hood Angle");
    }
}

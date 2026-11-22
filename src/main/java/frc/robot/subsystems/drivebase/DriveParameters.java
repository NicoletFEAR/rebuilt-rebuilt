package frc.robot.subsystems.drivebase;

import frc.lib.logging.LoggableOutputs;
import org.littletonrobotics.junction.Logger;

/**
 * Driver input parameters for teleop driving.
 *
 * @param x normalized forward input (1 = full forward)
 * @param y normalized strafe input (1 = full left)
 * @param omega normalized rotation input (1 = full counterclockwise)
 */
public record DriveParameters(double x, double y, double omega) implements LoggableOutputs {
    /**
     * Logs the drive parameters at the given key.
     *
     * @param key the key at which to log the parameters
     */
    @Override
    public void log(String key) {
        Logger.recordOutput(key + "/X", x);
        Logger.recordOutput(key + "/Y", y);
        Logger.recordOutput(key + "/Omega", omega);
    }
}

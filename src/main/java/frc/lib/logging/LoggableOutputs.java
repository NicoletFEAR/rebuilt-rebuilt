package frc.lib.logging;

/**
 * Represents anything that can be logged as an output.
 *
 * <p>There is an important distinction between logging inputs and logging outputs. Inputs are data
 * sent to the SystemCore from connected devices, such as a motor's temperature or whether a beam
 * break is tripped, while outputs are data computed by the SystemCore, like estimated drivebase
 * position or subsystem states. To represent something that can be logged as an input, use
 * AdvantageKit's {@code LoggableInputs} class instead.
 */
public interface LoggableOutputs {
    /**
     * Logs this value to AdvantageKit at the provided key.
     *
     * @param key the key at which to log the value
     */
    void log(String key);
}

package frc.lib.statemachine;

import frc.lib.StringHelpers;
import org.littletonrobotics.junction.Logger;

/**
 * Represents a state of a state machine.
 *
 * <p>States are typically implemented either as enums or as (sealed) records carrying state data.
 * This interface provides a {@link #log(String)} helper that logs a human-readable name for the
 * state to AdvantageKit; record states also log their values.
 */
public interface State {

    /**
     * Logs this state's name (and, for record states, its values) under the given key.
     *
     * @param key Logger key prefix to log under (e.g. {@code "Intake/State"})
     */
    default void log(String key) {
        Class<? extends State> c = getClass();

        if (this instanceof Record r) {
            Logger.recordOutput(key + "/Name", StringHelpers.pascalCaseToWords(c.getSimpleName()));
            Logger.recordOutput(key + "/Values", r);
        } else if (this instanceof Enum<?> e) {
            Logger.recordOutput(key + "/Name", StringHelpers.screamingSnakeCaseToWords(e.name()));
        }
    }
}

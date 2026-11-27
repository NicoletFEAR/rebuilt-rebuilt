package com.nicoletfear.lib.statemachine;

import com.nicoletfear.lib.StringHelpers;
import com.nicoletfear.lib.logging.LoggableOutputs;
import java.lang.reflect.RecordComponent;
import org.littletonrobotics.junction.Logger;
import org.wpilib.math.kinematics.SwerveModuleVelocity;

/**
 * Represents a state of a state machine.
 *
 * <p>States are typically implemented either as enums or as (sealed) records carrying state data.
 * This interface provides a {@link #log(String)} helper that logs a human-readable name for the
 * state to AdvantageKit; record states also log their values.
 */
public interface State extends LoggableOutputs {

    /**
     * Logs this state's name (and, for record states, its values) under the given key.
     *
     * @param key Logger key prefix to log under (e.g. {@code "Intake/State"})
     * @throws RuntimeException if this is a record and a field can't be accessed or doesn't implement
     *     {@code LoggableOutputs}
     */
    @Override
    default void log(String key) {
        Class<? extends State> c = getClass();

        if (this instanceof Record) {
            Logger.recordOutput(key + "/Name", StringHelpers.pascalCaseToWords(c.getSimpleName()));

            // Loops over all components associated with the record
            for (RecordComponent component : c.getRecordComponents()) {
                // Gets the name of the component
                String fieldName = StringHelpers.camelCaseToWords(component.getName());

                try {
                    // Gets the value of the component
                    Object value = component.getAccessor().invoke(this);

                    // Logs the value of the component based on the component's type
                    if (value instanceof LoggableOutputs loggable) {
                        loggable.log(key + "/" + fieldName);
                    } else if (value instanceof SwerveModuleVelocity loggable) {
                        Logger.recordOutput(key + "/" + fieldName, loggable);
                    } else {
                        throw new RuntimeException("Field `" + fieldName + "` is not loggable");
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Failed to extract field `" + fieldName + "`", e);
                }
            }
        } else if (this instanceof Enum<?> e) {
            Logger.recordOutput(key + "/Name", StringHelpers.screamingSnakeCaseToWords(e.name()));
        }
    }
}

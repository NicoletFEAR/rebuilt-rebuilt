package frc.lib.statemachine;

import org.littletonrobotics.junction.Logger;

import frc.lib.StringHelpers;

public interface State {
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

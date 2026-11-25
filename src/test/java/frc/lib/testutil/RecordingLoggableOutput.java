package frc.lib.testutil;

import frc.lib.logging.LoggableOutputs;
import java.util.ArrayList;
import java.util.List;

/** A {@link LoggableOutputs} implementation that records the keys it is asked to log. */
public class RecordingLoggableOutput implements LoggableOutputs {
    /** Keys passed to {@link #log(String)}, in call order. */
    public final List<String> keys = new ArrayList<>();

    @Override
    public void log(String key) {
        keys.add(key);
    }
}

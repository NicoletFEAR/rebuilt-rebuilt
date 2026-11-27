package com.nicoletfear.robot;

import static com.nicoletfear.lib.units.FrequencyUnit.Hertz;

import com.nicoletfear.lib.units.Frequency;

/** Constants critical to the way the code is executed. */
public final class ExecutionConstants {

    /**
     * Used as a reference for how long each periodic loop is.
     *
     * <p>Each periodic loop is 20 ms.
     */
    public static final Frequency LOOP_PERIOD = Hertz.of(50);

    private ExecutionConstants() {
        // Keep this constructor empty.
    }
}

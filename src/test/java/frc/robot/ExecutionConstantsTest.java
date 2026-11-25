package frc.robot;

import static frc.lib.units.FrequencyUnit.Hertz;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ExecutionConstantsTest {
    @Test
    void loopPeriodIsFiftyHertz() {
        assertEquals(50.0, ExecutionConstants.LOOP_PERIOD.in(Hertz), 1e-9);
    }
}

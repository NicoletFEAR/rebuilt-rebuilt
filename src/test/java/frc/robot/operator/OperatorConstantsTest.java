package frc.robot.operator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class OperatorConstantsTest {
    @Test
    void deadbandIsPointOne() {
        assertEquals(0.1, OperatorConstants.DEADBAND, 1e-12);
    }

    @Test
    void triggerThresholdIsPointNineFive() {
        assertEquals(0.95, OperatorConstants.TRIGGER_THRESHOLD, 1e-12);
    }
}

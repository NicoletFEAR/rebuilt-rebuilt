package frc.lib;

import static frc.lib.units.AngleUnit.Radians;
import static frc.lib.units.DistanceUnit.Meters;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ToleranceConstantsTest {
    private static final double EPSILON = 1e-12;

    @Test
    void velocityToleranceMultiplierIsFivePercent() {
        assertEquals(0.05, ToleranceConstants.VELOCITY_SETPOINT_TOLERANCE_MULTIPLIER, EPSILON);
    }

    @Test
    void angularPositionToleranceIsFiveHundredthsOfARadian() {
        assertEquals(0.05, ToleranceConstants.ANGULAR_POSITION_SETPOINT_TOLERANCE.in(Radians), EPSILON);
    }

    @Test
    void linearPositionToleranceIsFiveMillimeters() {
        assertEquals(0.005, ToleranceConstants.LINEAR_POSITION_SETPOINT_TOLERANCE.in(Meters), EPSILON);
    }
}

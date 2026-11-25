package frc.robot.subsystems;

import static frc.lib.units.TimeUnit.Seconds;
import static frc.lib.units.VoltageUnit.Volts;
import static frc.lib.units.VoltageVelocityUnit.VoltsPerSecond;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CharacterizationConstantsTest {
    private static final double EPSILON = 1e-9;

    @Test
    void rampRatesMatchExpectedValues() {
        assertEquals(1.0, CharacterizationConstants.DRIVE_RAMP_RATE.in(VoltsPerSecond), EPSILON);
        assertEquals(1.0, CharacterizationConstants.TURN_RAMP_RATE.in(VoltsPerSecond), EPSILON);
        assertEquals(Math.PI / 6.0, CharacterizationConstants.ROTATION_RAMP_RATE.in(VoltsPerSecond), EPSILON);
    }

    @Test
    void stepVoltagesMatchExpectedValues() {
        assertEquals(4.0, CharacterizationConstants.DRIVE_STEP_VOLTAGE.in(Volts), EPSILON);
        assertEquals(7.0, CharacterizationConstants.TURN_STEP_VOLTAGE.in(Volts), EPSILON);
        assertEquals(Math.PI, CharacterizationConstants.ROTATION_STEP_VOLTAGE.in(Volts), EPSILON);
    }

    @Test
    void sysIdTimeoutIsFiveSeconds() {
        assertEquals(5.0, CharacterizationConstants.SYS_ID_TIMEOUT.in(Seconds), EPSILON);
    }
}

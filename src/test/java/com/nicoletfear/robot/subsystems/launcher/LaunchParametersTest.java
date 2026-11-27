package com.nicoletfear.robot.subsystems.launcher;

import static com.nicoletfear.lib.units.AngleUnit.Radians;
import static com.nicoletfear.lib.units.AngularVelocityUnit.RadiansPerSecond;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import com.nicoletfear.lib.logging.LoggableOutputs;
import org.junit.jupiter.api.Test;

class LaunchParametersTest {
    @Test
    void storesFlywheelVelocityAndHoodAngle() {
        LaunchParameters parameters = new LaunchParameters(RadiansPerSecond.of(25.0), Radians.of(0.75));

        assertEquals(25.0, parameters.flywheelVelocity().in(RadiansPerSecond), 1e-12);
        assertEquals(0.75, parameters.hoodAngle().in(Radians), 1e-12);
    }

    @Test
    void isLoggable() {
        LaunchParameters parameters = new LaunchParameters(RadiansPerSecond.of(25.0), Radians.of(0.75));

        assertInstanceOf(LoggableOutputs.class, parameters);
        assertDoesNotThrow(() -> parameters.log("Test/Launch Parameters"));
    }
}

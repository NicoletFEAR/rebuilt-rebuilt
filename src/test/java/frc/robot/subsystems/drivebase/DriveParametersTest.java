package frc.robot.subsystems.drivebase;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import frc.lib.logging.LoggableOutputs;
import org.junit.jupiter.api.Test;

class DriveParametersTest {
    @Test
    void storesInputs() {
        DriveParameters parameters = new DriveParameters(0.25, -0.5, 0.75);

        assertEquals(0.25, parameters.x(), 1e-12);
        assertEquals(-0.5, parameters.y(), 1e-12);
        assertEquals(0.75, parameters.omega(), 1e-12);
    }

    @Test
    void isLoggable() {
        DriveParameters parameters = new DriveParameters(0.25, -0.5, 0.75);

        assertInstanceOf(LoggableOutputs.class, parameters);
        assertDoesNotThrow(() -> parameters.log("Test/Drive Parameters"));
    }
}

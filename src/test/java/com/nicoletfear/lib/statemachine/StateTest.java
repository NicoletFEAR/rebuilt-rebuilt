package com.nicoletfear.lib.statemachine;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.nicoletfear.lib.testutil.RecordingLoggableOutput;
import org.junit.jupiter.api.Test;
import org.wpilib.math.geometry.Rotation2d;
import org.wpilib.math.kinematics.SwerveModuleVelocity;

class StateTest {
    private enum ExampleEnumState implements State {
        FIRST_STATE,
        SECOND_STATE
    }

    private record RecordState(RecordingLoggableOutput output, SwerveModuleVelocity velocity) implements State {}

    private record EmptyRecordState() implements State {}

    private record UnsupportedRecordState(String value) implements State {}

    @Test
    void enumStateLoggingIsSafe() {
        assertDoesNotThrow(() -> ExampleEnumState.FIRST_STATE.log("Test/Enum State"));
        assertDoesNotThrow(() -> ExampleEnumState.SECOND_STATE.log("Test/Enum State"));
    }

    @Test
    void recordStateLogsEachLoggableComponent() {
        RecordingLoggableOutput output = new RecordingLoggableOutput();
        RecordState state = new RecordState(output, new SwerveModuleVelocity(1.5, Rotation2d.fromRadians(0.5)));

        state.log("Test/Record State");

        assertEquals(1, output.keys.size());
        assertEquals("Test/Record State/Output", output.keys.get(0));
    }

    @Test
    void emptyRecordStateLoggingIsSafe() {
        assertDoesNotThrow(() -> new EmptyRecordState().log("Test/Empty"));
    }

    @Test
    void unsupportedRecordComponentThrows() {
        RuntimeException exception =
                assertThrows(RuntimeException.class, () -> new UnsupportedRecordState("not loggable").log("Test/Bad"));

        assertTrue(exception.getMessage().contains("Failed to extract field"));
        assertTrue(exception.getCause() instanceof RuntimeException);
    }

    @Test
    void nullRecordComponentThrows() {
        RuntimeException exception =
                assertThrows(RuntimeException.class, () -> new RecordState(null, null).log("Test/Null"));

        assertTrue(exception.getMessage().contains("Failed to extract field"));
    }
}

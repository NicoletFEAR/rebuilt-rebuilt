package com.nicoletfear.robot.subsystems.drivebase;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class TurnMotorConstantsTest {
    @Test
    void everyModuleHasAnOffset() {
        for (SwerveModuleName module : SwerveModuleName.values()) {
            assertEquals(true, TurnMotor.TurnMotorConstants.OFFSET.containsKey(module), "Missing offset for " + module);
        }
        assertEquals(SwerveModuleName.values().length, TurnMotor.TurnMotorConstants.OFFSET.size());
    }
}

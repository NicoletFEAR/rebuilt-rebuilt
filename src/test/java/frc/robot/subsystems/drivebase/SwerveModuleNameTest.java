package frc.robot.subsystems.drivebase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import frc.robot.subsystems.DeviceIds;
import org.junit.jupiter.api.Test;

class SwerveModuleNameTest {
    @Test
    void toStringReturnsTitleCaseWords() {
        assertEquals("Front Left", SwerveModuleName.FRONT_LEFT.toString());
        assertEquals("Front Right", SwerveModuleName.FRONT_RIGHT.toString());
        assertEquals("Back Left", SwerveModuleName.BACK_LEFT.toString());
        assertEquals("Back Right", SwerveModuleName.BACK_RIGHT.toString());
    }

    @Test
    void driveReturnsTheConfiguredDriveId() {
        for (SwerveModuleName module : SwerveModuleName.values()) {
            assertSame(DeviceIds.DRIVE.get(module), module.drive());
        }
    }

    @Test
    void turnReturnsTheConfiguredTurnId() {
        for (SwerveModuleName module : SwerveModuleName.values()) {
            assertSame(DeviceIds.TURN.get(module), module.turn());
        }
    }

    @Test
    void absEncoderReturnsTheConfiguredEncoderId() {
        for (SwerveModuleName module : SwerveModuleName.values()) {
            assertSame(DeviceIds.SWERVE_ABS_ENCODER.get(module), module.absEncoder());
        }
    }

    @Test
    void offsetReturnsTheConfiguredOffset() {
        for (SwerveModuleName module : SwerveModuleName.values()) {
            assertSame(TurnMotor.TurnMotorConstants.OFFSET.get(module), module.offset());
            assertNotNull(module.offset());
        }
    }

    @Test
    void thereAreExactlyFourModules() {
        assertEquals(4, SwerveModuleName.values().length);
    }
}

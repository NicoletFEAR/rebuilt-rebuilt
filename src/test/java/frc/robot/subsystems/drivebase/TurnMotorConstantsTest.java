package frc.robot.subsystems.drivebase;

import static frc.lib.units.AngleUnit.Rotations;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class TurnMotorConstantsTest {
    private static final double EPSILON = 1e-12;

    @Test
    void everyModuleHasAnOffset() {
        for (SwerveModuleName module : SwerveModuleName.values()) {
            assertEquals(
                    true,
                    TurnMotor.TurnMotorConstants.OFFSET.containsKey(module),
                    "Missing offset for " + module);
        }
        assertEquals(
                SwerveModuleName.values().length, TurnMotor.TurnMotorConstants.OFFSET.size());
    }

    @Test
    void offsetsMatchTheExpectedValues() {
        assertEquals(
                -0.8896484375,
                TurnMotor.TurnMotorConstants.OFFSET
                        .get(SwerveModuleName.FRONT_LEFT)
                        .in(Rotations),
                EPSILON);
        assertEquals(
                -0.09716796875,
                TurnMotor.TurnMotorConstants.OFFSET
                        .get(SwerveModuleName.FRONT_RIGHT)
                        .in(Rotations),
                EPSILON);
        assertEquals(
                -0.721923828125,
                TurnMotor.TurnMotorConstants.OFFSET
                        .get(SwerveModuleName.BACK_LEFT)
                        .in(Rotations),
                EPSILON);
        assertEquals(
                -0.37841796875,
                TurnMotor.TurnMotorConstants.OFFSET
                        .get(SwerveModuleName.BACK_RIGHT)
                        .in(Rotations),
                EPSILON);
    }
}

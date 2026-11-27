package com.nicoletfear.robot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;
import org.wpilib.math.geometry.Rotation2d;

class UtilsTest {
    private static final double EPSILON = 1e-12;

    @Test
    void zeroVectorReturnsZeroRotation() {
        assertSame(Rotation2d.ZERO, Utils.zeroSafeRotation2d(0.0, 0.0));
    }

    @Test
    void negativeZeroVectorReturnsZeroRotation() {
        assertSame(Rotation2d.ZERO, Utils.zeroSafeRotation2d(-0.0, 0.0));
        assertSame(Rotation2d.ZERO, Utils.zeroSafeRotation2d(0.0, -0.0));
    }

    @Test
    void positiveXVectorIsZeroRadians() {
        assertEquals(0.0, Utils.zeroSafeRotation2d(1.0, 0.0).getRadians(), EPSILON);
    }

    @Test
    void positiveYVectorIsNinetyDegrees() {
        assertEquals(Math.PI / 2.0, Utils.zeroSafeRotation2d(0.0, 1.0).getRadians(), EPSILON);
    }

    @Test
    void diagonalVectorIsFortyFiveDegrees() {
        assertEquals(Math.PI / 4.0, Utils.zeroSafeRotation2d(1.0, 1.0).getRadians(), EPSILON);
    }

    @Test
    void negativeXVectorsAreOnTheCorrectSide() {
        assertEquals(Math.PI, Utils.zeroSafeRotation2d(-1.0, 0.0).getRadians(), EPSILON);
        assertEquals(-Math.PI / 2.0, Utils.zeroSafeRotation2d(0.0, -1.0).getRadians(), EPSILON);
    }

    @Test
    void usesAtan2ForArbitraryVectors() {
        assertEquals(Math.atan2(4.0, 3.0), Utils.zeroSafeRotation2d(3.0, 4.0).getRadians(), EPSILON);
        assertEquals(
                Math.atan2(-2.0, -1.0), Utils.zeroSafeRotation2d(-1.0, -2.0).getRadians(), EPSILON);
    }

    @Test
    void tinyNonZeroVectorDoesNotCollapseToZero() {
        Rotation2d rotation = Utils.zeroSafeRotation2d(1e-12, 0.0);

        assertEquals(0.0, rotation.getRadians(), EPSILON);
        org.junit.jupiter.api.Assertions.assertNotSame(Rotation2d.ZERO, rotation);
    }
}

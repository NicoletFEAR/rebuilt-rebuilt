package com.nicoletfear.lib.motor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.ctre.phoenix6.configs.Slot0Configs;
import org.junit.jupiter.api.Test;

class FeedforwardValuesTest {
    private static final double EPSILON = 1e-9;

    @Test
    void storesAllGains() {
        FeedforwardValues values = new FeedforwardValues(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);

        assertEquals(1.0, values.p(), EPSILON);
        assertEquals(2.0, values.i(), EPSILON);
        assertEquals(3.0, values.d(), EPSILON);
        assertEquals(4.0, values.s(), EPSILON);
        assertEquals(5.0, values.v(), EPSILON);
        assertEquals(6.0, values.a(), EPSILON);
        assertEquals(7.0, values.g(), EPSILON);
    }

    @Test
    void sixArgumentConstructorDefaultsGravityToZero() {
        FeedforwardValues values = new FeedforwardValues(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);

        assertEquals(0.0, values.g(), EPSILON);
    }

    @Test
    void convertsToSlot0Configs() {
        FeedforwardValues values = new FeedforwardValues(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);

        Slot0Configs configs = values.getSlot0Configs();

        assertEquals(1.0, configs.kP, EPSILON);
        assertEquals(2.0, configs.kI, EPSILON);
        assertEquals(3.0, configs.kD, EPSILON);
        assertEquals(4.0, configs.kS, EPSILON);
        assertEquals(5.0, configs.kV, EPSILON);
        assertEquals(6.0, configs.kA, EPSILON);
        assertEquals(7.0, configs.kG, EPSILON);
    }

    @Test
    void recordEqualityUsesAllGains() {
        FeedforwardValues first = new FeedforwardValues(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
        FeedforwardValues same = new FeedforwardValues(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
        FeedforwardValues different = new FeedforwardValues(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 8.0);

        assertEquals(first, same);
        assertEquals(first.hashCode(), same.hashCode());
        org.junit.jupiter.api.Assertions.assertNotEquals(first, different);
    }
}

package com.nicoletfear.lib.units;

import static com.nicoletfear.lib.units.DistanceUnit.Inches;
import static com.nicoletfear.lib.units.DistanceUnit.Meters;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.nicoletfear.lib.ToleranceConstants;
import org.junit.jupiter.api.Test;

class UnitTest {
    @Test
    void convertsFromAnotherUnit() {
        assertEquals(0.0254, Meters.from(1.0, Inches), ToleranceConstants.EPSILON);
        assertEquals(39.37007874015748, Inches.from(1.0, Meters), 1e-9);
        assertEquals(2.0, Meters.from(200.0, new DistanceUnit(0.01)), ToleranceConstants.EPSILON);
    }

    @Test
    void convertingFromTheSameUnitIsIdentity() {
        assertEquals(12.5, Meters.from(12.5, Meters), ToleranceConstants.EPSILON);
        assertEquals(12.5, Inches.from(12.5, Inches), ToleranceConstants.EPSILON);
    }

    @Test
    void rawReturnsTheSameValue() {
        assertEquals(12.5, Meters.raw(12.5), 0.0);
        assertEquals(-3.0, Inches.raw(-3.0), 0.0);
    }

    @Test
    void ofAndInAreInverses() {
        DistanceUnit[] units = {Meters, Inches, new DistanceUnit(1000.0), new DistanceUnit(0.001)};

        for (DistanceUnit unit : units) {
            Distance quantity = unit.of(7.25);
            assertEquals(7.25, quantity.in(unit), ToleranceConstants.EPSILON);
        }
    }
}

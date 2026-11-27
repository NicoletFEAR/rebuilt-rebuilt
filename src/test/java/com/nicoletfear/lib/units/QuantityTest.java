package com.nicoletfear.lib.units;

import static com.nicoletfear.lib.units.AngleUnit.Degrees;
import static com.nicoletfear.lib.units.AngleUnit.Radians;
import static com.nicoletfear.lib.units.AngleUnit.Rotations;
import static com.nicoletfear.lib.units.DistanceUnit.Inches;
import static com.nicoletfear.lib.units.DistanceUnit.Meters;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.nicoletfear.lib.ToleranceConstants;
import org.junit.jupiter.api.Test;
import org.littletonrobotics.junction.LogTable;

class QuantityTest {
    @Test
    void storesValueInBaseUnits() {
        Distance distance = Meters.of(2.5);

        assertEquals(2.5, distance.value, ToleranceConstants.EPSILON);
        assertEquals("m", distance.baseUnitSymbol);
    }

    @Test
    void convertsToOtherUnits() {
        assertEquals(39.37007874015748, Meters.of(1.0).in(Inches), 1e-9);
        assertEquals(2.54, Inches.of(100.0).in(Meters), 1e-9);
        assertEquals(180.0, Rotations.of(0.5).in(Degrees), 1e-9);
        assertEquals(Math.PI, Rotations.of(0.5).in(Radians), 1e-9);
        assertEquals(0.5, Radians.of(Math.PI).in(Rotations), 1e-9);
    }

    @Test
    void convertsUsingCustomUnit() {
        DistanceUnit doubleMeters = new DistanceUnit(2.0);

        assertEquals(2.0, Meters.of(4.0).in(doubleMeters), ToleranceConstants.EPSILON);
        assertEquals(4.0, doubleMeters.of(2.0).in(Meters), ToleranceConstants.EPSILON);
    }

    @Test
    void handlesNegativeValues() {
        assertEquals(-2.5, Meters.of(-2.5).value, ToleranceConstants.EPSILON);
        assertEquals(-98.4251968503937, Meters.of(-2.5).in(Inches), 1e-9);
    }

    @Test
    void comparesValues() {
        Distance one = Meters.of(1.0);
        Distance two = Meters.of(2.0);

        assertTrue(two.greaterThan(one));
        assertFalse(one.greaterThan(two));
        assertTrue(one.lessThan(two));
        assertFalse(two.lessThan(one));
        assertFalse(one.greaterThan(one));
        assertFalse(one.lessThan(one));
    }

    @Test
    void equalityUsesStrictTolerance() {
        Distance one = Meters.of(1.0);
        Distance almostOne = Meters.of(1.05);
        Distance tolerance = Meters.of(0.1);

        assertTrue(one.equal(almostOne, tolerance));
        assertFalse(one.notEqual(almostOne, tolerance));

        Distance exactlyTolerance = Meters.of(1.5);
        Distance exactTolerance = Meters.of(0.5);
        assertFalse(one.equal(exactlyTolerance, exactTolerance));
        assertFalse(one.notEqual(exactlyTolerance, exactTolerance));
    }

    @Test
    void inequalityUsesStrictTolerance() {
        Distance one = Meters.of(1.0);
        Distance far = Meters.of(1.2);
        Distance tolerance = Meters.of(0.1);

        assertTrue(one.notEqual(far, tolerance));
        assertFalse(one.equal(far, tolerance));
    }

    @Test
    void writesAndReadsLogTable() {
        Distance original = Meters.of(3.75);
        LogTable table = new LogTable(0);

        original.toLog(table);

        Distance restored = new Distance(0.0);
        restored.fromLog(table);

        assertEquals(3.75, restored.value, ToleranceConstants.EPSILON);
        assertEquals("m", restored.baseUnitSymbol);
        assertEquals(3.75, restored.in(Meters), ToleranceConstants.EPSILON);
    }

    @Test
    void fromLogKeepsExistingValuesWhenKeysAreMissing() {
        Distance distance = Meters.of(1.25);
        LogTable emptyTable = new LogTable(0);

        distance.fromLog(emptyTable);

        assertEquals(1.25, distance.value, ToleranceConstants.EPSILON);
        assertEquals("m", distance.baseUnitSymbol);
    }

    @Test
    void loggingIsSafeWithoutAdvantageKitRunning() {
        assertDoesNotThrow(() -> Meters.of(1.0).log("Test/Distance"));
        assertDoesNotThrow(() -> Radians.of(1.0).log("Test/Angle"));
    }

    @Test
    void baseUnitSymbolsMatchTheirQuantities() {
        assertEquals("rad", Radians.of(0.0).baseUnitSymbol);
        assertEquals("V", VoltageUnit.Volts.of(0.0).baseUnitSymbol);
        assertEquals("A", CurrentUnit.Amps.of(0.0).baseUnitSymbol);
        assertEquals("K", TemperatureUnit.Kelvin.of(0.0).baseUnitSymbol);
        assertEquals("s", TimeUnit.Seconds.of(0.0).baseUnitSymbol);
        assertEquals("Hz", FrequencyUnit.Hertz.of(0.0).baseUnitSymbol);
    }
}

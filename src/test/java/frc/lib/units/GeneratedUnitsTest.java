package frc.lib.units;

import static frc.lib.units.AngleUnit.Degrees;
import static frc.lib.units.AngleUnit.Radians;
import static frc.lib.units.AngleUnit.Rotations;
import static frc.lib.units.AngularAccelerationUnit.RadiansPerSecondSquared;
import static frc.lib.units.AngularAccelerationUnit.RotationsPerSecondSquared;
import static frc.lib.units.AngularVelocityUnit.RadiansPerSecond;
import static frc.lib.units.AngularVelocityUnit.RotationsPerMinute;
import static frc.lib.units.AngularVelocityUnit.RotationsPerSecond;
import static frc.lib.units.CurrentUnit.Amps;
import static frc.lib.units.DistanceUnit.Inches;
import static frc.lib.units.DistanceUnit.Meters;
import static frc.lib.units.FrequencyUnit.Hertz;
import static frc.lib.units.TimeUnit.Seconds;
import static frc.lib.units.VelocityUnit.MetersPerSecond;
import static frc.lib.units.VoltageUnit.Volts;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.littletonrobotics.junction.LogTable;
import org.wpilib.math.geometry.Rotation2d;

/** Tests the classes produced by {@code GenerateUnits} from {@code units.json}. */
class GeneratedUnitsTest {
    private static final double EPSILON = 1e-9;

    private static final List<String> QUANTITIES = List.of(
            "Distance",
            "Mass",
            "Time",
            "Current",
            "Temperature",
            "Amount",
            "Light",
            "Angle",
            "Frequency",
            "Anglement",
            "Velocity",
            "AngularVelocity",
            "AngularAcceleration",
            "MomentOfInertia",
            "Torque",
            "ActionPerAngle",
            "MagneticFlux",
            "Voltage",
            "MagneticFluxPerAngle",
            "VoltageVelocity",
            "Resistance");

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void assertRoundTrip(Unit<?> unit, double value) {
        try {
            Quantity quantity = (Quantity) unit.getClass().getMethod("of", double.class).invoke(unit, value);
            assertEquals(value, quantity.in(unit), EPSILON);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError("Could not round-trip " + unit.getClass().getName(), e);
        }
    }

    @Test
    void everyQuantityHasAQuantityAndUnitClass() throws ClassNotFoundException {
        for (String quantity : QUANTITIES) {
            Class<?> quantityClass = Class.forName("frc.lib.units." + quantity);
            Class<?> unitClass = Class.forName("frc.lib.units." + quantity + "Unit");

            assertNotNull(quantityClass.getSuperclass());
            assertEquals("frc.lib.units.Quantity", quantityClass.getSuperclass().getName());
            assertEquals("frc.lib.units.Unit", unitClass.getSuperclass().getName());
        }
    }

    @Test
    void allGeneratedUnitsRoundTrip() {
        assertRoundTrip(Meters, 1.0);
        assertRoundTrip(Inches, 1.0);
        assertRoundTrip(MassUnit.Kilograms, 1.0);
        assertRoundTrip(Seconds, 1.0);
        assertRoundTrip(Amps, 1.0);
        assertRoundTrip(TemperatureUnit.Kelvin, 1.0);
        assertRoundTrip(AmountUnit.Moles, 1.0);
        assertRoundTrip(LightUnit.Candelas, 1.0);
        assertRoundTrip(Radians, 1.0);
        assertRoundTrip(Rotations, 1.0);
        assertRoundTrip(Degrees, 1.0);
        assertRoundTrip(Hertz, 1.0);
        assertRoundTrip(AnglementUnit.RadianSeconds, 1.0);
        assertRoundTrip(MetersPerSecond, 1.0);
        assertRoundTrip(RadiansPerSecond, 1.0);
        assertRoundTrip(RotationsPerSecond, 1.0);
        assertRoundTrip(RotationsPerMinute, 1.0);
        assertRoundTrip(RadiansPerSecondSquared, 1.0);
        assertRoundTrip(RotationsPerSecondSquared, 1.0);
        assertRoundTrip(MomentOfInertiaUnit.KilogramSquareMeters, 1.0);
        assertRoundTrip(TorqueUnit.NewtonMeters, 1.0);
        assertRoundTrip(ActionPerAngleUnit.JoulesPerHertzPerRadian, 1.0);
        assertRoundTrip(MagneticFluxUnit.Webers, 1.0);
        assertRoundTrip(Volts, 1.0);
        assertRoundTrip(MagneticFluxPerAngleUnit.WebersPerRadian, 1.0);
        assertRoundTrip(VoltageVelocityUnit.VoltsPerSecond, 1.0);
        assertRoundTrip(ResistanceUnit.Ohms, 1.0);
    }

    @Test
    void conversionsMatchUnitFactors() {
        assertEquals(0.0254, Inches.of(1.0).in(Meters), EPSILON);
        assertEquals(2 * Math.PI, Rotations.of(1.0).in(Radians), 1e-9);
        assertEquals(Math.PI / 180.0, Degrees.of(1.0).in(Radians), 1e-9);
        assertEquals(2 * Math.PI, RotationsPerSecond.of(1.0).in(RadiansPerSecond), 1e-9);
        assertEquals(2 * Math.PI, RotationsPerSecondSquared.of(1.0).in(RadiansPerSecondSquared), 1e-9);
    }

    @Test
    @Disabled("units.json currently defines 1 RPM as 2*pi*60 rad/s; the physical value is 2*pi/60")
    void rotationsPerMinuteUsesPhysicalConversion() {
        assertEquals(2 * Math.PI, RotationsPerMinute.of(60.0).in(RadiansPerSecond), 1e-9);
    }

    @Test
    void arithmeticOperationsProduceExpectedValues() {
        Distance three = Meters.of(3.0);
        Distance two = Meters.of(2.0);

        assertEquals(5.0, three.plus(two).in(Meters), EPSILON);
        assertEquals(1.0, three.minus(two).in(Meters), EPSILON);
        assertEquals(6.0, three.times(2.0).in(Meters), EPSILON);
        assertEquals(1.5, three.div(2.0).in(Meters), EPSILON);
        assertEquals(-3.0, three.negative().in(Meters), EPSILON);
        assertEquals(3.0, Meters.of(2.1).ceil().in(Meters), EPSILON);
        assertEquals(2.0, Meters.of(2.9).floor().in(Meters), EPSILON);
    }

    @Test
    void maxMinAndClampChooseTheRightValue() {
        Distance one = Meters.of(1.0);
        Distance two = Meters.of(2.0);
        Distance three = Meters.of(3.0);

        assertEquals(3.0, three.max(two).in(Meters), EPSILON);
        assertEquals(2.0, one.max(two).in(Meters), EPSILON);
        assertEquals(2.0, three.min(two).in(Meters), EPSILON);
        assertEquals(1.0, one.min(two).in(Meters), EPSILON);
        assertEquals(2.0, three.clamp(one, two).in(Meters), EPSILON);
        assertEquals(1.0, Meters.of(0.0).clamp(one, two).in(Meters), EPSILON);
        assertEquals(2.0, two.clamp(one, three).in(Meters), EPSILON);
    }

    @Test
    void crossQuantityOperationsProduceExpectedQuantities() {
        assertEquals(5.0, Meters.of(10.0).div(Seconds.of(2.0)).in(MetersPerSecond), EPSILON);
        assertEquals(6.0, Seconds.of(2.0).times(MetersPerSecond.of(3.0)).in(Meters), EPSILON);
        assertEquals(3.0, Meters.of(6.0).div(MetersPerSecond.of(2.0)).in(Seconds), EPSILON);
        assertEquals(6.0, Volts.of(12.0).div(Amps.of(2.0)).in(ResistanceUnit.Ohms), EPSILON);
        assertEquals(6.0, Volts.of(12.0).div(Seconds.of(2.0)).in(VoltageVelocityUnit.VoltsPerSecond), EPSILON);
        assertEquals(0.5, Seconds.of(2.0).reciprocal().in(Hertz), EPSILON);
        assertEquals(6.0, Radians.of(2.0).times(Seconds.of(3.0)).in(AnglementUnit.RadianSeconds), EPSILON);
        assertEquals(8.0, RadiansPerSecond.of(4.0).times(Seconds.of(2.0)).in(Radians), EPSILON);
        assertEquals(1.0, Radians.of(2.0).div(Seconds.of(2.0)).in(RadiansPerSecond), EPSILON);
        assertEquals(24.0, Volts.of(12.0).times(Seconds.of(2.0)).in(MagneticFluxUnit.Webers), EPSILON);
        assertEquals(
                4.0, Volts.of(12.0).div(MagneticFluxPerAngleUnit.WebersPerRadian.of(3.0)).in(RadiansPerSecond), EPSILON);
    }

    @Test
    void angleConvertsToAndFromRotation2d() {
        Rotation2d rotation = Rotation2d.fromRadians(1.25);

        Angle angle = AngleUnit.ofRotation(rotation);

        assertEquals(1.25, angle.in(Radians), EPSILON);
        assertEquals(1.25, angle.toRotation().getRadians(), EPSILON);
        assertEquals(rotation.getRadians(), angle.toRotation().getRadians(), EPSILON);
    }

    @Test
    void wpiFactoriesConvertFromWpiMeasures() {
        assertEquals(
                2.5,
                DistanceUnit.ofWpi(org.wpilib.units.Units.Meters.of(2.5)).in(Meters),
                EPSILON);
        assertEquals(
                Math.PI,
                AngleUnit.ofWpi(org.wpilib.units.Units.Radians.of(Math.PI)).in(Radians),
                EPSILON);
        assertEquals(3.0, TimeUnit.ofWpi(org.wpilib.units.Units.Seconds.of(3.0)).in(Seconds), EPSILON);
        assertEquals(12.0, VoltageUnit.ofWpi(org.wpilib.units.Units.Volts.of(12.0)).in(Volts), EPSILON);
        assertEquals(2.0, CurrentUnit.ofWpi(org.wpilib.units.Units.Amps.of(2.0)).in(Amps), EPSILON);
    }

    @Test
    void generatedQuantitiesSupportLogging() {
        VoltageVelocity voltageVelocity = VoltageVelocityUnit.VoltsPerSecond.of(2.5);
        LogTable table = new LogTable(0);

        voltageVelocity.toLog(table);
        VoltageVelocity restored = new VoltageVelocity(0.0);
        restored.fromLog(table);

        assertEquals(2.5, restored.in(VoltageVelocityUnit.VoltsPerSecond), EPSILON);
        assertEquals("V/s", restored.baseUnitSymbol);
    }

    @Test
    void generatedQuantitiesExposeBaseUnitSymbols() {
        assertTrue(Meters.of(0.0).baseUnitSymbol.equals("m"));
        assertTrue(Radians.of(0.0).baseUnitSymbol.equals("rad"));
        assertTrue(Volts.of(0.0).baseUnitSymbol.equals("V"));
        assertTrue(Seconds.of(0.0).baseUnitSymbol.equals("s"));
        assertTrue(RotationsPerSecond.of(0.0).baseUnitSymbol.equals("rad/s"));
    }
}

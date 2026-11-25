package frc.lib.motor;

import static frc.lib.units.ActionPerAngleUnit.JoulesPerHertzPerRadian;
import static frc.lib.units.AngularVelocityUnit.RadiansPerSecond;
import static frc.lib.units.AngularVelocityUnit.RotationsPerMinute;
import static frc.lib.units.CurrentUnit.Amps;
import static frc.lib.units.MagneticFluxPerAngleUnit.WebersPerRadian;
import static frc.lib.units.MagneticFluxUnit.Webers;
import static frc.lib.units.MomentOfInertiaUnit.KilogramSquareMeters;
import static frc.lib.units.ResistanceUnit.Ohms;
import static frc.lib.units.TorqueUnit.NewtonMeters;
import static frc.lib.units.VoltageUnit.Volts;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class MotorConstantsTest {
    private static final double EPSILON = 1e-9;

    @Test
    void electricalConstantsHaveExpectedValues() {
        assertEquals(12.0, MotorConstants.MAX_VOLTAGE.in(Volts), EPSILON);
        assertEquals(3.0, MotorConstants.FREE_CURRENT.in(Amps), EPSILON);
        assertEquals(279.0, MotorConstants.STALL_CURRENT.in(Amps), EPSILON);
        assertEquals(0.01491, MotorConstants.TORQUE_CONSTANT.in(Webers), EPSILON);
        assertEquals(0.001491, MotorConstants.BACK_EMF_CONSTANT.in(WebersPerRadian), EPSILON);
    }

    @Test
    void mechanicalConstantsHaveExpectedValues() {
        assertEquals(7758.0, MotorConstants.FREE_SPEED.in(RotationsPerMinute), EPSILON);
        assertEquals(0.0, MotorConstants.LOAD_TORQUE.in(NewtonMeters), EPSILON);
        assertEquals(0.016, MotorConstants.MOMENT_OF_INERTIA.in(KilogramSquareMeters), EPSILON);
    }

    @Test
    void windingResistanceIsVoltageOverStallCurrent() {
        assertEquals(12.0 / 279.0, MotorConstants.WINDING_RESISTANCE.in(Ohms), EPSILON);
    }

    @Test
    void viscousFrictionMatchesDefinition() {
        double expected = MotorConstants.FREE_CURRENT.in(Amps) * MotorConstants.TORQUE_CONSTANT.in(Webers)
                / MotorConstants.FREE_SPEED.in(RadiansPerSecond);

        assertEquals(expected, MotorConstants.VISCOUS_FRICTION.in(JoulesPerHertzPerRadian), EPSILON);
    }
}

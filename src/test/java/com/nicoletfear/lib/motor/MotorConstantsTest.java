package com.nicoletfear.lib.motor;

import static com.nicoletfear.lib.units.ActionPerAngleUnit.JoulesPerHertzPerRadian;
import static com.nicoletfear.lib.units.AngularVelocityUnit.RadiansPerSecond;
import static com.nicoletfear.lib.units.AngularVelocityUnit.RotationsPerMinute;
import static com.nicoletfear.lib.units.CurrentUnit.Amps;
import static com.nicoletfear.lib.units.MagneticFluxPerAngleUnit.WebersPerRadian;
import static com.nicoletfear.lib.units.MagneticFluxUnit.Webers;
import static com.nicoletfear.lib.units.MomentOfInertiaUnit.KilogramSquareMeters;
import static com.nicoletfear.lib.units.ResistanceUnit.Ohms;
import static com.nicoletfear.lib.units.TorqueUnit.NewtonMeters;
import static com.nicoletfear.lib.units.VoltageUnit.Volts;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.nicoletfear.lib.ToleranceConstants;
import org.junit.jupiter.api.Test;

class MotorConstantsTest {
    @Test
    void electricalConstantsHaveExpectedValues() {
        assertEquals(12.0, MotorConstants.MAX_VOLTAGE.in(Volts), ToleranceConstants.EPSILON);
        assertEquals(3.0, MotorConstants.FREE_CURRENT.in(Amps), ToleranceConstants.EPSILON);
        assertEquals(279.0, MotorConstants.STALL_CURRENT.in(Amps), ToleranceConstants.EPSILON);
        assertEquals(0.01491, MotorConstants.TORQUE_CONSTANT.in(Webers), ToleranceConstants.EPSILON);
        assertEquals(0.001491, MotorConstants.BACK_EMF_CONSTANT.in(WebersPerRadian), ToleranceConstants.EPSILON);
    }

    @Test
    void mechanicalConstantsHaveExpectedValues() {
        assertEquals(7758.0, MotorConstants.FREE_SPEED.in(RotationsPerMinute), ToleranceConstants.EPSILON);
        assertEquals(0.0, MotorConstants.LOAD_TORQUE.in(NewtonMeters), ToleranceConstants.EPSILON);
        assertEquals(0.016, MotorConstants.MOMENT_OF_INERTIA.in(KilogramSquareMeters), ToleranceConstants.EPSILON);
    }

    @Test
    void windingResistanceIsVoltageOverStallCurrent() {
        assertEquals(12.0 / 279.0, MotorConstants.WINDING_RESISTANCE.in(Ohms), ToleranceConstants.EPSILON);
    }

    @Test
    void viscousFrictionMatchesDefinition() {
        double expected = MotorConstants.FREE_CURRENT.in(Amps)
                * MotorConstants.TORQUE_CONSTANT.in(Webers)
                / MotorConstants.FREE_SPEED.in(RadiansPerSecond);

        assertEquals(expected, MotorConstants.VISCOUS_FRICTION.in(JoulesPerHertzPerRadian), ToleranceConstants.EPSILON);
    }
}

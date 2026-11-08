package frc.lib.motor;

import frc.lib.units.MagneticFlux;
import frc.lib.units.MagneticFluxPerAngle;
import frc.lib.units.MomentOfInertia;
import frc.lib.units.Resistance;
import frc.lib.units.Torque;

import static frc.lib.units.AngularVelocityUnit.RotationsPerMinute;
import static frc.lib.units.CurrentUnit.Amps;
import static frc.lib.units.MagneticFluxUnit.Webers;
import static frc.lib.units.MagneticFluxPerAngleUnit.WebersPerRadian;
import static frc.lib.units.MomentOfInertiaUnit.KilogramSquareMeters;
import static frc.lib.units.TorqueUnit.NewtonMeters;
import static frc.lib.units.VoltageUnit.Volts;

import frc.lib.units.ActionPerAngle;
import frc.lib.units.AngularVelocity;
import frc.lib.units.Current;

public final class MotorConstants {
    public static final AngularVelocity freeSpeed = RotationsPerMinute.of(7758.0);
    public static final Current freeCurrent = Amps.of(3.0);
    public static final Current stallCurrent = Amps.of(279.0);
    public static final MagneticFlux torqueConstant = Webers.of(0.01491);
    public static final MagneticFluxPerAngle backEmfConstant = WebersPerRadian.of(0.001491);
    public static final Torque loadTorque = NewtonMeters.of(0.0);
    public static final MomentOfInertia momentOfInertia = KilogramSquareMeters.of(0.016);
    public static final Resistance windingResistance = Volts.of(12.0).div(stallCurrent);
    public static final ActionPerAngle viscousFriction = freeCurrent.times(torqueConstant).div(freeSpeed);
    private MotorConstants() {}
}

package frc.lib.motor;

import static frc.lib.units.AngularVelocityUnit.RotationsPerMinute;
import static frc.lib.units.CurrentUnit.Amps;
import static frc.lib.units.MagneticFluxPerAngleUnit.WebersPerRadian;
import static frc.lib.units.MagneticFluxUnit.Webers;
import static frc.lib.units.MomentOfInertiaUnit.KilogramSquareMeters;
import static frc.lib.units.TorqueUnit.NewtonMeters;
import static frc.lib.units.VoltageUnit.Volts;

import frc.lib.units.ActionPerAngle;
import frc.lib.units.AngularVelocity;
import frc.lib.units.Current;
import frc.lib.units.MagneticFlux;
import frc.lib.units.MagneticFluxPerAngle;
import frc.lib.units.MomentOfInertia;
import frc.lib.units.Resistance;
import frc.lib.units.Torque;
import frc.lib.units.Voltage;

public final class MotorConstants {
    public static final Voltage MAX_VOLTAGE = Volts.of(12.0);
    public static final AngularVelocity FREE_SPEED = RotationsPerMinute.of(7758.0);
    public static final Current FREE_CURRENT = Amps.of(3.0);
    public static final Current STALL_CURRENT = Amps.of(279.0);
    public static final MagneticFlux TORQUE_CONSTANT = Webers.of(0.01491);
    public static final MagneticFluxPerAngle BACK_EMF_CONSTANT = WebersPerRadian.of(0.001491);
    public static final Torque LOAD_TORQUE = NewtonMeters.of(0.0);
    public static final MomentOfInertia MOMENT_OF_INERTIA = KilogramSquareMeters.of(0.016);
    public static final Resistance WINDING_RESISTANCE = MAX_VOLTAGE.div(STALL_CURRENT);
    public static final ActionPerAngle VISCOUS_FRICTION =
            FREE_CURRENT.times(TORQUE_CONSTANT).div(FREE_SPEED);

    private MotorConstants() {}
}

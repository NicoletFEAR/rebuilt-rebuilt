package com.nicoletfear.lib.motor;

import static com.nicoletfear.lib.units.AngularVelocityUnit.RotationsPerMinute;
import static com.nicoletfear.lib.units.CurrentUnit.Amps;
import static com.nicoletfear.lib.units.MagneticFluxPerAngleUnit.WebersPerRadian;
import static com.nicoletfear.lib.units.MagneticFluxUnit.Webers;
import static com.nicoletfear.lib.units.MomentOfInertiaUnit.KilogramSquareMeters;
import static com.nicoletfear.lib.units.TorqueUnit.NewtonMeters;
import static com.nicoletfear.lib.units.VoltageUnit.Volts;

import com.nicoletfear.lib.units.ActionPerAngle;
import com.nicoletfear.lib.units.AngularVelocity;
import com.nicoletfear.lib.units.Current;
import com.nicoletfear.lib.units.MagneticFlux;
import com.nicoletfear.lib.units.MagneticFluxPerAngle;
import com.nicoletfear.lib.units.MomentOfInertia;
import com.nicoletfear.lib.units.Resistance;
import com.nicoletfear.lib.units.Torque;
import com.nicoletfear.lib.units.Voltage;

/**
 * Physical constants of the motor model used for simulation.
 *
 * <p>These constants describe the motor's electrical and mechanical behavior (free speed and
 * current, stall current, torque and back-EMF constants, load torque, rotor inertia, winding
 * resistance, and viscous friction) and are used by the simulation motor model to compute simulated
 * acceleration, velocity, and position.
 */
public final class MotorConstants {

    /** Maximum voltage applied by the simulation motor model. */
    public static final Voltage MAX_VOLTAGE = Volts.of(12.0);

    /** No-load (free) speed of the motor. */
    public static final AngularVelocity FREE_SPEED = RotationsPerMinute.of(7758.0);

    /** Current drawn by the motor at no load. */
    public static final Current FREE_CURRENT = Amps.of(3.0);

    /** Current drawn by the motor at stall. */
    public static final Current STALL_CURRENT = Amps.of(279.0);

    /** Torque constant of the motor model. */
    public static final MagneticFlux TORQUE_CONSTANT = Webers.of(0.01491);

    /** Back-EMF constant of the motor model. */
    public static final MagneticFluxPerAngle BACK_EMF_CONSTANT = WebersPerRadian.of(0.001491);

    /** Load torque acting on the motor model; currently zero. */
    public static final Torque LOAD_TORQUE = NewtonMeters.of(0.0);

    /** Rotor moment of inertia of the motor model. */
    public static final MomentOfInertia MOMENT_OF_INERTIA = KilogramSquareMeters.of(0.016);

    /** Winding resistance, computed as {@link #MAX_VOLTAGE} divided by {@link #STALL_CURRENT}. */
    public static final Resistance WINDING_RESISTANCE = MAX_VOLTAGE.div(STALL_CURRENT);

    /**
     * Viscous friction, computed as {@link #FREE_CURRENT} times {@link #TORQUE_CONSTANT} divided by
     * {@link #FREE_SPEED}.
     */
    public static final ActionPerAngle VISCOUS_FRICTION =
            FREE_CURRENT.times(TORQUE_CONSTANT).div(FREE_SPEED);

    private MotorConstants() {}
}

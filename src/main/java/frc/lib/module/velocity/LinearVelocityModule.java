package frc.lib.module.velocity;

import static frc.lib.units.AngleUnit.Radians;
import static frc.lib.units.AngularVelocityUnit.RadiansPerSecond;
import static frc.lib.units.DistanceUnit.Meters;
import static frc.lib.units.VelocityUnit.MetersPerSecond;

import frc.lib.module.Module;
import frc.lib.motor.MotorIO;
import frc.lib.units.Distance;
import frc.lib.units.Velocity;

/**
 * A module that controls and measures the linear velocity of a mechanism.
 *
 * <p>This module extends {@link Module} and provides functionality for setting and retrieving the
 * mechanism's linear velocity.
 */
public final class LinearVelocityModule extends Module {

    /**
     * Creates a linear velocity module.
     *
     * @param name name used to identify and log this module
     * @param motor interface used to communicate with the motor
     */
    public LinearVelocityModule(String name, MotorIO motor) {
        super(name, motor);
    }

    /**
     * Sets the desired linear velocity of the mechanism for a known, static velocity.
     *
     * <p>The motor may follow a motion profile to reach the velocity. Use {@code setDynamicVelocity}
     * for velocities that change continuously.
     *
     * <p>The requested mechanism velocity is converted from meters per second to sensor angular
     * velocity before being sent to the motor.
     *
     * @param velocity desired mechanism linear velocity
     */
    public void setVelocitySetpoint(Velocity velocity) {
        motor.setVelocitySetpoint(RadiansPerSecond.of(velocity.in(MetersPerSecond)));
    }

    /**
     * Sets the desired linear velocity of the mechanism for a continuously changing velocity.
     *
     * <p>This variant is intended for velocity setpoints that change over time; the motor adjusts
     * without the delay of a motion profile. Use {@code setVelocitySetpoint} for known static
     * velocities.
     *
     * <p>The requested mechanism velocity is converted from meters per second to sensor angular
     * velocity before being sent to the motor.
     *
     * @param velocity desired mechanism linear velocity
     */
    public void setDynamicVelocity(Velocity velocity) {
        motor.setDynamicVelocity(RadiansPerSecond.of(velocity.in(MetersPerSecond)));
    }

    /**
     * Gets the current linear position of the mechanism.
     *
     * <p>The motor's rotor position is converted to meters.
     *
     * @return current mechanism position as a {@link Distance}
     */
    public Distance getPosition() {
        return Meters.of(inputs.position.in(Radians));
    }

    /**
     * Gets the current linear velocity of the mechanism.
     *
     * <p>The motor's rotor angular velocity is converted to meters per second.
     *
     * @return current mechanism linear velocity
     */
    public Velocity getVelocity() {
        return MetersPerSecond.of(inputs.velocity.in(RadiansPerSecond));
    }
}

package frc.lib.module.velocity;

import frc.lib.module.Module;
import frc.lib.motor.MotorIO;
import frc.lib.units.AngularVelocity;

/**
 * A module that controls and measures the angular velocity of a mechanism.
 *
 * <p>This module extends {@link Module} and provides functionality for setting and retrieving the
 * mechanism's angular velocity.
 */
public class AngularVelocityModule extends Module {

    /**
     * Creates an angular velocity module.
     *
     * @param name name used to identify and log this module
     * @param motor interface used to communicate with the motor
     */
    public AngularVelocityModule(String name, MotorIO motor) {
        super(name, motor);
    }

    /**
     * Sets the desired angular velocity of the mechanism for a known, static velocity.
     *
     * <p>The motor may follow a motion profile to reach the velocity. Use {@code setDynamicVelocity}
     * for velocities that change continuously.
     *
     * @param velocity desired mechanism angular velocity
     */
    public void setVelocitySetpoint(AngularVelocity velocity) {
        motor.setVelocitySetpoint(velocity);
    }

    /**
     * Sets the desired angular velocity of the mechanism for a continuously changing velocity.
     *
     * <p>This variant is intended for velocity setpoints that change over time; the motor adjusts
     * without the delay of a motion profile. Use {@code setVelocitySetpoint} for known static
     * velocities.
     *
     * @param velocity desired mechanism angular velocity
     */
    public void setDynamicVelocity(AngularVelocity velocity) {
        motor.setDynamicVelocity(velocity);
    }

    /**
     * Gets the current angular velocity of the mechanism.
     *
     * @return current mechanism angular velocity
     */
    public AngularVelocity getVelocity() {
        return inputs.velocity;
    }
}

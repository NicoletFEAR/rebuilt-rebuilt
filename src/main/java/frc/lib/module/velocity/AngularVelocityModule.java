package frc.lib.module.velocity;

import frc.lib.module.Module;
import frc.lib.motor.MotorIO;
import frc.lib.units.AngularVelocity;

/**
 * A module that controls and measures the angular velocity of a mechanism.
 *
 * <p>This module extends {@link Module} and provides functionality for setting and retrieving the
 * mechanism's angular velocity. Motor velocity is converted to mechanism velocity using the
 * configured rotor-to-mechanism ratio.
 */
public class AngularVelocityModule extends Module {

    /**
     * Creates an angular velocity module.
     *
     * @param name name used to identify and log this module
     * @param motor interface used to communicate with the motor
     * @param rotorToMechanismRatio ratio between the motor rotor and the mechanism
     */
    public AngularVelocityModule(String name, MotorIO motor, double rotorToMechanismRatio) {
        super(name, motor, rotorToMechanismRatio);
    }

    /**
     * Sets the desired angular velocity of the mechanism for a known, static velocity.
     * 
     * <p>The motor may follow a motion profile to reach the velocity. Use {@code setDynamicVelocity}
     * for velocities that change continuously.
     *
     * <p>The requested velocity is passed to the motor IO interface as the velocity setpoint using
     * the configured rotor-to-mechanism ratio.
     *
     * @param velocity desired mechanism angular velocity
     */
    public void setVelocitySetpoint(AngularVelocity velocity) {
        io.setVelocitySetpoint(velocity.times(rotorToMechanismRatio));
    }

    /**
     * Sets the desired angular velocity of the mechanism for a continuously changing velocity.
     * 
     * <p>This variant is intended for velocity setpoints that change over time; the motor adjusts
     * without the delay of a motion profile. Use {@code setVelocitySetpoint} for known static
     * velocities.
     *
     * <p>The requested velocity is passed to the motor IO interface as the velocity setpoint using
     * the configured rotor-to-mechanism ratio.
     *
     * @param velocity desired mechanism angular velocity
     */
    public void setDynamicVelocity(AngularVelocity velocity) {
        io.setDynamicVelocity(velocity.times(rotorToMechanismRatio));
    }

    /**
     * Gets the current angular velocity of the mechanism.
     *
     * <p>The motor's angular velocity is converted to mechanism velocity using the configured
     * rotor-to-mechanism ratio.
     *
     * @return current mechanism angular velocity
     */
    public AngularVelocity getVelocity() {
        return inputs.velocity.div(rotorToMechanismRatio);
    }
}

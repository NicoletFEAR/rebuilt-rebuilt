package frc.lib.module.velocity;

import edu.wpi.first.units.measure.AngularVelocity;
import frc.lib.module.Module;
import frc.lib.motor.MotorIO;

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
     * Sets the desired angular velocity of the mechanism.
     *
     * <p>The requested velocity is passed to the motor IO interface as the velocity setpoint.
     *
     * @param velocity desired mechanism angular velocity
     */
    protected void setVelocitySetpoint(AngularVelocity velocity) {
        io.setVelocitySetpoint(velocity);
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

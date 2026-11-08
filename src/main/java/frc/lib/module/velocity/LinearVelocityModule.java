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
 * mechanism's linear velocity. Motor rotor velocity is converted to mechanism velocity using the
 * configured rotor-to-mechanism ratio.
 */
public class LinearVelocityModule extends Module {

    /**
     * Creates a linear velocity module.
     *
     * @param name name used to identify and log this module
     * @param io interface used to communicate with the motor
     * @param rotorToMechanismRatio ratio between the motor rotor and the mechanism
     */
    public LinearVelocityModule(String name, MotorIO io, double rotorToMechanismRatio) {
        super(name, io, rotorToMechanismRatio);
    }

    /**
     * Sets the desired linear velocity of the mechanism.
     *
     * <p>The requested mechanism velocity is converted from meters per second to rotor angular
     * velocity using the configured rotor-to-mechanism ratio before being sent to the motor.
     *
     * @param velocity desired mechanism linear velocity
     */
    protected void setVelocitySetpoint(Velocity velocity) {
        io.setVelocitySetpoint(
                RadiansPerSecond.of(velocity.in(MetersPerSecond) * rotorToMechanismRatio));
    }

    public Distance getPosition() {
        return Meters.of(inputs.position.in(Radians) / rotorToMechanismRatio);
    }

    /**
     * Gets the current linear velocity of the mechanism.
     *
     * <p>The motor's rotor angular velocity is converted to meters per second using the configured
     * rotor-to-mechanism ratio.
     *
     * @return current mechanism linear velocity
     */
    public Velocity getVelocity() {
        return MetersPerSecond.of(inputs.velocity.in(RadiansPerSecond) / rotorToMechanismRatio);
    }
}

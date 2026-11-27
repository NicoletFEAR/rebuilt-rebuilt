package com.nicoletfear.lib.module.position;

import com.nicoletfear.lib.module.Module;
import com.nicoletfear.lib.motor.MotorIO;
import com.nicoletfear.lib.units.Angle;
import org.wpilib.math.geometry.Rotation2d;

/**
 * A module that controls and measures the angular position of a mechanism.
 *
 * <p>This module extends {@link Module} and provides functionality for setting and retrieving the
 * mechanism's angular position.
 */
public class AngularPositionModule extends Module {

    /**
     * Creates an angular position module.
     *
     * @param name name used to identify and log this module
     * @param motor interface used to communicate with the motor
     */
    public AngularPositionModule(String name, MotorIO motor) {
        super(name, motor);
    }

    /**
     * Sets the desired angular position of the mechanism for a known static setpoint.
     *
     * <p>The motor may follow a motion profile to reach the setpoint. Use {@code setDynamicPosition}
     * for setpoints that change continuously.
     *
     * <p>The requested mechanism position is converted to rotor position using the configured
     * rotor-to-mechanism ratio before being sent to the motor.
     *
     * @param position desired mechanism position
     */
    public void setPositionSetpoint(Angle position) {
        motor.setPositionSetpoint(position);
    }

    /**
     * Sets the desired angular position of the mechanism for a continuously changing setpoint.
     *
     * <p>This variant is intended for setpoints that change over time; the motor adjusts without the
     * delay of a motion profile. Use {@code setPositionSetpoint} for known static setpoints.
     *
     * <p>The requested mechanism position is converted to rotor position using the configured
     * rotor-to-mechanism ratio before being sent to the motor.
     *
     * @param position desired mechanism position
     */
    public void setDynamicPosition(Angle position) {
        motor.setDynamicPosition(position);
    }

    /**
     * Gets the current angular position of the mechanism.
     *
     * <p>The motor's rotor position is converted to mechanism position using the configured
     * rotor-to-mechanism ratio.
     *
     * @return current mechanism position as an {@link Angle}
     */
    public Angle getPosition() {
        return inputs.position;
    }

    /**
     * Gets the current angular position of the mechanism as a {@link Rotation2d}.
     *
     * @return current mechanism rotation
     */
    public Rotation2d getRotation() {
        return getPosition().toRotation();
    }
}

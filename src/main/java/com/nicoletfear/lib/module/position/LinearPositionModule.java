package com.nicoletfear.lib.module.position;

import static com.nicoletfear.lib.units.AngleUnit.Radians;
import static com.nicoletfear.lib.units.DistanceUnit.Meters;

import com.nicoletfear.lib.module.Module;
import com.nicoletfear.lib.motor.MotorIO;
import com.nicoletfear.lib.units.Distance;

/**
 * A module that controls and measures the linear position of a mechanism.
 *
 * <p>This module extends {@link Module} and provides functionality for setting and retrieving the
 * mechanism's linear position.
 */
public class LinearPositionModule extends Module {

    /**
     * Creates a linear position module.
     *
     * @param name name used to identify and log this module
     * @param motor interface used to communicate with the motor
     */
    public LinearPositionModule(String name, MotorIO motor) {
        super(name, motor);
    }

    /**
     * Sets the desired linear position of the mechanism for a known static setpoint.
     *
     * <p>The motor may follow a motion profile to reach the setpoint. Use {@code setDynamicPosition}
     * for setpoints that change continuously.
     *
     * <p>The requested mechanism position is converted from meters to sensor position before being
     * sent to the motor.
     *
     * @param position desired mechanism position
     */
    public void setPositionSetpoint(Distance position) {
        motor.setPositionSetpoint(Radians.of(position.in(Meters)));
    }

    /**
     * Sets the desired linear position of the mechanism for a continuously changing setpoint.
     *
     * <p>This variant is intended for setpoints that change over time; the motor adjusts without the
     * delay of a motion profile. Use {@code setPositionSetpoint} for known static setpoints.
     *
     * <p>The requested mechanism position is converted from meters to sensor position before being
     * sent to the motor.
     *
     * @param position desired mechanism position
     */
    public void setDynamicPosition(Distance position) {
        motor.setDynamicPosition(Radians.of(position.in(Meters)));
    }

    /**
     * Gets the current linear position of the mechanism.
     *
     * <p>The motor's sensor position is converted to meters.
     *
     * @return current mechanism position as a {@link Distance}
     */
    public Distance getPosition() {
        return Meters.of(inputs.position.in(Radians));
    }
}

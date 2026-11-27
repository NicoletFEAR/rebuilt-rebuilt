package com.nicoletfear.lib;

import static com.nicoletfear.lib.units.AngleUnit.Radians;
import static com.nicoletfear.lib.units.DistanceUnit.Meters;

import com.nicoletfear.lib.units.Angle;
import com.nicoletfear.lib.units.Distance;

/** This file houses all of the general constants that are needed to run the robot. */
public final class ToleranceConstants {

    /**
     * Sets the tolerance of a velocity mechanism to be within 0.05*setpoint of the setpoint. This
     * works because as speed grows higher, precision becomes less important, and demanding too much
     * precision at high speeds can be detrimental.
     */
    public static final double VELOCITY_SETPOINT_TOLERANCE_MULTIPLIER = 0.05;

    /**
     * Sets the tolerance of an angular position mechanism to be within 0.05 radians of the setpoint.
     */
    public static final Angle ANGULAR_POSITION_SETPOINT_TOLERANCE = Radians.of(0.05);

    /**
     * Sets the tolerance of a linear position mechanism to be within 0.005 meters of the setpoint.
     */
    public static final Distance LINEAR_POSITION_SETPOINT_TOLERANCE = Meters.of(0.005);

    /**
     * Minimum distance between two doubles such that they should not be considered equal. For real life,
     * tolerances are usually greater than this, so this is just for doubles that don't represent anything
     * physical.
     */
    public static final double EPSILON = 1e-12;

    private ToleranceConstants() {
        // Keep this constructor empty.
    }
}

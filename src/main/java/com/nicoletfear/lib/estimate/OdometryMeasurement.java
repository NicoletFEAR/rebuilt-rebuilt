package com.nicoletfear.lib.estimate;

import com.nicoletfear.lib.units.Timestamp;
import org.wpilib.math.geometry.Pose2d;

/**
 * A field-relative pose of the robot together with the timestamp at which it was measured.
 *
 * @param timestamp time at which the measurement was taken
 * @param pose field-relative robot pose at the time of the measurement
 */
public record OdometryMeasurement(Timestamp timestamp, Pose2d pose) {}

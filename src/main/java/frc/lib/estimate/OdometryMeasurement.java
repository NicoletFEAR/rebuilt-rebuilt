package frc.lib.estimate;

import org.wpilib.math.geometry.Pose2d;
import org.wpilib.units.measure.Time;

public record OdometryMeasurement(Time timestamp, Pose2d pose) {}

package frc.lib.estimate;

import org.wpilib.math.geometry.Pose2d;

import frc.lib.units.Time;

public record OdometryMeasurement(Time timestamp, Pose2d pose) {}

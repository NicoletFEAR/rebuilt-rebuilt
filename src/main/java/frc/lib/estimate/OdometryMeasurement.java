package frc.lib.estimate;

import frc.lib.units.Time;
import org.wpilib.math.geometry.Pose2d;

public record OdometryMeasurement(Time timestamp, Pose2d pose) {}

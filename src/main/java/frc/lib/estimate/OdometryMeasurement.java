package frc.lib.estimate;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.units.measure.Time;

public record OdometryMeasurement(Time timestamp, Pose2d pose) {}

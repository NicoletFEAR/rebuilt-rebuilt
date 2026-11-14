package frc.lib.estimate;

import frc.lib.units.Timestamp;
import org.wpilib.math.geometry.Pose2d;

public record OdometryMeasurement(Timestamp timestamp, Pose2d pose) {}

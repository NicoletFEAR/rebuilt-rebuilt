package frc.lib.estimate;

import static frc.lib.units.TimeUnit.Seconds;
import static org.junit.jupiter.api.Assertions.assertEquals;

import frc.lib.units.Timestamp;
import org.junit.jupiter.api.Test;
import org.wpilib.math.geometry.Pose2d;
import org.wpilib.math.geometry.Rotation2d;

class OdometryMeasurementTest {
    @Test
    void storesTimestampAndPose() {
        Timestamp timestamp = Seconds.timestamp(1.5);
        Pose2d pose = new Pose2d(1.0, 2.0, Rotation2d.fromRadians(0.25));

        OdometryMeasurement measurement = new OdometryMeasurement(timestamp, pose);

        assertEquals(timestamp, measurement.timestamp());
        assertEquals(pose, measurement.pose());
    }

    @Test
    void recordEqualityUsesTimestampAndPose() {
        Timestamp timestamp = Seconds.timestamp(1.5);
        Pose2d pose = new Pose2d(1.0, 2.0, Rotation2d.fromRadians(0.25));

        OdometryMeasurement first = new OdometryMeasurement(timestamp, pose);
        OdometryMeasurement same = new OdometryMeasurement(timestamp, pose);
        OdometryMeasurement different =
                new OdometryMeasurement(Seconds.timestamp(2.0), pose);

        assertEquals(first, same);
        assertEquals(first.hashCode(), same.hashCode());
        org.junit.jupiter.api.Assertions.assertNotEquals(first, different);
    }
}

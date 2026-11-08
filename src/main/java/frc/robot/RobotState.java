package frc.robot;

import frc.lib.estimate.OdometryMeasurement;
import frc.lib.units.Time;
import lombok.Data;
import org.littletonrobotics.junction.AutoLog;
import org.wpilib.math.geometry.Pose2d;

import static frc.lib.units.TimeUnit.Seconds;

@Data
@AutoLog
public class RobotState {
    Pose2d estimatedPose = Pose2d.ZERO;
    Time lastTimestamp = Seconds.of(Double.MIN_NORMAL);

    public void addOdometryMeasurement(OdometryMeasurement measurement) {
        if (measurement.timestamp().greaterThan(lastTimestamp)) {
            lastTimestamp = measurement.timestamp();
            estimatedPose = measurement.pose();
        }
    }
}

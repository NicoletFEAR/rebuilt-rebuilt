package frc.robot;

import static edu.wpi.first.units.Units.Seconds;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.units.measure.Time;
import frc.lib.estimate.OdometryMeasurement;
import lombok.Data;
import org.littletonrobotics.junction.AutoLog;

@Data
@AutoLog
public class RobotState {
    Pose2d estimatedPose = Pose2d.kZero;
    Time lastTimestamp = Seconds.of(Double.MIN_NORMAL);

    public void addOdometryMeasurement(OdometryMeasurement measurement) {
        if (measurement.timestamp().gt(lastTimestamp)) {
            lastTimestamp = measurement.timestamp();
            estimatedPose = measurement.pose();
        }
    }
}

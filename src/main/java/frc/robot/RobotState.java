package frc.robot;

import static org.wpilib.units.Units.Seconds;

import frc.lib.estimate.OdometryMeasurement;
import lombok.Data;
import org.littletonrobotics.junction.AutoLog;
import org.wpilib.math.geometry.Pose2d;
import org.wpilib.units.measure.Time;

@Data
@AutoLog
public class RobotState {
    Pose2d estimatedPose = Pose2d.ZERO;
    Time lastTimestamp = Seconds.of(Double.MIN_NORMAL);

    public void addOdometryMeasurement(OdometryMeasurement measurement) {
        if (measurement.timestamp().gt(lastTimestamp)) {
            lastTimestamp = measurement.timestamp();
            estimatedPose = measurement.pose();
        }
    }
}

package frc.robot;

import static frc.lib.units.TimeUnit.Seconds;

import frc.lib.estimate.OdometryMeasurement;
import frc.lib.units.Timestamp;
import lombok.Data;
import org.littletonrobotics.junction.Logger;
import org.wpilib.math.geometry.Pose2d;

@Data
public class RobotState {
    Pose2d estimatedPose = Pose2d.ZERO;
    Timestamp lastTimestamp = Seconds.timestamp(0.0);

    public void addOdometryMeasurement(OdometryMeasurement measurement) {
        if (measurement.timestamp().after(lastTimestamp)) {
            lastTimestamp = measurement.timestamp();
            estimatedPose = measurement.pose();
        }
    }

    public void log() {
        Logger.recordOutput("Robot State/Estimated Pose", estimatedPose);
        lastTimestamp.log("Robot State/Last Timestamp");
    }
}

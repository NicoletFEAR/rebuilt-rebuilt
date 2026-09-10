package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import lombok.Data;
import org.littletonrobotics.junction.AutoLog;

@Data
@AutoLog
public class RobotState {
    Pose2d estimatedPosition = Pose2d.kZero;
}

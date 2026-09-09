package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import lombok.Data;

@Data
public class RobotState {
    private Pose2d estimatedPosition = Pose2d.kZero;
}

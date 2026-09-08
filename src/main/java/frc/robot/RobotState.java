package frc.robot;

import edu.wpi.first.math.geometry.Transform3d;
import lombok.Data;

@Data
public class RobotState {
    private Transform3d estimatedPosition;
}

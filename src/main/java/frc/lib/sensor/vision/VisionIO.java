package frc.lib.sensor.vision;

import org.littletonrobotics.junction.AutoLog;
import org.wpilib.math.geometry.Pose2d;

public interface VisionIO {
    @AutoLog
    public static class VisionInputs {
        public boolean connected = false;
        public boolean hasValidPose = false;
        public Pose2d extimatPose2d = new Pose2d();
    }

    void updateInputs(VisionInputs inputs);
}

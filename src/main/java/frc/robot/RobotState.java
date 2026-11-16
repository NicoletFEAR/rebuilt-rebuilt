package frc.robot;

import static frc.lib.units.TimeUnit.Seconds;

import frc.lib.estimate.OdometryMeasurement;
import frc.lib.units.Timestamp;
import org.littletonrobotics.junction.Logger;
import org.wpilib.math.geometry.Pose2d;

/**
 * Holds the robot's estimated field-relative pose and the timestamp of the measurement that
 * produced it.
 *
 * <p>Instances of this class are shared between the drive subsystem (which produces odometry
 * measurements) and other consumers of the robot's pose. Lombok's {@link Data} annotation provides
 * the getters and setters for {@link #estimatedPose} and {@link #lastTimestamp}.
 */
public final class RobotState {

    /** Estimated field-relative pose of the robot; starts at the origin. */
    private Pose2d estimatedPose = Pose2d.ZERO;

    /** Timestamp of the odometry measurement that produced {@link #estimatedPose}. */
    private Timestamp lastTimestamp = Seconds.timestamp(0.0);

    /**
     * Records an odometry measurement.
     *
     * <p>The measurement is applied only if its timestamp is after {@link #lastTimestamp}; older
     * measurements are ignored.
     *
     * @param measurement the odometry measurement to record
     */
    public void addOdometryMeasurement(OdometryMeasurement measurement) {
        if (measurement.timestamp().after(lastTimestamp)) {
            lastTimestamp = measurement.timestamp();
            estimatedPose = measurement.pose();
        }
    }

    /** Logs the estimated pose and the last odometry timestamp. */
    public void log() {
        Logger.recordOutput("Robot State/Estimated Pose", estimatedPose);
        lastTimestamp.log("Robot State/Last Timestamp");
    }
}

package com.nicoletfear.robot;

import static com.nicoletfear.lib.units.TimeUnit.Seconds;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.nicoletfear.lib.estimate.OdometryMeasurement;
import com.nicoletfear.lib.units.Timestamp;
import java.lang.reflect.Field;
import org.junit.jupiter.api.Test;
import org.wpilib.math.geometry.Pose2d;
import org.wpilib.math.geometry.Rotation2d;

class RobotStateTest {
    private static final Pose2d FIRST_POSE = new Pose2d(1.0, 2.0, Rotation2d.fromRadians(0.25));
    private static final Pose2d SECOND_POSE = new Pose2d(3.0, 4.0, Rotation2d.fromRadians(0.5));

    private static Pose2d estimatedPose(RobotState state) throws Exception {
        Field field = RobotState.class.getDeclaredField("estimatedPose");
        field.setAccessible(true);
        return (Pose2d) field.get(state);
    }

    private static Timestamp lastTimestamp(RobotState state) throws Exception {
        Field field = RobotState.class.getDeclaredField("lastTimestamp");
        field.setAccessible(true);
        return (Timestamp) field.get(state);
    }

    @Test
    void startsAtTheOriginWithZeroTimestamp() throws Exception {
        RobotState state = new RobotState();

        assertEquals(Pose2d.ZERO, estimatedPose(state));
        assertEquals(0.0, lastTimestamp(state).since(Seconds.timestamp(0.0)).in(Seconds), 1e-9);
    }

    @Test
    void recordsNewerMeasurement() throws Exception {
        RobotState state = new RobotState();

        state.addOdometryMeasurement(new OdometryMeasurement(Seconds.timestamp(1.0), FIRST_POSE));

        assertEquals(FIRST_POSE, estimatedPose(state));
        assertEquals(1.0, lastTimestamp(state).since(Seconds.timestamp(0.0)).in(Seconds), 1e-9);
    }

    @Test
    void ignoresOlderMeasurement() throws Exception {
        RobotState state = new RobotState();
        state.addOdometryMeasurement(new OdometryMeasurement(Seconds.timestamp(2.0), FIRST_POSE));

        state.addOdometryMeasurement(new OdometryMeasurement(Seconds.timestamp(1.0), SECOND_POSE));

        assertEquals(FIRST_POSE, estimatedPose(state));
        assertEquals(2.0, lastTimestamp(state).since(Seconds.timestamp(0.0)).in(Seconds), 1e-9);
    }

    @Test
    void ignoresMeasurementWithTheSameTimestamp() throws Exception {
        RobotState state = new RobotState();
        state.addOdometryMeasurement(new OdometryMeasurement(Seconds.timestamp(2.0), FIRST_POSE));

        state.addOdometryMeasurement(new OdometryMeasurement(Seconds.timestamp(2.0), SECOND_POSE));

        assertEquals(FIRST_POSE, estimatedPose(state));
    }

    @Test
    void acceptsEachNewerMeasurement() throws Exception {
        RobotState state = new RobotState();

        state.addOdometryMeasurement(new OdometryMeasurement(Seconds.timestamp(1.0), FIRST_POSE));
        state.addOdometryMeasurement(new OdometryMeasurement(Seconds.timestamp(2.0), SECOND_POSE));

        assertEquals(SECOND_POSE, estimatedPose(state));
        assertEquals(2.0, lastTimestamp(state).since(Seconds.timestamp(0.0)).in(Seconds), 1e-9);
    }

    @Test
    void ignoresNegativeTimestampsBeforeTheInitialTimestamp() throws Exception {
        RobotState state = new RobotState();

        state.addOdometryMeasurement(new OdometryMeasurement(Seconds.timestamp(-1.0), FIRST_POSE));

        assertEquals(Pose2d.ZERO, estimatedPose(state));
    }

    @Test
    void loggingIsSafeWithoutAdvantageKitRunning() {
        RobotState state = new RobotState();
        state.addOdometryMeasurement(new OdometryMeasurement(Seconds.timestamp(1.0), FIRST_POSE));

        assertDoesNotThrow(state::log);
    }
}

package com.nicoletfear.lib.sensor.vision;

import org.littletonrobotics.junction.AutoLog;
import org.wpilib.math.geometry.Pose2d;

/**
 * Provides an interface for communicating with a camera, like a Limelight or
 * a PhotonVision.
 */
public interface VisionIO {

    /**
     * Stores the information reported by a camera.
     */
    @AutoLog
    public static class VisionInputs {

        /** Whether the camera is connected. */
        public boolean connected = false;

        /** Whether the pose sent by the camera can be trusted (or maybe whether it exists at all; ask Soukarno). */
        public boolean hasValidPose = false;

        /** The pose the camera thinks the robot is at. */
        public Pose2d estimatedPose = new Pose2d();
    }

    /**
     * Updates the provided inputs with the camera's current status.
     *
     * <p>This is done instead of simply returning a new inputs object so that we can reuse inputs
     * objects, and don't have to waste time each loop period allocating a bunch of new inputs
     * objects.
     *
     * @param inputs object that should be populated with the camera's current inputs
     */
    void updateInputs(VisionInputs inputs);
}

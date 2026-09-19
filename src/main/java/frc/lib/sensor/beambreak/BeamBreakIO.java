package frc.lib.sensor.beambreak;

import org.littletonrobotics.junction.AutoLog;

/**
 * Provides an interface for communicating with a beam break sensor.
 *
 * <p>{@code BeamBreakIO} abstracts the hardware-specific implementation of a beam break sensor.
 * This allows the rest of the robot code to interact with the sensor without depending on a
 * specific hardware implementation.
 *
 * <p>Implementations of this interface are responsible for updating the sensor's connection status
 * and whether the beam is currently tripped.
 */
public interface BeamBreakIO {

    /**
     * Stores the inputs and status information reported by a beam break sensor.
     *
     * <p>This class is annotated with {@link AutoLog}, allowing AdvantageKit to automatically
     * generate a {@code BeamBreakIOInputsAutoLogged} class for logging these inputs.
     */
    @AutoLog
    public static class BeamBreakIOInputs {

        /** Whether the beam break sensor is currently connected. */
        public boolean connected = false;

        /** Whether the beam break sensor is currently tripped. */
        public boolean tripped = false;
    }

    /**
     * Updates the provided inputs with the beam break sensor's current status.
     *
     * @param inputs object that should be populated with the sensor's current inputs
     */
    void updateInputs(BeamBreakIOInputs inputs);
}

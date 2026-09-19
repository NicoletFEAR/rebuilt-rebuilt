package frc.lib.sensor.tof;

import static edu.wpi.first.units.Units.Meters;

import edu.wpi.first.units.measure.Distance;
import org.littletonrobotics.junction.AutoLog;

/**
 * Provides an interface for communicating with a time-of-flight sensor.
 *
 * <p>{@code TofIO} abstracts the hardware-specific implementation of a time-of-flight sensor. This
 * allows the rest of the robot code to interact with the sensor without depending on a specific
 * hardware implementation.
 *
 * <p>Implementations of this interface are responsible for updating the sensor's connection status
 * and measured distance.
 */
public interface TofIO {

    /**
     * Stores the inputs and status information reported by a time-of-flight sensor.
     *
     * <p>This class is annotated with {@link AutoLog}, allowing AdvantageKit to automatically
     * generate a {@code TofIOInputsAutoLogged} class for logging these inputs.
     */
    @AutoLog
    public static class TofIOInputs {

        /** Whether the time-of-flight sensor is currently connected. */
        public boolean connected = false;

        /** Current distance measured by the sensor. */
        public Distance distance = Meters.of(0.0);
    }

    /**
     * Updates the provided inputs with the time-of-flight sensor's current status and measured
     * distance.
     *
     * @param input object that should be populated with the sensor's current inputs
     */
    void updateInputs(TofIOInputs input);
}

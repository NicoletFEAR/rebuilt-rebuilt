package frc.lib.sensor.tof;

import static edu.wpi.first.units.Units.Millimeters;

import com.playingwithfusion.TimeOfFlight;
import frc.lib.CanId;

/**
 * Real hardware implementation of {@link TofIO} using a Playing With Fusion time-of-flight sensor.
 *
 * <p>This class provides an interface between the library's time-of-flight sensor abstraction and a
 * physical {@link TimeOfFlight} sensor. It retrieves the measured distance from the sensor and
 * provides it to the rest of the robot code through {@link TofIOInputs}.
 *
 * <p>The distance returned by the Playing With Fusion sensor is converted from millimeters into a
 * WPILib {@link edu.wpi.first.units.measure.Distance}.
 */
public class CanTof implements TofIO {

    /** Physical Playing With Fusion time-of-flight sensor. */
    public final TimeOfFlight tof;

    /**
     * Creates a time-of-flight sensor using the specified CAN ID.
     *
     * @param id CAN ID and CAN bus of the time-of-flight sensor
     */
    public CanTof(CanId id) {
        tof = new TimeOfFlight(id.id());
    }

    /**
     * Updates the provided inputs with the current state of the time-of-flight sensor.
     *
     * <p>The sensor is reported as connected, and its measured distance is converted from millimeters
     * into a WPILib {@code Distance}.
     *
     * @param inputs object that should be populated with the sensor's current inputs
     */
    @Override
    public void updateInputs(TofIOInputs inputs) {
        inputs.connected = true;
        inputs.distance = Millimeters.of(tof.getRange());
    }
}

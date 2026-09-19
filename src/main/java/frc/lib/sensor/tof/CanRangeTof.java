package frc.lib.sensor.tof;

import static edu.wpi.first.units.Units.Hertz;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.hardware.CANrange;
import edu.wpi.first.units.measure.Distance;

/**
 * Real hardware implementation of {@link TofIO} using a CTRE CANrange sensor.
 * 
 * <p>
 * This class provides an interface between the library's time-of-flight
 * sensor abstraction and a physical CANrange sensor. It retrieves the
 * distance measured by the sensor and reports whether the sensor is connected.
 * 
 * <p>
 * The distance signal is updated at 100 Hz, and CAN bus utilization is
 * optimized to reduce unnecessary CAN bus traffic.
 */
public class CanRangeTof implements TofIO {

    /** Physical CANrange sensor. */
    private final CANrange canRange;

    /** Status signal containing the distance measured by the CANrange. */
    private final StatusSignal<Distance> distanceSignal;

    /**
     * Creates a CANrange time-of-flight sensor using the provided configuration.
     * 
     * <p>
     * The CANrange is configured using the values provided by the
     * {@link CanRangeTofConfig}. The distance signal is configured to update at
     * 100 Hz.
     * 
     * @param config configuration for the CANrange sensor
     */
    public CanRangeTof(CanRangeTofConfig config) {
        canRange = new CANrange(config.id().id(), config.id().bus());
        canRange.getConfigurator().apply(config.getCanRangeConfiguration());

        distanceSignal = canRange.getDistance();
        distanceSignal.setUpdateFrequency(Hertz.of(100));
        canRange.optimizeBusUtilization();
    }

    /**
     * Updates the provided inputs with the CANrange's current status.
     * 
     * <p>
     * The distance signal is refreshed, and the sensor's connection status and
     * measured distance are stored in the provided inputs object.
     * 
     * @param inputs object that should be populated with the sensor's current
     *               inputs
     */
    @Override
    public void updateInputs(TofIOInputs inputs) {
        distanceSignal.refresh();
        inputs.connected = StatusSignal.isAllGood();
        inputs.distance = distanceSignal.getValue();
    }
}

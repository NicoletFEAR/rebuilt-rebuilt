package frc.lib.sensor.beambreak;

import static edu.wpi.first.units.Units.Hertz;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.hardware.CANrange;
import frc.lib.sensor.CanRangeConfig;

/**
 * Real hardware implementation of {@link BeamBreakIO} using a CTRE CANrange sensor.
 *
 * <p>This class provides an interface between the library's beam break abstraction and a physical
 * CANrange sensor. It retrieves the sensor's detection status and reports whether the sensor is
 * connected and whether the beam is currently tripped.
 *
 * <p>The detection status is updated at 100 Hz to provide frequent sensor updates while optimizing
 * CAN bus utilization.
 */
public class CanRangeBeamBreak implements BeamBreakIO {

    /** Physical CANrange sensor. */
    private final CANrange canRange;

    /** Status signal indicating whether the CANrange has detected an object. */
    private final StatusSignal<Boolean> trippedSignal;

    /**
     * Creates a CANrange beam break sensor using the provided CAN ID.
     *
     * <p>The sensor's detection status is configured to update at 100 Hz, and CAN bus utilization is
     * optimized.
     *
     * @param config Configuration for the CANrange sensor
     */
    public CanRangeBeamBreak(CanRangeConfig config) {
        canRange = new CANrange(config.id().id());
        canRange.getConfigurator().apply(config.getCanRangeConfiguration());

        trippedSignal = canRange.getIsDetected();
        trippedSignal.setUpdateFrequency(Hertz.of(100));
        canRange.optimizeBusUtilization();
    }

    /**
     * Updates the provided inputs with the CANrange sensor's current status.
     *
     * <p>The detection status is refreshed and the connection status and tripped state are stored in
     * the provided inputs object.
     *
     * @param inputs object to populate with the sensor's current inputs
     */
    @Override
    public void updateInputs(BeamBreakIOInputs inputs) {
        trippedSignal.refresh();
        inputs.connected = BaseStatusSignal.isAllGood(trippedSignal);
        inputs.tripped = trippedSignal.getValue();
    }
}

package frc.lib.sensor.tof;

/**
 * Simulation implementation of {@link CanRangeTof} for a CANrange time-of-flight sensor.
 *
 * <p>This class extends {@link CanRangeTof} so that it can be used anywhere a CANrange
 * time-of-flight sensor is expected. During simulation, the sensor is reported as disconnected
 * because there is no physical CANrange sensor connected.
 */
public class SimCanRangeTof extends CanRangeTof {
    /**
     * Creates a simulated CANrange time-of-flight sensor.
     *
     * @param config configuration for the simulated CANrange sensor
     */
    public SimCanRangeTof(CanRangeTofConfig config) {
        super(config);
    }

    /**
     * Updates the simulated time-of-flight sensor inputs.
     *
     * <p>The simulated sensor is reported as disconnected because no physical CANrange sensor is
     * present during simulation.
     *
     * @param inputs object that should be populated with the sensor's current inputs
     */
    @Override
    public void updateInputs(TofIOInputs inputs) {
        inputs.connected = false;
    }
}

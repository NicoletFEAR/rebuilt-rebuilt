package frc.lib.sensor.beambreak;

/**
 * Simulation implementation of {@link BeamBreak} for a beam break sensor.
 *
 * <p>This class extends {@link BeamBreak} so that it can be used anywhere a normal beam break
 * sensor is expected. During simulation, the sensor is reported as disconnected.
 */
public class SimBeamBreak extends BeamBreak {

    /**
     * Creates a simulated beam break sensor.
     *
     * @param id digital input port associated with the simulated beam break sensor
     */
    public SimBeamBreak(int id) {
        super(id);
    }

    /**
     * Updates the simulated beam break sensor inputs.
     *
     * <p>The simulated sensor is reported as disconnected because no physical beam break sensor is
     * connected during simulation.
     *
     * @param inputs object that should be populated with the sensor's current inputs
     */
    @Override
    public void updateInputs(BeamBreakIOInputs inputs) {
        inputs.connected = false;
    }
}

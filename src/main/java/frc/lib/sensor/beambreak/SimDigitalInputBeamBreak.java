package frc.lib.sensor.beambreak;

/**
 * Simulation implementation of {@link DigitalInputBeamBreak} for a beam break sensor.
 *
 * <p>This class extends {@link DigitalInputBeamBreak} so that it can be used anywhere a normal beam
 * break sensor is expected. During simulation, the sensor is reported as disconnected.
 */
public final class SimDigitalInputBeamBreak extends DigitalInputBeamBreak {

    /**
     * Creates a simulated beam break sensor.
     *
     * @param config configuration for the beam break
     */
    public SimDigitalInputBeamBreak(BeamBreakConfig config) {
        super(config);
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

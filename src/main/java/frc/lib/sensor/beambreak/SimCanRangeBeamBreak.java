package frc.lib.sensor.beambreak;

import frc.lib.CanId;

/**
 * Simulation implementation of {@link CanRangeBeamBreak} for a CANrange
 * beam break sensor.
 * 
 * <p>
 * This class extends {@link CanRangeBeamBreak} so that it can be used
 * anywhere a CANrange beam break sensor is expected. During simulation, the
 * sensor is reported as disconnected because there is no physical CANrange
 * sensor connected.
 */
public class SimCanRangeBeamBreak extends CanRangeBeamBreak {

    /**
     * Creates a simulated CANrange beam break sensor.
     * 
     * @param id CAN ID and CAN bus of the simulated CANrange sensor
     */
    public SimCanRangeBeamBreak(CanId id) {
        super(id);
    }

    /**
     * Updates the simulated beam break sensor inputs.
     * 
     * <p>
     * The simulated sensor is reported as disconnected because no physical
     * CANrange sensor is present during simulation.
     * 
     * @param inputs object that should be populated with the sensor's current
     *               inputs
     */
    @Override
    public void updateInputs(BeamBreakIOInputs inputs) {
        inputs.connected = false;
    }
}

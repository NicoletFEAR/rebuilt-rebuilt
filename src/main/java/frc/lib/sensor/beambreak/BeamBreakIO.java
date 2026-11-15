package frc.lib.sensor.beambreak;

import frc.lib.sensor.CanRangeConfig;
import frc.robot.Robot;
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
     * Creates a real CANrange beam break or a simulated CANrange beam break depending on whether the
     * robot is being simulated or not.
     *
     * @param config configuration for the CANrange beam break
     * @return the correct CANrange beam break implementation
     */
    static CanRangeBeamBreak buildCanRangeBeamBreak(CanRangeConfig config) {
        return Robot.isReal() ? new CanRangeBeamBreak(config) : new SimCanRangeBeamBreak(config);
    }

    /**
     * Creates a real beam break or a simulated beam break depending on whether the robot is being
     * simulated or not.
     *
     * @param id digital input ID of the beam break
     * @return the correct beam break implementation
     */
    static BeamBreak buildBeamBreak(int id) {
        return Robot.isReal() ? new BeamBreak(id) : new SimBeamBreak(id);
    }

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
     * Updates the provided inputs with the beam break sensor's current status. This is done instead
     * of simply returning a new inputs object so that we can reuse inputs objects, and don't have to
     * waste time each loop period allocating a bunch of new inputs objects.
     *
     * @param inputs object that should be populated with the sensor's current inputs
     */
    void updateInputs(BeamBreakIOInputs inputs);
}

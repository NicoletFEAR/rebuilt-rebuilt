package frc.lib.sensor.tof;

import static frc.lib.units.DistanceUnit.Meters;

import frc.lib.sensor.CanRangeConfig;
import frc.lib.units.Distance;
import frc.robot.Robot;
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
     * Creates a real CANrange time-of-flight sensor or a simulated CANrange time-of-flight sensor
     * depending on whether the robot is being simulated or not.
     *
     * @param config configuration for the CANrange time-of-flight sensor
     * @return the correct CANrange time-of-flight sensor implementation
     */
    static CanRangeTof buildCanRangeTof(CanRangeConfig config) {
        return Robot.isReal() ? new CanRangeTof(config) : new SimCanRangeTof(config);
    }

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
     * <p>This is done instead of simply returning a new inputs object so that we can reuse inputs
     * objects, and don't have to waste time each loop period allocating a bunch of new inputs
     * objects.
     *
     * @param input object that should be populated with the sensor's current inputs
     */
    void updateInputs(TofIOInputs input);
}

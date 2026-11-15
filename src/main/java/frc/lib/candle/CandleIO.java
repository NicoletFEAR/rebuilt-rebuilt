package frc.lib.candle;

import org.littletonrobotics.junction.AutoLog;

/**
 * Interface for communicating with an LED CANdle controller.
 *
 * <p>Currently only defines the logged inputs for a CANdle; no control methods are declared yet.
 */
public interface CandleIO {

    /**
     * Stores the inputs and status reported by a CANdle controller.
     *
     * <p>This class is annotated with {@link AutoLog}, allowing AdvantageKit to automatically
     * generate a {@code CandleIOInputsAutoLogged} class for logging these inputs.
     */
    @AutoLog
    public static class CandleIOInputs {

        /** Whether the CANdle controller is currently connected. */
        public boolean connected = false;
    }
}

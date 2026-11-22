package frc.lib.led;

import static frc.lib.units.TemperatureUnit.Kelvin;
import static frc.lib.units.VoltageUnit.Volts;

import frc.lib.units.Temperature;
import frc.lib.units.Voltage;
import org.littletonrobotics.junction.AutoLog;
import org.wpilib.util.Color;

/**
 * Interface for communicating with an LED controller.
 *
 * <p>Currently only defines the logged inputs; no control methods are declared yet.
 */
public interface LedIO {

    /**
     * Stores the inputs and status reported by an LED controller.
     *
     * <p>This class is annotated with {@link AutoLog}, allowing AdvantageKit to automatically
     * generate an {@code LedIOInputsAutoLogged} class for logging these inputs.
     */
    @AutoLog
    public static class LedIOInputs {

        /** Whether the LED controller is currently connected. */
        public boolean connected = false;

        /** Voltage being supplied to the LED controller. */
        public Voltage voltage = Volts.of(0.0);

        /** Temperature of the LED controller. */
        public Temperature temperature = Kelvin.of(294.0);
    }

    /**
     * Updates the provided inputs with the latest LED controller data.
     *
     * <p>This is done instead of simply returning a new inputs object so that we can reuse inputs
     * objects, and don't have to waste time each loop period allocating a bunch of new inputs
     * objects.
     *
     * @param inputs the input object whose data will be updated
     */
    void updateInputs(LedIOInputs inputs);

    /**
     * Sets the color of all connected LEDs to the provided color.
     *
     * @param color color for the connected LEDs
     */
    void setColor(Color color);

    /**
     * Sets the color of a specific built-in LED, intended for displaying robot status information.
     *
     * @param index the LED to set
     * @param color color for the LED
     */
    void setStatusColor(int index, Color color);
}

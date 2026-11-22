package frc.lib.led;

import static frc.lib.units.FrequencyUnit.Hertz;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.StatusSignalCollection;
import com.ctre.phoenix6.controls.SolidColor;
import com.ctre.phoenix6.hardware.CANdle;
import com.ctre.phoenix6.signals.RGBWColor;
import frc.lib.units.TemperatureUnit;
import frc.lib.units.VoltageUnit;
import org.wpilib.units.measure.Temperature;
import org.wpilib.units.measure.Voltage;
import org.wpilib.util.Color;

/** Implementation of {@link LedIO} using a CTRE CANdle. */
public class Candle implements LedIO {

    private CANdle led;

    private final StatusSignalCollection signals = new StatusSignalCollection(2);
    private final StatusSignal<Voltage> voltageSignal;
    private final StatusSignal<Temperature> temperatureSignal;

    private final SolidColor colorControl;
    private final SolidColor statusColorControl;

    /**
     * Creates a CANdle object based on the provided configuration. Status signals are set to update
     * at 100 Hz and optimized for reduced CAN bus utilization.
     *
     * @param config configuration for the CANdle
     */
    public Candle(LedConfig config) {
        led = new CANdle(config.id().id(), config.id().bus());
        colorControl = new SolidColor(8, 7 + config.ledCount());
        statusColorControl = new SolidColor(0, 0);

        voltageSignal = led.getSupplyVoltage();
        temperatureSignal = led.getDeviceTemp();
        signals.addSignals(voltageSignal, temperatureSignal);
        signals.setUpdateFrequencyForAll(Hertz.raw(100));
        led.optimizeBusUtilization();
    }

    /**
     * Updates the provided inputs with the latest data from the CANdle.
     *
     * @param inputs the inputs object to populate with the new data
     */
    @Override
    public void updateInputs(LedIOInputs inputs) {
        signals.refreshAll();
        inputs.connected = signals.isAllGood();
        inputs.voltage = VoltageUnit.ofWpi(voltageSignal.getValue());
        inputs.temperature = TemperatureUnit.ofWpi(temperatureSignal.getValue());
    }

    /**
     * Sets all connected LEDs to the provided color.
     *
     * @param color the color for the connected LEDs
     */
    @Override
    public void setColor(Color color) {
        led.setControl(colorControl.withColor(new RGBWColor(color)));
    }

    /**
     * Sets the CANdle's built-in LED at the provided index to the provided color.
     *
     * @param index the index to set; must be in the interval [0, 7]
     * @param color the color for the LED
     * @throws IllegalArgumentException if the index is outside the interval [0, 7]
     */
    @Override
    public void setStatusColor(int index, Color color) {
        if (index < 0 || index > 7) {
            throw new IllegalArgumentException(
                    "Index for built-in CANdle LED must be in the interval [0, 7]");
        }

        led.setControl(
                statusColorControl
                        .withLEDStartIndex(index)
                        .withLEDEndIndex(index)
                        .withColor(new RGBWColor(color)));
    }
}

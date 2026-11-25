package frc.lib.sensor.beambreak;

import org.wpilib.hardware.discrete.DigitalInput;

/**
 * Real hardware implementation of {@link BeamBreakIO} using a WPILib {@link DigitalInput}.
 *
 * <p>This class provides an interface between the library's beam break sensor abstraction and a
 * beam break sensor connected to a roboRIO digital input. It retrieves the state of the digital
 * input and reports whether the beam is currently tripped.
 *
 * <p>Unlike CAN-based sensors, a {@link DigitalInput} does not provide a connection status, so this
 * implementation always reports the sensor as connected.
 */
public class DigitalInputBeamBreak implements BeamBreakIO {

    /** Digital input connected to the beam break sensor. */
    private final DigitalInput beamBreak;

    /**
     * Creates a beam break sensor using the specified configuration
     *
     * @param config configuration for the beam break sensor
     * @throws IllegalArgumentException if the configuration specifies any device type other than
     *     {@link BeamBreakDevice.DigitalInput}.
     */
    public DigitalInputBeamBreak(BeamBreakConfig config) {
        beamBreak =
                switch (config.device()) {
                    case BeamBreakDevice.CanRange _ ->
                            throw new IllegalArgumentException(
                                    "Configuration for a digital input beam break cannot specify a CANrange");
                    case BeamBreakDevice.DigitalInput(int id) -> new DigitalInput(id);
                };
    }

    /**
     * Updates the provided inputs with the current state of the beam break sensor.
     *
     * <p>The digital input state is read and stored in the {@code tripped} field. The sensor is
     * always reported as connected because WPILib's {@link DigitalInput} does not provide a way to
     * determine whether a physical device is actually connected to the port.
     *
     * @param inputs object that should be populated with the sensor's current inputs
     */
    @Override
    public void updateInputs(BeamBreakIOInputs inputs) {
        inputs.connected = true;
        inputs.tripped = beamBreak.get();
    }
}

package com.nicoletfear.lib.sensor.absencoder;

import static com.nicoletfear.lib.units.AngleUnit.Radians;
import static com.nicoletfear.lib.units.FrequencyUnit.Hertz;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.StatusSignalCollection;
import com.ctre.phoenix6.hardware.CANcoder;
import com.nicoletfear.lib.Configurator;
import com.nicoletfear.lib.units.AngleUnit;
import com.nicoletfear.lib.units.AngularVelocityUnit;
import org.wpilib.units.measure.Angle;
import org.wpilib.units.measure.AngularVelocity;

/**
 * Real hardware implementation of {@link AbsEncoderIO} using a CTRE CANcoder.
 *
 * <p>This class provides an interface between the library's absolute encoder abstraction and a
 * physical CANcoder. The position and velocity signals are updated at 100 Hz, and CAN bus
 * utilization is optimized.
 */
public class CanCoder implements AbsEncoderIO {
    private final CANcoder absEncoder;

    /** Position and velocity signals from the CANcoder. */
    private final StatusSignalCollection signals = new StatusSignalCollection(2);

    /** The mechanism position currently read by the CANcoder. */
    private final StatusSignal<Angle> positionSignal;

    /** The mechanism velocity currently read by the CANcoder. */
    private final StatusSignal<AngularVelocity> velocitySignal;

    /**
     * Configures the CANcoder with the values given in the config.
     *
     * @param config configuration for the CANcoder
     */
    public CanCoder(AbsEncoderConfig config) {
        absEncoder = new CANcoder(config.id().id(), config.id().bus());
        Configurator.applyCanCoderConfiguration(absEncoder, config.getCanCoderConfiguration());

        positionSignal = absEncoder.getAbsolutePosition();
        velocitySignal = absEncoder.getVelocity();

        signals.addSignals(positionSignal, velocitySignal);
        signals.setUpdateFrequencyForAll(Hertz.raw(100.0));
        absEncoder.optimizeBusUtilization();
    }

    /**
     * Updates the provided inputs with the CANcoder's current position and velocity.
     *
     * @param inputs object to populate with the CANcoder's current inputs
     */
    @Override
    public void updateInputs(AbsEncoderIOInputs inputs) {
        signals.refreshAll();
        inputs.connected = signals.isAllGood();
        inputs.position = AngleUnit.ofWpi(positionSignal.getValue());
        inputs.velocity = AngularVelocityUnit.ofWpi(velocitySignal.getValue());
    }

    /** Sets the CANcoder's reported position to zero. */
    @Override
    public void setZeroPoint() {
        absEncoder.setPosition(Radians.raw(0.0));
    }
}

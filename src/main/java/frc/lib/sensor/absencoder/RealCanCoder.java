package frc.lib.sensor.absencoder;

import static frc.lib.units.AngleUnit.Radians;
import static frc.lib.units.FrequencyUnit.Hertz;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.StatusSignalCollection;
import com.ctre.phoenix6.hardware.CANcoder;
import frc.lib.units.AngleUnit;
import frc.lib.units.AngularVelocityUnit;
import org.wpilib.units.measure.Angle;
import org.wpilib.units.measure.AngularVelocity;

/** Implements {@link AbsEncoderIO} in order to create the real/physical CANcoder */
public class RealCanCoder implements AbsEncoderIO {
    private final CANcoder canCoder;

    /** Stores the Position and Velocity Signals given from the CANcoder */
    private final StatusSignalCollection signals = new StatusSignalCollection(2);

    /** Is the current mechanism position the CANcoder is reading */
    private final StatusSignal<Angle> positionSignal;

    /** Is the current mechanism velocity the CANcoder is reading */
    private final StatusSignal<AngularVelocity> velocitySignal;

    /**
     * Configures the CANcoder with the values given in the config
     *
     * @param config Is the configuration for the CANcoder
     */
    public RealCanCoder(AbsEncoderConfig config) {
        canCoder = new CANcoder(config.id().id(), config.id().bus());
        canCoder.getConfigurator().apply(config.getCanCoderConfiguration());

        positionSignal = canCoder.getAbsolutePosition();
        velocitySignal = canCoder.getVelocity();

        signals.addSignals(positionSignal, velocitySignal);
        signals.setUpdateFrequencyForAll(Hertz.raw(100.0));
        canCoder.optimizeBusUtilization();
    }

    /** Provides the implementation for updating the inputs/data of the CANcoder */
    @Override
    public void updateInputs(AbsEncoderIOInputs inputs) {
        signals.refreshAll();
        inputs.connected = signals.isAllGood();
        inputs.position = AngleUnit.ofWpi(positionSignal.getValue());
        inputs.velocity = AngularVelocityUnit.ofWpi(velocitySignal.getValue());
    }

    /** Provides the implementation for zeroing/homing the CANcoder */
    @Override
    public void setZeroPoint() {
        canCoder.setPosition(Radians.raw(0.0));
    }
}

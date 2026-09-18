package frc.lib.absencoder;

import static edu.wpi.first.units.Units.Hertz;
import static edu.wpi.first.units.Units.Radians;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.StatusSignalCollection;
import com.ctre.phoenix6.hardware.CANcoder;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;

/**
 * Implements {@link AbsEncoderIO} in order to create the real/physical CANcoder
 */
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
        signals.setUpdateFrequencyForAll(Hertz.of(100.0));
        canCoder.optimizeBusUtilization();
    }

    /** Provides the implementation for updating the inputs/data of the CANcoder */
    @Override
    public void updateInputs(AbsEncoderIOInputs inputs) {
        signals.refreshAll();
        inputs.connected = signals.isAllGood();
        inputs.position = positionSignal.getValue();
        inputs.velocity = velocitySignal.getValue();
    }

    /** Provides the implementation for zeroing/homing the CANcoder */
    @Override
    public void setZeroPoint() {
        canCoder.setPosition(Radians.of(0.0));
    }
}

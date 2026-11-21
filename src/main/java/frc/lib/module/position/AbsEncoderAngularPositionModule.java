package frc.lib.module.position;

import frc.lib.motor.MotorIO;
import frc.lib.sensor.absencoder.AbsEncoderIO;
import frc.lib.sensor.absencoder.AbsEncoderIOInputsAutoLogged;
import frc.lib.units.Angle;
import org.littletonrobotics.junction.Logger;

/**
 * An angular position module that uses an absolute encoder as its primary source for mechanism
 * position.
 *
 * <p>This module extends {@link AngularPositionModule} and uses the absolute encoder position when
 * the encoder is connected. If the absolute encoder is disconnected, the module falls back to the
 * position provided by the parent {@link AngularPositionModule}.
 */
public final class AbsEncoderAngularPositionModule extends AngularPositionModule {

    /** Interface used to communicate with and retrieve data from the absolute encoder. */
    private final AbsEncoderIO absEncoder;

    /** Logged inputs containing the current state and position of the absolute encoder. */
    private final AbsEncoderIOInputsAutoLogged encoderInputs = new AbsEncoderIOInputsAutoLogged();

    /**
     * Creates an angular position module using an absolute encoder for position feedback.
     *
     * @param name name used to identify and log this module
     * @param motor interface used to communicate with the motor
     * @param absEncoder interface used to communicate with the absolute encoder
     */
    public AbsEncoderAngularPositionModule(String name, MotorIO motor, AbsEncoderIO absEncoder) {
        super(name, motor);
        this.absEncoder = absEncoder;
    }

    /**
     * Updates the module's inputs and logs the current absolute encoder data.
     *
     * <p>This method first updates the inputs inherited from the parent module, then retrieves the
     * latest absolute encoder data and sends it to the AdvantageKit logger.
     */
    @Override
    public void updateInputs() {
        super.updateInputs();
        absEncoder.updateInputs(encoderInputs);
        Logger.processInputs(name + " Abs Encoder", encoderInputs);
    }

    /**
     * Gets the current angular position of the mechanism.
     *
     * <p>If the absolute encoder is connected, its position is used. Otherwise, the position provided
     * by the parent {@link AngularPositionModule} is used as a fallback.
     *
     * @return the current mechanism position as an {@link Angle}
     */
    @Override
    public Angle getPosition() {
        if (encoderInputs.connected) {
            return encoderInputs.position;
        } else {
            return super.getPosition();
        }
    }
}

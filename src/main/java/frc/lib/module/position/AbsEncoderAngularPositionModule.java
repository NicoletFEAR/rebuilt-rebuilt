package frc.lib.module.position;

import edu.wpi.first.units.measure.Angle;
import frc.lib.absencoder.AbsEncoderIO;
import frc.lib.absencoder.AbsEncoderIOInputsAutoLogged;
import frc.lib.motor.MotorIO;
import org.littletonrobotics.junction.Logger;

/**
 * An angular position module that uses an absolute encoder as its primary
 * source for mechanism position.
 * 
 * <p>
 * This module extends {@link AngularPositionModule} and uses the absolute
 * encoder position when the encoder is connected. If the absolute encoder is
 * disconnected, the module falls back to the position provided by the parent
 * {@link AngularPositionModule}.
 */
public class AbsEncoderAngularPositionModule extends AngularPositionModule {

    /**
     * Interface used to communicate with and retrieve data from the absolute
     * encoder.
     */
    private final AbsEncoderIO absEncoderIO;

    /**
     * Logged inputs containing the current state and position of the absolute
     * encoder.
     */
    private final AbsEncoderIOInputsAutoLogged absEncoderInputs;

    /**
     * Creates an angular position module using an absolute encoder for position
     * feedback.
     *
     * @param name                  name used to identify and log this module
     * @param motorIO               interface used to communicate with the motor
     * @param canCoderIO            interface used to communicate with the absolute
     *                              encoder
     * @param rotorToMechanismRatio ratio between the motor rotor and the mechanism
     */
    public AbsEncoderAngularPositionModule(
            String name, MotorIO motorIO, AbsEncoderIO canCoderIO, double rotorToMechanismRatio) {
        super(name, motorIO, rotorToMechanismRatio);
        this.absEncoderIO = canCoderIO;
        absEncoderInputs = new AbsEncoderIOInputsAutoLogged();
    }

    /**
     * Updates the module's inputs and logs the current absolute encoder data.
     * 
     * <p>
     * This method first updates the inputs inherited from the parent module, then
     * retrieves the latest absolute encoder data and sends it to the AdvantageKit
     * logger.
     */
    @Override
    public void periodic() {
        super.periodic();
        absEncoderIO.updateInputs(absEncoderInputs);
        Logger.processInputs(
                name + "AbsEncoder", absEncoderInputs.applyRotorToMechanismRatio(rotorToMechanismRatio));
    }

    /**
     * Gets the current angular position of the mechanism.
     * 
     * <p>
     * If the absolute encoder is connected, its position is used. Otherwise, the
     * position provided by the parent {@link AngularPositionModule} is used as a
     * fallback.
     * 
     * @return the current mechanism position as an {@link Angle}
     */
    @Override
    public Angle getPosition() {
        if (absEncoderInputs.connected) {
            return absEncoderInputs.position.div(rotorToMechanismRatio);
        } else {
            return super.getPosition().div(rotorToMechanismRatio);
        }
    }
}

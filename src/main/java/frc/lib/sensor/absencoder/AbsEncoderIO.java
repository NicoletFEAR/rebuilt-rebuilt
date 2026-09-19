package frc.lib.sensor.absencoder;

import static edu.wpi.first.units.Units.Radians;
import static edu.wpi.first.units.Units.RadiansPerSecond;

import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import org.littletonrobotics.junction.AutoLog;

/**
 * An interface for communicating with an Absolute Encoder
 *
 * <p>Can be used to create a real and/or a simulated Absolute Encoder
 */
public interface AbsEncoderIO {
    /**
     * Stores the inputs and sensor readings of the Absolute Encoder
     *
     * <p>Automatically logs the values to AdvantageScope through the {@link AutoLog} annotation.
     */
    @AutoLog
    public static class AbsEncoderIOInputs {

        /** Whether the Absolute Encoder is currently connected */
        public boolean connected = false;

        /** The angular position of the Absolute Encoder */
        public Angle position = Radians.of(0.0);

        /** The Angular Velocity of the Absolute Encoder */
        public AngularVelocity velocity = RadiansPerSecond.of(0.0);

        /**
         * Converts the raw position value of the Absolute Encoder into the mechanism unit using the
         * rotorToMechanismRatio
         *
         * @param rotorToMechanismRatio Is the gear ratio of the mechanism
         * @return Returns the converted values of the mechanism in easily understandable units
         */
        public AbsEncoderIOInputsAutoLogged applyRotorToMechanismRatio(double rotorToMechanismRatio) {
            AbsEncoderIOInputsAutoLogged result = new AbsEncoderIOInputsAutoLogged();
            result.connected = connected;
            result.position = position.div(rotorToMechanismRatio);
            result.velocity = velocity.div(rotorToMechanismRatio);
            return result;
        }
    }

    /**
     * Updates the provided inputs with the latest Absolute Encoder data
     *
     * @param inputs The input object whose data will be updated
     */
    void updateInputs(AbsEncoderIOInputs inputs);

    /** Sets the current position as its zero/home position */
    void setZeroPoint();
}

package frc.lib.cancoder;

import static edu.wpi.first.units.Units.Radians;
import static edu.wpi.first.units.Units.RadiansPerSecond;

import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import org.littletonrobotics.junction.AutoLog;

/**
 * An interface for communicating with a CANcoder
 *
 * <p>Can be used to create a real and/or a simulated CANcoder
 */
public interface CanCoderIO {
    /**
     * Stores the inputs and sensor readings of the CANcoder
     *
     * <p>Automatically logs the values to AdvantageScope through the {@link AutoLog} annotation.
     */
    @AutoLog
    public static class CanCoderIOInputs {

        /** Whether the CANcoder is currently connected */
        public boolean connected = false;

        /** The position of the CANcoder in Radians */
        public Angle position = Radians.of(0.0);

        /** The Angular Velocity of the CANcoder in Radians per Second */
        public AngularVelocity velocity = RadiansPerSecond.of(0.0);

        /**
         * Converts the raw position value of the CANcoder into the mechanism unit using the
         * rotorToMechanismRatio
         *
         * @param rotorToMechanismRatio Is the gear ratio of the mechanism
         * @return Returns the converted values of the mechanism in easily understandable units
         */
        public CanCoderIOInputsAutoLogged applyRotorToMechanismRatio(double rotorToMechanismRatio) {
            CanCoderIOInputsAutoLogged result = new CanCoderIOInputsAutoLogged();
            result.connected = connected;
            result.position = position.div(rotorToMechanismRatio);
            result.velocity = velocity.div(rotorToMechanismRatio);
            return result;
        }
    }

    /**
     * Updates the provided inputs with the latest CANcoder data
     *
     * @param inputs The input object whose data will be updated
     */
    void updateInputs(CanCoderIOInputs inputs);

    /** Sets the current position as its zero/home position */
    void setZeroPoint();
}

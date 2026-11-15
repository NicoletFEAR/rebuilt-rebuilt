package frc.lib.sensor.absencoder;

import static frc.lib.units.AngleUnit.Radians;
import static frc.lib.units.AngularVelocityUnit.RadiansPerSecond;

import frc.lib.units.Angle;
import frc.lib.units.AngularVelocity;
import frc.robot.Robot;
import org.littletonrobotics.junction.AutoLog;

/**
 * Provides an interface for communicating with an absolute encoder.
 *
 * <p>Can be used to create a real and/or a simulated Absolute Encoder
 */
public interface AbsEncoderIO {

    /**
     * Creates a real CANcoder or a simulated CANcoder depending on whether the robot is being
     * simulated or not.
     *
     * @param config configuration for the CANcoder
     * @return the correct CANcoder implementation
     */
    static CanCoder buildCanCoder(AbsEncoderConfig config) {
        return Robot.isReal() ? new CanCoder(config) : new SimCanCoder(config);
    }

    /**
     * Stores the inputs and sensor readings of the Absolute Encoder
     *
     * <p>Automatically logs the values to AdvantageKit through the {@link AutoLog} annotation.
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
     * Updates the provided inputs with the latest Absolute Encoder data. This is done instead of
     * simply returning a new inputs object so that we can reuse inputs objects, and don't have to
     * waste time each loop period allocating a bunch of new inputs objects.
     *
     * @param inputs The input object whose data will be updated
     */
    void updateInputs(AbsEncoderIOInputs inputs);

    /** Sets the current position as its zero/home position */
    void setZeroPoint();
}

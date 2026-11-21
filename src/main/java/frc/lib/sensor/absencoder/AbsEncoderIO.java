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
 * <p>Can be used to create a real or simulated absolute encoder.
 */
public interface AbsEncoderIO {

    /**
     * Creates a real CANcoder or a simulated CANcoder depending on whether the robot is being
     * simulated or not.
     *
     * @param config configuration for the absolute encoder
     * @return the correct absolute encoder implementation
     */
    static AbsEncoderIO buildAbsEncoder(AbsEncoderConfig config) {
        return Robot.isReal() ? new CanCoder(config) : new SimCanCoder(config);
    }

    /**
     * Stores the inputs and sensor readings of the absolute encoder.
     *
     * <p>Automatically logs the values to AdvantageKit through the {@link AutoLog} annotation.
     */
    @AutoLog
    public static class AbsEncoderIOInputs {

        /** Whether the absolute encoder is currently connected. */
        public boolean connected = false;

        /** The angular position of the absolute encoder. */
        public Angle position = Radians.of(0.0);

        /** The angular velocity of the absolute encoder. */
        public AngularVelocity velocity = RadiansPerSecond.of(0.0);
    }

    /**
     * Updates the provided inputs with the latest absolute encoder data. This is done instead of
     * simply returning a new inputs object so that we can reuse inputs objects, and don't have to
     * waste time each loop period allocating a bunch of new inputs objects.
     *
     * @param inputs the input object whose data will be updated
     */
    void updateInputs(AbsEncoderIOInputs inputs);

    /** Sets the current position as its zero/home position. */
    void setZeroPoint();
}

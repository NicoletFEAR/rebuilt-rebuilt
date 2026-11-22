package frc.lib.motor;

import static frc.lib.units.AngularAccelerationUnit.RotationsPerSecondSquared;
import static frc.lib.units.AngularVelocityUnit.RotationsPerSecond;

import com.ctre.phoenix6.configs.FeedbackConfigs;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import frc.lib.CanId;
import java.util.Optional;

/**
 * Stores the configuration values used to configure a motor.
 *
 * <p>This record contains the motor's CAN ID, neutral mode, sensor to mechanism ratio, feedback and
 * feedforward gains, and optional follower alignment. The configuration can be converted into a
 * CTRE Phoenix 6 {@link TalonFXConfiguration} using {@link #getTalonFXConfiguration()}.
 *
 * @param id CAN ID and CAN bus of the motor
 * @param sensorToMechanismRatio number of radians of sensor rotation associated with 1 radian of
 *     mechanism rotation or 1 meter of mechanism movement
 * @param neutralMode neutral behavior of the motor when no output is applied
 * @param feedforwardValues PID and feedforward gains for the motor
 * @param alignment optional alignment used when configuring the motor as a follower
 */
public record MotorConfig(
        CanId id,
        double sensorToMechanismRatio,
        NeutralModeValue neutralMode,
        FeedforwardValues feedforwardValues,
        Optional<MotorAlignmentValue> alignment) {

    /**
     * Creates a motor configuration using the default neutral mode, the default sensor to mechanism
     * ratio, and no follower alignment.
     *
     * <p>The default neutral mode is {@link NeutralModeValue#Coast}, the default sensor to mechanism
     * ratio is 1, and the motor is configured without a follower alignment.
     *
     * @param id CAN ID and CAN bus of the motor
     * @param feedforwardValues PID and feedforward gains for the motor
     */
    public MotorConfig(CanId id, FeedforwardValues feedforwardValues) {
        this(id, 1.0, feedforwardValues);
    }

    /**
     * Creates a motor configuration using the default neutral mode and no follower alignment.
     *
     * <p>The default neutral mode is {@link NeutralModeValue#Coast} and the motor is configured
     * without a follower alignment.
     *
     * @param id CAN ID and CAN bus of the motor
     * @param sensorToMechanismRatio number of radians of sensor rotation associated with 1 radian of
     *     mechanism rotation or 1 meter of mechanism movement
     * @param feedforwardValues PID and feedforward gains for the motor
     */
    public MotorConfig(CanId id, double sensorToMechanismRatio, FeedforwardValues feedforwardValues) {
        this(id, sensorToMechanismRatio, NeutralModeValue.Coast, feedforwardValues, Optional.empty());
    }

    /**
     * Creates a motor configuration using the default neutral mode, the default sensor to mechanism
     * ratio, and the given follower alignment.
     *
     * <p>The default neutral mode is {@link NeutralModeValue#Coast} and the default sensor to
     * mechanism ratio is 1.
     *
     * @param id CAN ID and CAN bus of the motor
     * @param feedforwardValues PID and feedforward gains for the motor
     * @param alignment alignment to use when configuring the motor as a follower
     */
    public MotorConfig(CanId id, FeedforwardValues feedforwardValues, MotorAlignmentValue alignment) {
        this(id, 1.0, NeutralModeValue.Coast, feedforwardValues, Optional.of(alignment));
    }

    /**
     * Creates a CTRE Phoenix 6 configuration using the values stored in this motor configuration.
     *
     * <p>The returned configuration sets the motor's neutral mode, sensor to mechanism ratio, Slot 0
     * PID and feedforward gains, and Motion Magic cruise velocity and acceleration (hardcoded to 100
     * rotations per second and 100 rotations per second squared).
     *
     * @return a configured {@link TalonFXConfiguration}
     */
    public TalonFXConfiguration getTalonFXConfiguration() {
        return new TalonFXConfiguration()
                .withFeedback(new FeedbackConfigs().withSensorToMechanismRatio(sensorToMechanismRatio))
                .withMotorOutput(new MotorOutputConfigs().withNeutralMode(neutralMode))
                .withSlot0(feedforwardValues.getSlot0Configs())
                .withMotionMagic(
                        new MotionMagicConfigs()
                                .withMotionMagicAcceleration(RotationsPerSecondSquared.raw(100.0))
                                .withMotionMagicCruiseVelocity(RotationsPerSecond.raw(100.0)));
    }

    /**
     * Creates a new motor configuration with all the values of the current one except for a different
     * ID.
     *
     * @param id new ID
     */
    public MotorConfig withId(CanId id) {
        return new MotorConfig(id, sensorToMechanismRatio, neutralMode, feedforwardValues, alignment);
    }

    /**
     * Creates a new motor configuration with all the values of the current one except for a different
     * sensor to mechanism ratio.
     *
     * @param sensorToMechanismRatio new sensor to mechanism ratio
     */
    public MotorConfig withSensorToMechanismRatio(double sensorToMechanismRatio) {
        return new MotorConfig(id, sensorToMechanismRatio, neutralMode, feedforwardValues, alignment);
    }

    /**
     * Creates a new motor configuration with all the values of the current one except for a different
     * neutral mode.
     *
     * @param neutralMode new neutral mode
     */
    public MotorConfig withNeutralMode(NeutralModeValue neutralMode) {
        return new MotorConfig(id, sensorToMechanismRatio, neutralMode, feedforwardValues, alignment);
    }

    /**
     * Creates a new motor configuration with all the values of the current one except for different
     * feedforward values.
     *
     * @param feedforwardValues new feedforward values
     */
    public MotorConfig withFeedforwardValues(FeedforwardValues feedforwardValues) {
        return new MotorConfig(id, sensorToMechanismRatio, neutralMode, feedforwardValues, alignment);
    }

    /**
     * Creates a new motor configuration with all the values of the current one except for a different
     * alignment.
     *
     * @param alignment new alignment to use
     */
    public MotorConfig withAlignment(MotorAlignmentValue alignment) {
        return new MotorConfig(
                id, sensorToMechanismRatio, neutralMode, feedforwardValues, Optional.of(alignment));
    }

    /**
     * Creates a new motor configuration with all the values of the current one, except with no
     * alignment.
     */
    public MotorConfig withoutAlignment() {
        return new MotorConfig(
                id, sensorToMechanismRatio, neutralMode, feedforwardValues, Optional.empty());
    }
}

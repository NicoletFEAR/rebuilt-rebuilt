package frc.lib.motor;

import static edu.wpi.first.units.Units.KilogramSquareMeters;
import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.RadiansPerSecondPerSecond;

import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.units.measure.MomentOfInertia;
import frc.lib.CanId;
import java.util.Optional;

/**
 * Stores the configuration values used to configure a motor.
 *
 * <p>This record contains the motor's CAN ID, neutral mode, feedback and feedforward gains, motor
 * type, moment of inertia, and optional follower alignment. The configuration can be converted into
 * a CTRE Phoenix 6 {@link TalonFXConfiguration} using {@link #getTalonFXConfiguration()}.
 *
 * @param id CAN ID and CAN bus of the motor
 * @param neutralMode neutral behavior of the motor when no output is applied
 * @param feedforwardValues PID and feedforward gains for the motor
 * @param type type of Kraken motor being used
 * @param momentOfInertia moment of inertia of the motor and mechanism
 * @param alignment optional alignment used when configuring the motor as a follower
 */
public record MotorConfig(
        CanId id,
        NeutralModeValue neutralMode,
        FeedforwardValues feedforwardValues,
        KrakenType type,
        MomentOfInertia momentOfInertia,
        Optional<MotorAlignmentValue> alignment) {

    /**
     * Creates a motor configuration using the default neutral mode, moment of inertia, and no
     * follower alignment.
     *
     * <p>The default neutral mode is {@link NeutralModeValue#Coast}, the default moment of inertia is
     * 0.016 kg·m², and the motor is configured without a follower alignment.
     *
     * @param id CAN ID and CAN bus of the motor
     * @param feedforwardValues PID and feedforward gains for the motor
     * @param type type of Kraken motor being used
     */
    public MotorConfig(CanId id, FeedforwardValues feedforwardValues, KrakenType type) {
        this(
                id,
                NeutralModeValue.Coast,
                feedforwardValues,
                type,
                KilogramSquareMeters.of(0.016),
                Optional.empty());
    }

    /**
     * Creates a motor configuration using the default neutral mode and no follower alignment.
     *
     * <p>The default neutral mode is {@link NeutralModeValue#Coast}.
     *
     * @param id CAN ID and CAN bus of the motor
     * @param feedforwardValues PID and feedforward gains for the motor
     * @param type type of Kraken motor being used
     * @param momentOfInertia moment of inertia of the motor and mechanism
     */
    public MotorConfig(
            CanId id,
            FeedforwardValues feedforwardValues,
            KrakenType type,
            MomentOfInertia momentOfInertia) {
        this(id, NeutralModeValue.Coast, feedforwardValues, type, momentOfInertia, Optional.empty());
    }

    public MotorConfig(
            CanId id,
            FeedforwardValues feedforwardValues,
            KrakenType type,
            MotorAlignmentValue alignment) {
        this(
                id,
                NeutralModeValue.Coast,
                feedforwardValues,
                type,
                KilogramSquareMeters.of(0.016),
                Optional.of(alignment));
    }

    /**
     * Creates a CTRE Phoenix 6 configuration using the values stored in this motor configuration.
     *
     * <p>The returned configuration sets the motor's neutral mode and Slot 0 PID and feedforward
     * gains.
     *
     * @return a configured {@link TalonFXConfiguration}
     */
    public TalonFXConfiguration getTalonFXConfiguration() {
        return new TalonFXConfiguration()
                .withMotorOutput(new MotorOutputConfigs().withNeutralMode(neutralMode))
                .withSlot0(feedforwardValues.getSlot0Configs())
                .withMotionMagic(
                        new MotionMagicConfigs()
                                .withMotionMagicAcceleration(RadiansPerSecondPerSecond.of(Math.PI * 200.0))
                                .withMotionMagicCruiseVelocity(RadiansPerSecond.of(Math.PI * 200.0)));
    }
}

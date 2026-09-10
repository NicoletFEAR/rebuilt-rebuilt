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

public record MotorConfig(
        CanId id,
        NeutralModeValue neutralMode,
        FeedforwardValues feedforwardValues,
        KrakenType type,
        MomentOfInertia momentOfInertia,
        Optional<MotorAlignmentValue> alignment) {
    public MotorConfig(CanId id, FeedforwardValues feedforwardValues, KrakenType type) {
        this(
                id,
                NeutralModeValue.Coast,
                feedforwardValues,
                type,
                KilogramSquareMeters.of(0.016),
                Optional.empty());
    }

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

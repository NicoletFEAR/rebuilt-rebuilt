package frc.lib.motor;

import static edu.wpi.first.units.Units.KilogramSquareMeters;

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

    public TalonFXConfiguration getTalonFXConfiguration() {
        return new TalonFXConfiguration()
                .withMotorOutput(new MotorOutputConfigs().withNeutralMode(neutralMode))
                .withSlot0(feedforwardValues.getSlot0Configs());
    }
}

package frc.lib.motor;

import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.units.measure.MomentOfInertia;
import frc.lib.CanId;
import java.util.Objects;
import java.util.Optional;
import lombok.Builder;

@Builder
public record MotorConfig(
        CanId id,
        NeutralModeValue neutralMode,
        FeedforwardValues feedforwardValues,
        KrakenType type,
        MomentOfInertia momentOfInertia,
        double positionStdandardDeviation,
        double velocityStandardDeviation,
        Optional<MotorAlignmentValue> alignment) {
    public MotorConfig {
        alignment = Objects.requireNonNullElse(alignment, Optional.empty());
    }

    public TalonFXConfiguration getTalonFXConfiguration() {
        return new TalonFXConfiguration()
                .withMotorOutput(new MotorOutputConfigs().withNeutralMode(neutralMode))
                .withSlot0(feedforwardValues.getSlot0Configs());
    }
}

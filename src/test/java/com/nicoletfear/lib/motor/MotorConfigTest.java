package com.nicoletfear.lib.motor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.nicoletfear.lib.CanId;
import com.nicoletfear.lib.testutil.TestConfigs;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class MotorConfigTest {
    private static final double EPSILON = 1e-9;

    private static final CanId ID = TestConfigs.canId(3);
    private static final FeedforwardValues FEEDFORWARD = TestConfigs.feedforward();

    @Test
    void defaultConstructorUsesCoastAndNoAlignment() {
        MotorConfig config = new MotorConfig(ID, 2.0, FEEDFORWARD);

        assertEquals(ID, config.id());
        assertEquals(2.0, config.sensorToMechanismRatio(), EPSILON);
        assertEquals(NeutralModeValue.Coast, config.neutralMode());
        assertEquals(FEEDFORWARD, config.feedforwardValues());
        assertEquals(Optional.empty(), config.alignment());
    }

    @Test
    void alignmentConstructorStoresAlignment() {
        MotorConfig config = new MotorConfig(ID, 2.0, FEEDFORWARD, MotorAlignmentValue.Opposed);

        assertEquals(Optional.of(MotorAlignmentValue.Opposed), config.alignment());
        assertEquals(NeutralModeValue.Coast, config.neutralMode());
    }

    @Test
    void withIdOnlyChangesId() {
        MotorConfig original = new MotorConfig(ID, 2.0, NeutralModeValue.Brake, FEEDFORWARD, Optional.empty());
        CanId newId = TestConfigs.canId(9);

        MotorConfig changed = original.withId(newId);

        assertEquals(newId, changed.id());
        assertEquals(original.sensorToMechanismRatio(), changed.sensorToMechanismRatio(), EPSILON);
        assertEquals(original.neutralMode(), changed.neutralMode());
        assertEquals(original.feedforwardValues(), changed.feedforwardValues());
        assertEquals(original.alignment(), changed.alignment());
    }

    @Test
    void withSensorToMechanismRatioOnlyChangesRatio() {
        MotorConfig original = new MotorConfig(ID, 2.0, NeutralModeValue.Brake, FEEDFORWARD, Optional.empty());

        MotorConfig changed = original.withSensorToMechanismRatio(3.5);

        assertEquals(3.5, changed.sensorToMechanismRatio(), EPSILON);
        assertEquals(original.id(), changed.id());
        assertEquals(original.neutralMode(), changed.neutralMode());
        assertEquals(original.feedforwardValues(), changed.feedforwardValues());
        assertEquals(original.alignment(), changed.alignment());
    }

    @Test
    void withNeutralModeOnlyChangesNeutralMode() {
        MotorConfig original = new MotorConfig(ID, 2.0, FEEDFORWARD);

        MotorConfig changed = original.withNeutralMode(NeutralModeValue.Brake);

        assertEquals(NeutralModeValue.Brake, changed.neutralMode());
        assertEquals(original.id(), changed.id());
        assertEquals(original.sensorToMechanismRatio(), changed.sensorToMechanismRatio(), EPSILON);
        assertEquals(original.feedforwardValues(), changed.feedforwardValues());
    }

    @Test
    void withFeedforwardValuesOnlyChangesFeedforwardValues() {
        MotorConfig original = new MotorConfig(ID, 2.0, FEEDFORWARD);
        FeedforwardValues newFeedforward = new FeedforwardValues(9.0, 8.0, 7.0, 6.0, 5.0, 4.0, 3.0);

        MotorConfig changed = original.withFeedforwardValues(newFeedforward);

        assertEquals(newFeedforward, changed.feedforwardValues());
        assertEquals(original.id(), changed.id());
        assertEquals(original.sensorToMechanismRatio(), changed.sensorToMechanismRatio(), EPSILON);
        assertEquals(original.neutralMode(), changed.neutralMode());
    }

    @Test
    void withAlignmentAddsAlignment() {
        MotorConfig original = new MotorConfig(ID, 2.0, FEEDFORWARD);

        MotorConfig changed = original.withAlignment(MotorAlignmentValue.Aligned);

        assertEquals(Optional.of(MotorAlignmentValue.Aligned), changed.alignment());
        assertEquals(original.id(), changed.id());
    }

    @Test
    void withoutAlignmentRemovesAlignment() {
        MotorConfig original = new MotorConfig(ID, 2.0, FEEDFORWARD, MotorAlignmentValue.Opposed);

        MotorConfig changed = original.withoutAlignment();

        assertTrue(changed.alignment().isEmpty());
        assertEquals(original.id(), changed.id());
        assertEquals(original.neutralMode(), changed.neutralMode());
    }

    @Test
    void convertsToTalonFxConfiguration() {
        MotorConfig config = new MotorConfig(ID, 2.5, NeutralModeValue.Brake, FEEDFORWARD, Optional.empty());

        TalonFXConfiguration ctreConfig = config.getTalonFXConfiguration();

        assertEquals(2.5, ctreConfig.Feedback.SensorToMechanismRatio, EPSILON);
        assertEquals(NeutralModeValue.Brake, ctreConfig.MotorOutput.NeutralMode);
        assertEquals(FEEDFORWARD.p(), ctreConfig.Slot0.kP, EPSILON);
        assertEquals(FEEDFORWARD.i(), ctreConfig.Slot0.kI, EPSILON);
        assertEquals(FEEDFORWARD.d(), ctreConfig.Slot0.kD, EPSILON);
        assertEquals(FEEDFORWARD.s(), ctreConfig.Slot0.kS, EPSILON);
        assertEquals(FEEDFORWARD.v(), ctreConfig.Slot0.kV, EPSILON);
        assertEquals(FEEDFORWARD.a(), ctreConfig.Slot0.kA, EPSILON);
        assertEquals(FEEDFORWARD.g(), ctreConfig.Slot0.kG, EPSILON);
        assertEquals(100.0, ctreConfig.MotionMagic.MotionMagicCruiseVelocity, EPSILON);
        assertEquals(100.0, ctreConfig.MotionMagic.MotionMagicAcceleration, EPSILON);
    }

    @Test
    void recordEqualityUsesEveryField() {
        MotorConfig first = new MotorConfig(ID, 2.0, FEEDFORWARD);
        MotorConfig same = new MotorConfig(ID, 2.0, FEEDFORWARD);
        MotorConfig differentId = new MotorConfig(TestConfigs.canId(4), 2.0, FEEDFORWARD);

        assertEquals(first, same);
        assertEquals(first.hashCode(), same.hashCode());
        assertFalse(first.equals(differentId));
        assertSame(first.id(), first.withId(ID).id());
    }
}

package frc.lib.sensor.absencoder;

import static frc.lib.units.AngleUnit.Rotations;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.signals.SensorDirectionValue;
import frc.lib.testutil.TestConfigs;
import org.junit.jupiter.api.Test;

class AbsEncoderConfigTest {
    private static final double EPSILON = 1e-9;

    @Test
    void convertsToCanCoderConfiguration() {
        AbsEncoderConfig config = new AbsEncoderConfig(TestConfigs.canId(3), Rotations.of(0.25));

        CANcoderConfiguration ctreConfig = config.getCanCoderConfiguration();

        assertEquals(1.0, ctreConfig.MagnetSensor.AbsoluteSensorDiscontinuityPoint, EPSILON);
        assertEquals(0.25, ctreConfig.MagnetSensor.MagnetOffset, EPSILON);
        assertEquals(SensorDirectionValue.CounterClockwise_Positive, ctreConfig.MagnetSensor.SensorDirection);
    }

    @Test
    void offsetIsConvertedFromRadiansToRotations() {
        AbsEncoderConfig config = new AbsEncoderConfig(TestConfigs.canId(3), frc.lib.units.AngleUnit.Radians.of(Math.PI));

        assertEquals(0.5, config.getCanCoderConfiguration().MagnetSensor.MagnetOffset, EPSILON);
    }

    @Test
    void storesIdAndOffset() {
        AbsEncoderConfig first = new AbsEncoderConfig(TestConfigs.canId(3), Rotations.of(0.25));
        AbsEncoderConfig same = new AbsEncoderConfig(TestConfigs.canId(3), Rotations.of(0.25));
        AbsEncoderConfig different = new AbsEncoderConfig(TestConfigs.canId(4), Rotations.of(0.25));

        assertEquals(first.id(), same.id());
        assertEquals(first.offset().in(Rotations), same.offset().in(Rotations), EPSILON);
        org.junit.jupiter.api.Assertions.assertNotEquals(first.id(), different.id());
    }
}

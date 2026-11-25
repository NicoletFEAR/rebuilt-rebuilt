package frc.lib.sensor.beambreak;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.ctre.phoenix6.configs.CANrangeConfiguration;
import frc.lib.testutil.TestConfigs;
import org.junit.jupiter.api.Test;
import org.wpilib.units.Units;

class BeamBreakConfigTest {
    private static final double EPSILON = 1e-9;

    @Test
    void convertsToCanRangeConfiguration() {
        BeamBreakConfig config = new BeamBreakConfig(
                new BeamBreakDevice.CanRange(TestConfigs.canId(7)), Units.Meters.of(0.42));

        CANrangeConfiguration ctreConfig = config.getCanRangeConfiguration();

        assertEquals(0.42, ctreConfig.ProximityParams.ProximityThreshold, EPSILON);
    }

    @Test
    void storesDeviceAndThreshold() {
        BeamBreakDevice device = new BeamBreakDevice.DigitalInput(2);
        BeamBreakConfig config = new BeamBreakConfig(device, Units.Meters.of(0.1));

        assertEquals(device, config.device());
        assertEquals(0.1, config.proximityThreshold().in(Units.Meters), EPSILON);
    }

    @Test
    void recordEqualityUsesDeviceAndThreshold() {
        BeamBreakConfig first = new BeamBreakConfig(
                new BeamBreakDevice.CanRange(TestConfigs.canId(7)), Units.Meters.of(0.42));
        BeamBreakConfig same = new BeamBreakConfig(
                new BeamBreakDevice.CanRange(TestConfigs.canId(7)), Units.Meters.of(0.42));
        BeamBreakConfig different = new BeamBreakConfig(
                new BeamBreakDevice.CanRange(TestConfigs.canId(8)), Units.Meters.of(0.42));

        assertEquals(first, same);
        assertEquals(first.hashCode(), same.hashCode());
        org.junit.jupiter.api.Assertions.assertNotEquals(first, different);
    }
}

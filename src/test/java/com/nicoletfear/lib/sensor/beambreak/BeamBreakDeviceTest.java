package com.nicoletfear.lib.sensor.beambreak;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.nicoletfear.lib.testutil.TestConfigs;
import org.junit.jupiter.api.Test;

class BeamBreakDeviceTest {
    @Test
    void canRangeDeviceStoresId() {
        BeamBreakDevice device = new BeamBreakDevice.CanRange(TestConfigs.canId(7));

        BeamBreakDevice.CanRange canRange = assertInstanceOf(BeamBreakDevice.CanRange.class, device);
        assertEquals(TestConfigs.canId(7), canRange.id());
    }

    @Test
    void digitalInputDeviceStoresId() {
        BeamBreakDevice device = new BeamBreakDevice.DigitalInput(3);

        BeamBreakDevice.DigitalInput digitalInput = assertInstanceOf(BeamBreakDevice.DigitalInput.class, device);
        assertEquals(3, digitalInput.id());
    }

    @Test
    void recordEqualityUsesDeviceData() {
        assertEquals(
                new BeamBreakDevice.CanRange(TestConfigs.canId(7)), new BeamBreakDevice.CanRange(TestConfigs.canId(7)));
        assertEquals(new BeamBreakDevice.DigitalInput(2), new BeamBreakDevice.DigitalInput(2));
        assertNotEquals(new BeamBreakDevice.DigitalInput(2), new BeamBreakDevice.DigitalInput(3));
        assertNotEquals(
                new BeamBreakDevice.CanRange(TestConfigs.canId(7)), new BeamBreakDevice.CanRange(TestConfigs.canId(8)));
    }
}

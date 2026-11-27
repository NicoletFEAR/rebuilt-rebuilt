package com.nicoletfear.lib.led;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.nicoletfear.lib.CanId;
import com.nicoletfear.lib.testutil.TestConfigs;
import org.junit.jupiter.api.Test;

class LedConfigTest {
    @Test
    void storesIdAndLedCount() {
        CanId id = TestConfigs.canId(1);
        LedConfig config = new LedConfig(id, 60);

        assertEquals(id, config.id());
        assertEquals(60, config.ledCount());
    }

    @Test
    void recordEqualityUsesIdAndLedCount() {
        LedConfig first = new LedConfig(TestConfigs.canId(1), 60);
        LedConfig same = new LedConfig(TestConfigs.canId(1), 60);
        LedConfig different = new LedConfig(TestConfigs.canId(1), 30);

        assertEquals(first, same);
        assertEquals(first.hashCode(), same.hashCode());
        org.junit.jupiter.api.Assertions.assertNotEquals(first, different);
    }
}

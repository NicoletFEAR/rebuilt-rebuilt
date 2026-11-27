package com.nicoletfear.lib;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.ctre.phoenix6.CANBus;
import org.junit.jupiter.api.Test;

class CanIdTest {
    @Test
    void storesBusAndId() {
        CANBus bus = new CANBus("test");
        CanId canId = new CanId(bus, 12);

        assertEquals(bus, canId.bus());
        assertEquals("test", canId.bus().getName());
        assertEquals(12, canId.id());
    }

    @Test
    void equalityUsesBusNameAndId() {
        CanId first = new CanId(new CANBus("test"), 1);
        CanId same = new CanId(new CANBus("test"), 1);
        CanId differentId = new CanId(new CANBus("test"), 2);
        CanId differentBus = new CanId(new CANBus("other"), 1);

        assertEquals(first, same);
        assertEquals(first.hashCode(), same.hashCode());
        assertNotEquals(first, differentId);
        assertNotEquals(first, differentBus);
    }
}

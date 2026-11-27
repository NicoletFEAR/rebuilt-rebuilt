package com.nicoletfear.lib.testutil;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.nicoletfear.lib.CanId;
import com.nicoletfear.lib.motor.FeedforwardValues;
import com.nicoletfear.lib.motor.MotorConfig;

/** Shared, hardware-free values for tests. */
public final class TestConfigs {
    /** A fake CAN bus that never touches hardware. */
    public static final CANBus BUS = new CANBus("test");

    private TestConfigs() {}

    public static CanId canId(int id) {
        return new CanId(BUS, id);
    }

    public static FeedforwardValues feedforward() {
        return new FeedforwardValues(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);
    }

    public static MotorConfig motorConfig(int id) {
        return new MotorConfig(canId(id), 1.0, feedforward());
    }

    public static MotorConfig followerConfig(int id) {
        return new MotorConfig(canId(id), 1.0, feedforward(), MotorAlignmentValue.Opposed);
    }
}

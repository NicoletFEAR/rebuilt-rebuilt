package com.nicoletfear.lib.module.position;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.nicoletfear.lib.motor.MotorIO;
import com.nicoletfear.lib.testutil.FakeMotorIO;
import com.nicoletfear.lib.testutil.TestConfigs;
import org.junit.jupiter.api.Test;

class MultiLinearPositionModuleTest {
    @Test
    void configuresEachFollowerToFollowTheLeader() {
        FakeMotorIO leader = new FakeMotorIO(TestConfigs.motorConfig(1));
        FakeMotorIO follower = new FakeMotorIO(TestConfigs.followerConfig(2));

        new MultiLinearPositionModule("Test", leader, new MotorIO[] {follower});

        assertEquals(1, follower.followCalls);
        assertSame(leader, follower.followLeader);
        assertEquals(MotorAlignmentValue.Opposed, follower.followAlignment);
    }

    @Test
    void rejectsFollowersWithoutAlignment() {
        FakeMotorIO leader = new FakeMotorIO(TestConfigs.motorConfig(1));
        FakeMotorIO follower = new FakeMotorIO(TestConfigs.motorConfig(2));

        assertThrows(
                IllegalArgumentException.class,
                () -> new MultiLinearPositionModule("Test", leader, new MotorIO[] {follower}));
    }

    @Test
    void updatesLeaderAndEveryFollower() {
        FakeMotorIO leader = new FakeMotorIO(TestConfigs.motorConfig(1));
        FakeMotorIO first = new FakeMotorIO(TestConfigs.followerConfig(2));
        FakeMotorIO second = new FakeMotorIO(TestConfigs.followerConfig(3));
        MultiLinearPositionModule module = new MultiLinearPositionModule("Test", leader, new MotorIO[] {first, second});

        module.updateInputs();

        assertEquals(1, leader.updateInputsCalls);
        assertEquals(1, first.updateInputsCalls);
        assertEquals(1, second.updateInputsCalls);
    }
}

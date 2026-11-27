package com.nicoletfear.lib.module.velocity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.nicoletfear.lib.motor.MotorIO;
import com.nicoletfear.lib.testutil.FakeMotorIO;
import com.nicoletfear.lib.testutil.TestConfigs;
import org.junit.jupiter.api.Test;

class MultiAngularVelocityModuleTest {
    @Test
    void configuresEachFollowerToFollowTheLeader() {
        FakeMotorIO leader = new FakeMotorIO(TestConfigs.motorConfig(1));
        FakeMotorIO first = new FakeMotorIO(TestConfigs.followerConfig(2));
        FakeMotorIO second = new FakeMotorIO(TestConfigs.followerConfig(3));

        new MultiAngularVelocityModule("Test", leader, new MotorIO[] {first, second});

        assertEquals(1, first.followCalls);
        assertSame(leader, first.followLeader);
        assertEquals(MotorAlignmentValue.Opposed, first.followAlignment);

        assertEquals(1, second.followCalls);
        assertSame(leader, second.followLeader);
        assertEquals(MotorAlignmentValue.Opposed, second.followAlignment);
    }

    @Test
    void rejectsFollowersWithoutAlignment() {
        FakeMotorIO leader = new FakeMotorIO(TestConfigs.motorConfig(1));
        FakeMotorIO follower = new FakeMotorIO(TestConfigs.motorConfig(2));

        assertThrows(
                IllegalArgumentException.class,
                () -> new MultiAngularVelocityModule("Test", leader, new MotorIO[] {follower}));
    }

    @Test
    void updatesLeaderAndEveryFollower() {
        FakeMotorIO leader = new FakeMotorIO(TestConfigs.motorConfig(1));
        FakeMotorIO first = new FakeMotorIO(TestConfigs.followerConfig(2));
        FakeMotorIO second = new FakeMotorIO(TestConfigs.followerConfig(3));
        MultiAngularVelocityModule module =
                new MultiAngularVelocityModule("Test", leader, new MotorIO[] {first, second});

        module.updateInputs();

        assertEquals(1, leader.updateInputsCalls);
        assertEquals(1, first.updateInputsCalls);
        assertEquals(1, second.updateInputsCalls);
    }
}

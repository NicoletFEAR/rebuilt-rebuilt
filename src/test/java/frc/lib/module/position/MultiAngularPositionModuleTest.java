package frc.lib.module.position;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ctre.phoenix6.signals.MotorAlignmentValue;
import frc.lib.motor.MotorIO;
import frc.lib.testutil.FakeMotorIO;
import frc.lib.testutil.TestConfigs;
import org.junit.jupiter.api.Test;

class MultiAngularPositionModuleTest {
    @Test
    void configuresEachFollowerToFollowTheLeader() {
        FakeMotorIO leader = new FakeMotorIO(TestConfigs.motorConfig(1));
        FakeMotorIO first = new FakeMotorIO(TestConfigs.followerConfig(2));
        FakeMotorIO second = new FakeMotorIO(TestConfigs.followerConfig(3));

        new MultiAngularPositionModule("Test", leader, new MotorIO[] {first, second});

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

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new MultiAngularPositionModule("Test", leader, new MotorIO[] {follower}));

        assertEquals(
                "Every follower config must have the `alignment` field set", exception.getMessage());
    }

    @Test
    void updatesLeaderAndEveryFollower() {
        FakeMotorIO leader = new FakeMotorIO(TestConfigs.motorConfig(1));
        FakeMotorIO first = new FakeMotorIO(TestConfigs.followerConfig(2));
        FakeMotorIO second = new FakeMotorIO(TestConfigs.followerConfig(3));
        MultiAngularPositionModule module =
                new MultiAngularPositionModule("Test", leader, new MotorIO[] {first, second});

        module.updateInputs();

        assertEquals(1, leader.updateInputsCalls);
        assertEquals(1, first.updateInputsCalls);
        assertEquals(1, second.updateInputsCalls);
    }

    @Test
    void worksWithoutFollowers() {
        FakeMotorIO leader = new FakeMotorIO(TestConfigs.motorConfig(1));
        MultiAngularPositionModule module = new MultiAngularPositionModule("Test", leader, new MotorIO[0]);

        module.updateInputs();

        assertEquals(1, leader.updateInputsCalls);
    }
}

package com.nicoletfear.lib.module.velocity;

import static com.nicoletfear.lib.units.AngularVelocityUnit.RadiansPerSecond;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.nicoletfear.lib.ToleranceConstants;
import com.nicoletfear.lib.testutil.FakeMotorIO;
import com.nicoletfear.lib.testutil.TestConfigs;
import org.junit.jupiter.api.Test;

class AngularVelocityModuleTest {
    @Test
    void setVelocitySetpointForwardsToTheMotor() {
        FakeMotorIO motor = new FakeMotorIO(TestConfigs.motorConfig(1));
        AngularVelocityModule module = new AngularVelocityModule("Test", motor);

        module.setVelocitySetpoint(RadiansPerSecond.of(4.5));

        assertEquals(1, motor.velocitySetpointCalls);
        assertEquals(4.5, motor.velocitySetpoint.in(RadiansPerSecond), ToleranceConstants.EPSILON);
    }

    @Test
    void setDynamicVelocityForwardsToTheMotor() {
        FakeMotorIO motor = new FakeMotorIO(TestConfigs.motorConfig(1));
        AngularVelocityModule module = new AngularVelocityModule("Test", motor);

        module.setDynamicVelocity(RadiansPerSecond.of(-2.0));

        assertEquals(1, motor.dynamicVelocityCalls);
        assertEquals(-2.0, motor.dynamicVelocity.in(RadiansPerSecond), ToleranceConstants.EPSILON);
    }

    @Test
    void getVelocityReturnsUpdatedMotorVelocity() {
        FakeMotorIO motor = new FakeMotorIO(TestConfigs.motorConfig(1));
        AngularVelocityModule module = new AngularVelocityModule("Test", motor);
        motor.inputUpdater = inputs -> inputs.velocity = RadiansPerSecond.of(6.25);

        module.updateInputs();

        assertEquals(6.25, module.getVelocity().in(RadiansPerSecond), ToleranceConstants.EPSILON);
    }
}

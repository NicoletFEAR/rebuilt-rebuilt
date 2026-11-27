package com.nicoletfear.lib.module.velocity;

import static com.nicoletfear.lib.units.AngleUnit.Radians;
import static com.nicoletfear.lib.units.AngularVelocityUnit.RadiansPerSecond;
import static com.nicoletfear.lib.units.DistanceUnit.Meters;
import static com.nicoletfear.lib.units.VelocityUnit.MetersPerSecond;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.nicoletfear.lib.testutil.FakeMotorIO;
import com.nicoletfear.lib.testutil.TestConfigs;
import org.junit.jupiter.api.Test;

class LinearVelocityModuleTest {
    private static final double EPSILON = 1e-9;

    @Test
    void setVelocitySetpointConvertsMetersPerSecondToSensorRadiansPerSecond() {
        FakeMotorIO motor = new FakeMotorIO(TestConfigs.motorConfig(1));
        LinearVelocityModule module = new LinearVelocityModule("Test", motor);

        module.setVelocitySetpoint(MetersPerSecond.of(3.25));

        assertEquals(1, motor.velocitySetpointCalls);
        assertEquals(3.25, motor.velocitySetpoint.in(RadiansPerSecond), EPSILON);
    }

    @Test
    void setDynamicVelocityConvertsMetersPerSecondToSensorRadiansPerSecond() {
        FakeMotorIO motor = new FakeMotorIO(TestConfigs.motorConfig(1));
        LinearVelocityModule module = new LinearVelocityModule("Test", motor);

        module.setDynamicVelocity(MetersPerSecond.of(-1.5));

        assertEquals(1, motor.dynamicVelocityCalls);
        assertEquals(-1.5, motor.dynamicVelocity.in(RadiansPerSecond), EPSILON);
    }

    @Test
    void getPositionConvertsSensorRadiansToMeters() {
        FakeMotorIO motor = new FakeMotorIO(TestConfigs.motorConfig(1));
        LinearVelocityModule module = new LinearVelocityModule("Test", motor);
        motor.inputUpdater = inputs -> inputs.position = Radians.of(2.75);

        module.updateInputs();

        assertEquals(2.75, module.getPosition().in(Meters), EPSILON);
    }

    @Test
    void getVelocityConvertsSensorRadiansPerSecondToMetersPerSecond() {
        FakeMotorIO motor = new FakeMotorIO(TestConfigs.motorConfig(1));
        LinearVelocityModule module = new LinearVelocityModule("Test", motor);
        motor.inputUpdater = inputs -> inputs.velocity = RadiansPerSecond.of(5.5);

        module.updateInputs();

        assertEquals(5.5, module.getVelocity().in(MetersPerSecond), EPSILON);
    }
}

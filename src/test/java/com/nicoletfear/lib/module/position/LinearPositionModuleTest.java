package com.nicoletfear.lib.module.position;

import static com.nicoletfear.lib.units.AngleUnit.Radians;
import static com.nicoletfear.lib.units.DistanceUnit.Meters;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.nicoletfear.lib.testutil.FakeMotorIO;
import com.nicoletfear.lib.testutil.TestConfigs;
import org.junit.jupiter.api.Test;

class LinearPositionModuleTest {
    private static final double EPSILON = 1e-9;

    @Test
    void setPositionSetpointConvertsMetersToSensorRadians() {
        FakeMotorIO motor = new FakeMotorIO(TestConfigs.motorConfig(1));
        LinearPositionModule module = new LinearPositionModule("Test", motor);

        module.setPositionSetpoint(Meters.of(2.5));

        assertEquals(1, motor.positionSetpointCalls);
        assertEquals(2.5, motor.positionSetpoint.in(Radians), EPSILON);
    }

    @Test
    void setDynamicPositionConvertsMetersToSensorRadians() {
        FakeMotorIO motor = new FakeMotorIO(TestConfigs.motorConfig(1));
        LinearPositionModule module = new LinearPositionModule("Test", motor);

        module.setDynamicPosition(Meters.of(-1.25));

        assertEquals(1, motor.dynamicPositionCalls);
        assertEquals(-1.25, motor.dynamicPosition.in(Radians), EPSILON);
    }

    @Test
    void getPositionConvertsSensorRadiansToMeters() {
        FakeMotorIO motor = new FakeMotorIO(TestConfigs.motorConfig(1));
        LinearPositionModule module = new LinearPositionModule("Test", motor);
        motor.inputUpdater = inputs -> inputs.position = Radians.of(3.5);

        module.updateInputs();

        assertEquals(3.5, module.getPosition().in(Meters), EPSILON);
    }
}

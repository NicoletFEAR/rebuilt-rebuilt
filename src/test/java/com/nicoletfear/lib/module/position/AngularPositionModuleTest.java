package com.nicoletfear.lib.module.position;

import static com.nicoletfear.lib.units.AngleUnit.Radians;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.nicoletfear.lib.ToleranceConstants;
import com.nicoletfear.lib.testutil.FakeMotorIO;
import com.nicoletfear.lib.testutil.TestConfigs;
import org.junit.jupiter.api.Test;

class AngularPositionModuleTest {
    @Test
    void setPositionSetpointForwardsToTheMotor() {
        FakeMotorIO motor = new FakeMotorIO(TestConfigs.motorConfig(1));
        AngularPositionModule module = new AngularPositionModule("Test", motor);

        module.setPositionSetpoint(Radians.of(1.25));

        assertEquals(1, motor.positionSetpointCalls);
        assertEquals(1.25, motor.positionSetpoint.in(Radians), ToleranceConstants.EPSILON);
    }

    @Test
    void setDynamicPositionForwardsToTheMotor() {
        FakeMotorIO motor = new FakeMotorIO(TestConfigs.motorConfig(1));
        AngularPositionModule module = new AngularPositionModule("Test", motor);

        module.setDynamicPosition(Radians.of(2.5));

        assertEquals(1, motor.dynamicPositionCalls);
        assertEquals(2.5, motor.dynamicPosition.in(Radians), ToleranceConstants.EPSILON);
    }

    @Test
    void getPositionReturnsUpdatedMotorPosition() {
        FakeMotorIO motor = new FakeMotorIO(TestConfigs.motorConfig(1));
        AngularPositionModule module = new AngularPositionModule("Test", motor);
        motor.inputUpdater = inputs -> inputs.position = Radians.of(3.75);

        module.updateInputs();

        assertEquals(3.75, module.getPosition().in(Radians), ToleranceConstants.EPSILON);
    }

    @Test
    void getRotationConvertsPositionToRotation2d() {
        FakeMotorIO motor = new FakeMotorIO(TestConfigs.motorConfig(1));
        AngularPositionModule module = new AngularPositionModule("Test", motor);
        motor.inputUpdater = inputs -> inputs.position = Radians.of(1.5);

        module.updateInputs();

        assertEquals(1.5, module.getRotation().getRadians(), ToleranceConstants.EPSILON);
    }
}

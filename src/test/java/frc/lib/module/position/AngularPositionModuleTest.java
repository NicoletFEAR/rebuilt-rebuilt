package frc.lib.module.position;

import static frc.lib.units.AngleUnit.Radians;
import static org.junit.jupiter.api.Assertions.assertEquals;

import frc.lib.testutil.FakeMotorIO;
import frc.lib.testutil.TestConfigs;
import org.junit.jupiter.api.Test;

class AngularPositionModuleTest {
    private static final double EPSILON = 1e-9;

    @Test
    void setPositionSetpointForwardsToTheMotor() {
        FakeMotorIO motor = new FakeMotorIO(TestConfigs.motorConfig(1));
        AngularPositionModule module = new AngularPositionModule("Test", motor);

        module.setPositionSetpoint(Radians.of(1.25));

        assertEquals(1, motor.positionSetpointCalls);
        assertEquals(1.25, motor.positionSetpoint.in(Radians), EPSILON);
    }

    @Test
    void setDynamicPositionForwardsToTheMotor() {
        FakeMotorIO motor = new FakeMotorIO(TestConfigs.motorConfig(1));
        AngularPositionModule module = new AngularPositionModule("Test", motor);

        module.setDynamicPosition(Radians.of(2.5));

        assertEquals(1, motor.dynamicPositionCalls);
        assertEquals(2.5, motor.dynamicPosition.in(Radians), EPSILON);
    }

    @Test
    void getPositionReturnsUpdatedMotorPosition() {
        FakeMotorIO motor = new FakeMotorIO(TestConfigs.motorConfig(1));
        AngularPositionModule module = new AngularPositionModule("Test", motor);
        motor.inputUpdater = inputs -> inputs.position = Radians.of(3.75);

        module.updateInputs();

        assertEquals(3.75, module.getPosition().in(Radians), EPSILON);
    }

    @Test
    void getRotationConvertsPositionToRotation2d() {
        FakeMotorIO motor = new FakeMotorIO(TestConfigs.motorConfig(1));
        AngularPositionModule module = new AngularPositionModule("Test", motor);
        motor.inputUpdater = inputs -> inputs.position = Radians.of(1.5);

        module.updateInputs();

        assertEquals(1.5, module.getRotation().getRadians(), EPSILON);
    }
}

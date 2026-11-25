package frc.lib.module;

import static frc.lib.units.AngleUnit.Radians;
import static frc.lib.units.AngularVelocityUnit.RadiansPerSecond;
import static frc.lib.units.VoltageUnit.Volts;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import frc.lib.motor.MotorIO;
import frc.lib.motor.MotorIOInputsAutoLogged;
import frc.lib.testutil.FakeMotorIO;
import frc.lib.testutil.TestConfigs;
import org.junit.jupiter.api.Test;

class ModuleTest {
    private static final double EPSILON = 1e-9;

    private static final class TestModule extends Module {
        TestModule(String name, MotorIO motor) {
            super(name, motor);
        }

        MotorIOInputsAutoLogged inputs() {
            return inputs;
        }

        String name() {
            return name;
        }
    }

    @Test
    void exposesNameAndMotorToSubclasses() {
        FakeMotorIO motor = new FakeMotorIO(TestConfigs.motorConfig(1));
        TestModule module = new TestModule("Test Module", motor);

        assertEquals("Test Module", module.name());
        assertSame(motor, module.motor);
    }

    @Test
    void updateInputsUpdatesAndReusesTheSameInputsObject() {
        FakeMotorIO motor = new FakeMotorIO(TestConfigs.motorConfig(1));
        TestModule module = new TestModule("Test Module", motor);
        motor.inputUpdater = inputs -> {
            inputs.connected = true;
            inputs.position = Radians.of(1.5);
            inputs.velocity = RadiansPerSecond.of(2.5);
        };

        module.updateInputs();
        MotorIOInputsAutoLogged firstInputs = module.inputs();
        module.updateInputs();

        assertEquals(2, motor.updateInputsCalls);
        assertSame(firstInputs, motor.lastInputs);
        assertSame(firstInputs, module.inputs());
        assertEquals(1.5, module.inputs().position.in(Radians), EPSILON);
        assertEquals(2.5, module.inputs().velocity.in(RadiansPerSecond), EPSILON);
    }

    @Test
    void setVoltageForwardsToTheMotor() {
        FakeMotorIO motor = new FakeMotorIO(TestConfigs.motorConfig(1));
        TestModule module = new TestModule("Test Module", motor);

        module.setVoltage(Volts.of(7.5));

        assertEquals(1, motor.voltageCalls);
        assertEquals(7.5, motor.voltage.in(Volts), EPSILON);
    }
}

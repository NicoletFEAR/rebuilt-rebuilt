package frc.lib.motor;

import static frc.lib.units.AngleUnit.Radians;
import static frc.lib.units.AngularVelocityUnit.RadiansPerSecond;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;

import com.ctre.phoenix6.signals.MotorAlignmentValue;
import frc.lib.testutil.TestConfigs;
import frc.lib.units.Angle;
import frc.lib.units.AngularVelocity;
import frc.lib.units.Voltage;
import org.junit.jupiter.api.Test;

class MotorIOTest {
    private static final double EPSILON = 1e-9;

    /** A motor that only records the dynamic methods, so the default setters can be tested. */
    private static final class DynamicOnlyMotorIO extends MotorIO {
        Angle dynamicPosition;
        AngularVelocity dynamicVelocity;

        DynamicOnlyMotorIO(MotorConfig config) {
            super(config);
        }

        @Override
        public void updateInputs(MotorIOInputs inputs) {}

        @Override
        public void follow(MotorIO leader, MotorAlignmentValue alignment) {}

        @Override
        public void setDynamicPosition(Angle position) {
            dynamicPosition = position;
        }

        @Override
        public void setDynamicVelocity(AngularVelocity velocity) {
            dynamicVelocity = velocity;
        }

        @Override
        public void setVoltage(Voltage voltage) {}
    }

    @Test
    void getConfigReturnsTheConfigPassedToTheConstructor() {
        MotorConfig config = TestConfigs.motorConfig(1);
        DynamicOnlyMotorIO motor = new DynamicOnlyMotorIO(config);

        assertSame(config, motor.getConfig());
    }

    @Test
    void setPositionSetpointDelegatesToSetDynamicPosition() {
        DynamicOnlyMotorIO motor = new DynamicOnlyMotorIO(TestConfigs.motorConfig(1));

        motor.setPositionSetpoint(Radians.of(1.25));

        assertEquals(1.25, motor.dynamicPosition.in(Radians), EPSILON);
    }

    @Test
    void setVelocitySetpointDelegatesToSetDynamicVelocity() {
        DynamicOnlyMotorIO motor = new DynamicOnlyMotorIO(TestConfigs.motorConfig(1));

        motor.setVelocitySetpoint(RadiansPerSecond.of(3.5));

        assertEquals(3.5, motor.dynamicVelocity.in(RadiansPerSecond), EPSILON);
    }

    @Test
    void inputsHaveExpectedDefaults() {
        MotorIO.MotorIOInputs inputs = new MotorIO.MotorIOInputs();

        assertFalse(inputs.connected);
        assertEquals(0.0, inputs.position.in(Radians), EPSILON);
        assertEquals(0.0, inputs.velocity.in(RadiansPerSecond), EPSILON);
        assertEquals(0.0, inputs.acceleration.in(frc.lib.units.AngularAccelerationUnit.RadiansPerSecondSquared), EPSILON);
        assertEquals(0.0, inputs.voltage.in(frc.lib.units.VoltageUnit.Volts), EPSILON);
        assertEquals(0.0, inputs.statorCurrent.in(frc.lib.units.CurrentUnit.Amps), EPSILON);
        assertEquals(0.0, inputs.supplyCurrent.in(frc.lib.units.CurrentUnit.Amps), EPSILON);
        assertEquals(294.0, inputs.temperature.in(frc.lib.units.TemperatureUnit.Kelvin), EPSILON);
    }

    @Test
    void inputsInstancesAreIndependent() {
        MotorIO.MotorIOInputs first = new MotorIO.MotorIOInputs();
        MotorIO.MotorIOInputs second = new MotorIO.MotorIOInputs();

        first.connected = true;
        first.position = Radians.of(2.0);

        assertFalse(second.connected);
        assertEquals(0.0, second.position.in(Radians), EPSILON);
    }
}

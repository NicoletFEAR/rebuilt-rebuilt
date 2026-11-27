package com.nicoletfear.lib.motor;

import static com.nicoletfear.lib.units.AngleUnit.Radians;
import static com.nicoletfear.lib.units.AngularVelocityUnit.RadiansPerSecond;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;

import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.nicoletfear.lib.ToleranceConstants;
import com.nicoletfear.lib.testutil.TestConfigs;
import com.nicoletfear.lib.units.Angle;
import com.nicoletfear.lib.units.AngularVelocity;
import com.nicoletfear.lib.units.Voltage;
import org.junit.jupiter.api.Test;

class MotorIOTest {
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

        assertEquals(1.25, motor.dynamicPosition.in(Radians), ToleranceConstants.EPSILON);
    }

    @Test
    void setVelocitySetpointDelegatesToSetDynamicVelocity() {
        DynamicOnlyMotorIO motor = new DynamicOnlyMotorIO(TestConfigs.motorConfig(1));

        motor.setVelocitySetpoint(RadiansPerSecond.of(3.5));

        assertEquals(3.5, motor.dynamicVelocity.in(RadiansPerSecond), ToleranceConstants.EPSILON);
    }

    @Test
    void inputsHaveExpectedDefaults() {
        MotorIO.MotorIOInputs inputs = new MotorIO.MotorIOInputs();

        assertFalse(inputs.connected);
        assertEquals(0.0, inputs.position.in(Radians), ToleranceConstants.EPSILON);
        assertEquals(0.0, inputs.velocity.in(RadiansPerSecond), ToleranceConstants.EPSILON);
        assertEquals(
                0.0,
                inputs.acceleration.in(com.nicoletfear.lib.units.AngularAccelerationUnit.RadiansPerSecondSquared),
                ToleranceConstants.EPSILON);
        assertEquals(0.0, inputs.voltage.in(com.nicoletfear.lib.units.VoltageUnit.Volts), ToleranceConstants.EPSILON);
        assertEquals(
                0.0, inputs.statorCurrent.in(com.nicoletfear.lib.units.CurrentUnit.Amps), ToleranceConstants.EPSILON);
        assertEquals(
                0.0, inputs.supplyCurrent.in(com.nicoletfear.lib.units.CurrentUnit.Amps), ToleranceConstants.EPSILON);
        assertEquals(
                294.0,
                inputs.temperature.in(com.nicoletfear.lib.units.TemperatureUnit.Kelvin),
                ToleranceConstants.EPSILON);
    }

    @Test
    void inputsInstancesAreIndependent() {
        MotorIO.MotorIOInputs first = new MotorIO.MotorIOInputs();
        MotorIO.MotorIOInputs second = new MotorIO.MotorIOInputs();

        first.connected = true;
        first.position = Radians.of(2.0);

        assertFalse(second.connected);
        assertEquals(0.0, second.position.in(Radians), ToleranceConstants.EPSILON);
    }
}

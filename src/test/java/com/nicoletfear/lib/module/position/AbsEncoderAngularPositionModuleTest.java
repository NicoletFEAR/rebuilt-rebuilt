package com.nicoletfear.lib.module.position;

import static com.nicoletfear.lib.units.AngleUnit.Radians;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.nicoletfear.lib.testutil.FakeAbsEncoderIO;
import com.nicoletfear.lib.testutil.FakeMotorIO;
import com.nicoletfear.lib.testutil.TestConfigs;
import org.junit.jupiter.api.Test;

class AbsEncoderAngularPositionModuleTest {
    private static final double EPSILON = 1e-9;

    @Test
    void usesAbsoluteEncoderPositionWhenConnected() {
        FakeMotorIO motor = new FakeMotorIO(TestConfigs.motorConfig(1));
        FakeAbsEncoderIO encoder = new FakeAbsEncoderIO();
        AbsEncoderAngularPositionModule module = new AbsEncoderAngularPositionModule("Test", motor, encoder);
        motor.inputUpdater = inputs -> inputs.position = Radians.of(0.5);
        encoder.inputUpdater = inputs -> {
            inputs.connected = true;
            inputs.position = Radians.of(1.75);
        };

        module.updateInputs();

        assertEquals(1.75, module.getPosition().in(Radians), EPSILON);
        assertEquals(1.75, module.getRotation().getRadians(), EPSILON);
    }

    @Test
    void fallsBackToMotorPositionWhenEncoderIsDisconnected() {
        FakeMotorIO motor = new FakeMotorIO(TestConfigs.motorConfig(1));
        FakeAbsEncoderIO encoder = new FakeAbsEncoderIO();
        AbsEncoderAngularPositionModule module = new AbsEncoderAngularPositionModule("Test", motor, encoder);
        motor.inputUpdater = inputs -> inputs.position = Radians.of(0.5);
        encoder.inputUpdater = inputs -> {
            inputs.connected = false;
            inputs.position = Radians.of(1.75);
        };

        module.updateInputs();

        assertEquals(0.5, module.getPosition().in(Radians), EPSILON);
        assertEquals(0.5, module.getRotation().getRadians(), EPSILON);
    }

    @Test
    void updatesBothMotorAndEncoderInputs() {
        FakeMotorIO motor = new FakeMotorIO(TestConfigs.motorConfig(1));
        FakeAbsEncoderIO encoder = new FakeAbsEncoderIO();
        AbsEncoderAngularPositionModule module = new AbsEncoderAngularPositionModule("Test", motor, encoder);

        module.updateInputs();

        assertEquals(1, motor.updateInputsCalls);
        assertEquals(1, encoder.updateInputsCalls);
    }
}

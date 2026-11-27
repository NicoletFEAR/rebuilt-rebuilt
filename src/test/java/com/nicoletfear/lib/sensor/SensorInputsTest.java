package com.nicoletfear.lib.sensor;

import static com.nicoletfear.lib.units.AngleUnit.Radians;
import static com.nicoletfear.lib.units.AngularVelocityUnit.RadiansPerSecond;
import static com.nicoletfear.lib.units.DistanceUnit.Meters;
import static com.nicoletfear.lib.units.TemperatureUnit.Kelvin;
import static com.nicoletfear.lib.units.VoltageUnit.Volts;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.nicoletfear.lib.led.LedIO;
import com.nicoletfear.lib.sensor.absencoder.AbsEncoderIO;
import com.nicoletfear.lib.sensor.beambreak.BeamBreakIO;
import com.nicoletfear.lib.sensor.tof.TofIO;
import com.nicoletfear.lib.sensor.vision.VisionIO;
import org.junit.jupiter.api.Test;
import org.wpilib.math.geometry.Pose2d;

class SensorInputsTest {
    private static final double EPSILON = 1e-9;

    @Test
    void beamBreakInputsHaveExpectedDefaults() {
        BeamBreakIO.BeamBreakIOInputs inputs = new BeamBreakIO.BeamBreakIOInputs();

        assertFalse(inputs.connected);
        assertFalse(inputs.tripped);
    }

    @Test
    void tofInputsHaveExpectedDefaults() {
        TofIO.TofIOInputs inputs = new TofIO.TofIOInputs();

        assertFalse(inputs.connected);
        assertEquals(0.0, inputs.distance.in(Meters), EPSILON);
    }

    @Test
    void visionInputsHaveExpectedDefaults() {
        VisionIO.VisionInputs inputs = new VisionIO.VisionInputs();

        assertFalse(inputs.connected);
        assertFalse(inputs.hasValidPose);
        assertEquals(Pose2d.ZERO, inputs.estimatedPose);
        assertNotNull(inputs.estimatedPose);
    }

    @Test
    void ledInputsHaveExpectedDefaults() {
        LedIO.LedIOInputs inputs = new LedIO.LedIOInputs();

        assertFalse(inputs.connected);
        assertEquals(0.0, inputs.voltage.in(Volts), EPSILON);
        assertEquals(294.0, inputs.temperature.in(Kelvin), EPSILON);
    }

    @Test
    void absEncoderInputsHaveExpectedDefaults() {
        AbsEncoderIO.AbsEncoderIOInputs inputs = new AbsEncoderIO.AbsEncoderIOInputs();

        assertFalse(inputs.connected);
        assertEquals(0.0, inputs.position.in(Radians), EPSILON);
        assertEquals(0.0, inputs.velocity.in(RadiansPerSecond), EPSILON);
    }
}

package com.nicoletfear.robot.subsystems.launcher;

import static com.nicoletfear.lib.units.AngleUnit.Rotations;
import static com.nicoletfear.lib.units.VoltageUnit.Volts;

import com.nicoletfear.lib.ToleranceConstants;
import com.nicoletfear.lib.module.position.AbsEncoderAngularPositionModule;
import com.nicoletfear.lib.motor.FeedforwardValues;
import com.nicoletfear.lib.motor.MotorConfig;
import com.nicoletfear.lib.motor.MotorIO;
import com.nicoletfear.lib.sensor.absencoder.AbsEncoderConfig;
import com.nicoletfear.lib.sensor.absencoder.AbsEncoderIO;
import com.nicoletfear.lib.statemachine.State;
import com.nicoletfear.lib.statemachine.StateMachine;
import com.nicoletfear.lib.units.Angle;
import com.nicoletfear.robot.subsystems.DeviceIds;

/**
 * Launcher hood that adjusts the launch angle.
 *
 * <p>The hood angle is controlled by an angular position module whose feedback comes from an
 * absolute encoder (falling back to the motor's relative position when the encoder is
 * disconnected).
 */
final class Hood extends StateMachine<Hood.HoodState> {
    private final AbsEncoderAngularPositionModule module;

    /** Creates the hood. */
    Hood() {
        super("Launcher/Hood", HoodState.OFF);
        module = new AbsEncoderAngularPositionModule(
                name,
                MotorIO.buildMotor(new MotorConfig(
                        DeviceIds.HOOD, HoodConstants.SENSOR_TO_MECHANISM_RATIO, HoodConstants.FEEDFORWARD_VALUES)),
                AbsEncoderIO.buildAbsEncoder(new AbsEncoderConfig(DeviceIds.HOOD_ENCODER, HoodConstants.HOOD_OFFSET)));
    }

    /** States of the hood. */
    static sealed interface HoodState extends State permits HoodState.Off, HoodState.HoldPosition {
        /** Hood is off (zero voltage). */
        static record Off() implements HoodState {}

        /** Hood is commanded to hold the given angle. */
        static record HoldPosition(Angle position) implements HoodState {}

        /** Shared constant for the {@link Off} record to avoid unnecessary allocations. */
        static final Off OFF = new Off();
    }

    /** Updates the motor and encoder inputs. */
    void updateInputs() {
        module.updateInputs();
    }

    /** Applies the behavior for the current state. */
    void execute() {
        switch (state) {
            case HoodState.Off() -> module.setVoltage(Volts.of(0.0));
            case HoodState.HoldPosition(Angle position) -> module.setDynamicPosition(position);
        }
    }

    /** Logs any desired outputs from the hood. */
    void logOutputs() {
        logState();
    }

    /**
     * Returns whether the hood is in the {@link HoodState.HoldPosition} state and the measured angle
     * is within {@link ToleranceConstants#ANGULAR_POSITION_SETPOINT_TOLERANCE} of the commanded
     * angle.
     */
    boolean isAtLaunchPosition() {
        return switch (state) {
            case HoodState.Off() -> false;
            case HoodState.HoldPosition(Angle position) ->
                position.equal(module.getPosition(), ToleranceConstants.ANGULAR_POSITION_SETPOINT_TOLERANCE);
        };
    }

    /** Constants for the hood. */
    private static final class HoodConstants {

        /** Absolute encoder offset for the hood, in rotations. */
        private static final Angle HOOD_OFFSET = Rotations.of(-0.25439453125);

        /** Feedforward and feedback gains for the hood motor. */
        private static final FeedforwardValues FEEDFORWARD_VALUES = new FeedforwardValues(0.1, 0.0, 0.0, 0.1, 0.1, 0.1);

        /** Sensor-to-mechanism ratio for the hood. */
        private static final double SENSOR_TO_MECHANISM_RATIO = 30.0;

        private HoodConstants() {
            /* Keep this constructor empty */
        }
    }
}

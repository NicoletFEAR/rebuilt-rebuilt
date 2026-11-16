package frc.robot.subsystems.launcher;

import static frc.lib.units.AngleUnit.Rotations;
import static frc.lib.units.VoltageUnit.Volts;

import frc.lib.ToleranceConstants;
import frc.lib.module.position.AbsEncoderAngularPositionModule;
import frc.lib.motor.FeedforwardValues;
import frc.lib.motor.MotorIO;
import frc.lib.sensor.absencoder.AbsEncoderIO;
import frc.lib.statemachine.State;
import frc.lib.statemachine.StateMachine;
import frc.lib.units.Angle;

/**
 * Launcher hood that adjusts the launch angle.
 *
 * <p>The hood angle is controlled by an angular position module whose feedback comes from an
 * absolute encoder (falling back to the motor's relative position when the encoder is
 * disconnected).
 */
public final class Hood extends StateMachine<Hood.HoodState> {
    private final AbsEncoderAngularPositionModule module;

    /**
     * Creates the hood.
     *
     * @param motor interface used to communicate with the motor
     * @param canCoder interface used to communicate with the absolute encoder
     */
    Hood(MotorIO motor, AbsEncoderIO canCoder) {
        super("Launcher/Hood", HoodState.OFF);
        module =
                new AbsEncoderAngularPositionModule(
                        name, motor, canCoder, HoodConstants.ROTOR_TO_MECHANISM_RATIO);
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

    /**
     * Updates the motor and encoder inputs, applies the behavior for the current state, and logs the
     * state.
     */
    void periodic() {
        module.updateInputs();

        switch (state) {
            case HoodState.Off() -> module.setVoltage(Volts.of(0.0));
            case HoodState.HoldPosition(Angle position) -> module.setPositionSetpoint(position);
        }

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
                    position.equal(
                            module.getPosition(), ToleranceConstants.ANGULAR_POSITION_SETPOINT_TOLERANCE);
        };
    }

    /** Constants for the hood. */
    public static final class HoodConstants {

        /** Absolute encoder offset for the hood, in rotations. */
        public static final Angle HOOD_OFFSET = Rotations.of(-0.25439453125);

        /** Feedforward and feedback gains for the hood motor. */
        public static final FeedforwardValues FEEDFORWARD_VALUES =
                new FeedforwardValues(5.0, 0.0, 0.0, 0.0, 0.0, 0.0);

        /** Rotor-to-mechanism ratio for the hood. */
        private static final double ROTOR_TO_MECHANISM_RATIO = 30.0;

        private HoodConstants() {
            /* Keep this constructor empty */
        }
    }
}

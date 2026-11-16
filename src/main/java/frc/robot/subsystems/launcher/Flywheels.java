package frc.robot.subsystems.launcher;

import static frc.lib.units.AngularVelocityUnit.RadiansPerSecond;
import static frc.lib.units.VoltageUnit.Volts;

import frc.lib.ToleranceConstants;
import frc.lib.module.velocity.MultiAngularVelocityModule;
import frc.lib.motor.FeedforwardValues;
import frc.lib.motor.MotorIO;
import frc.lib.statemachine.State;
import frc.lib.statemachine.StateMachine;
import frc.lib.units.AngularVelocity;

/**
 * Launcher flywheels.
 *
 * <p>A leader motor drives the flywheels; one or more follower motors are configured to follow it.
 * Each state maps to either zero voltage, the idle velocity, or a commanded launch velocity.
 */
public final class Flywheels extends StateMachine<Flywheels.FlywheelState> {
    private final MultiAngularVelocityModule module;

    /**
     * Creates the flywheels.
     *
     * @param motor interface used to communicate with the leader motor
     * @param followers interfaces used to communicate with the follower motors
     */
    Flywheels(MotorIO motor, MotorIO[] followers) {
        super("Launcher/Flywheels", FlywheelState.OFF);
        module =
                new MultiAngularVelocityModule(
                        name, motor, followers, FlywheelConstants.ROTOR_TO_MECHANISM_RATIO);
    }

    /** States of the flywheels. */
    static sealed interface FlywheelState extends State
            permits FlywheelState.Off, FlywheelState.Idle, FlywheelState.Launch {
        /** Flywheels are off (zero voltage). */
        static record Off() implements FlywheelState {}

        /** Flywheels are spinning at the idle velocity. */
        static record Idle() implements FlywheelState {}

        /** Flywheels are commanded to a launch velocity. */
        static record Launch(AngularVelocity velocity) implements FlywheelState {}

        /** Shared constant for the {@link Off} record to avoid unnecessary allocations. */
        static final Off OFF = new Off();

        /** Shared constant for the {@link Idle} record to avoid unnecessary allocations. */
        static final Idle IDLE = new Idle();
    }

    /** Updates the motor inputs, applies the behavior for the current state, and logs the state. */
    void periodic() {
        module.updateInputs();

        switch (state) {
            case FlywheelState.Off() -> module.setVoltage(Volts.of(0.0));
            case FlywheelState.Idle() -> module.setVelocitySetpoint(FlywheelConstants.IDLE_VELOCITY);
            case FlywheelState.Launch(AngularVelocity velocity) -> module.setDynamicVelocity(velocity);
        }

        logState();
    }

    /**
     * Returns whether the flywheels are in the {@link FlywheelState.Launch} state and the measured
     * velocity is within the launch tolerance of the commanded velocity.
     *
     * <p>The tolerance is the commanded velocity times {@link
     * ToleranceConstants#VELOCITY_SETPOINT_TOLERANCE_MULTIPLIER}.
     */
    boolean isAtLaunchVelocity() {
        return switch (state) {
            case FlywheelState.Off() -> false;
            case FlywheelState.Idle() -> false;
            case FlywheelState.Launch(AngularVelocity velocity) ->
                    velocity.equal(
                            module.getVelocity(),
                            velocity.times(ToleranceConstants.VELOCITY_SETPOINT_TOLERANCE_MULTIPLIER));
        };
    }

    /** Constants for the launcher flywheels. */
    public static final class FlywheelConstants {

        /** Feedforward and feedback gains for the flywheel motors. */
        public static final FeedforwardValues FEEDFORWARD_VALUES =
                new FeedforwardValues(0.1, 0.0, 0.0, 0.0, 0.0, 0.0);

        /** Rotor-to-mechanism ratio for the flywheels. */
        private static final double ROTOR_TO_MECHANISM_RATIO = 0.8;

        /** Velocity commanded in the {@link FlywheelState#IDLE} state. */
        private static final AngularVelocity IDLE_VELOCITY = RadiansPerSecond.of(84);

        private FlywheelConstants() {
            /* Keep this constructor empty */
        }
    }
}

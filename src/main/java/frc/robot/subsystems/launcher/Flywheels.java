package frc.robot.subsystems.launcher;

import static frc.lib.units.AngularVelocityUnit.RadiansPerSecond;
import static frc.lib.units.VoltageUnit.Volts;

import com.ctre.phoenix6.signals.MotorAlignmentValue;
import frc.lib.ToleranceConstants;
import frc.lib.module.velocity.MultiAngularVelocityModule;
import frc.lib.motor.FeedforwardValues;
import frc.lib.motor.MotorConfig;
import frc.lib.motor.MotorIO;
import frc.lib.statemachine.State;
import frc.lib.statemachine.StateMachine;
import frc.lib.units.AngularVelocity;
import frc.robot.subsystems.DeviceIds;

/**
 * Launcher flywheels.
 *
 * <p>A leader motor drives the flywheels; one or more follower motors are configured to follow it.
 * Each state maps to either zero voltage, the idle velocity, or a commanded launch velocity.
 */
final class Flywheels extends StateMachine<Flywheels.FlywheelState> {
    private final MultiAngularVelocityModule module;

    /** Creates the flywheels. */
    Flywheels() {
        super("Launcher/Flywheels", FlywheelState.OFF);
        module =
                new MultiAngularVelocityModule(
                        name,
                        MotorIO.buildMotor(
                                new MotorConfig(
                                        DeviceIds.LEFT_LAUNCHER,
                                        FlywheelConstants.SENSOR_TO_MECHANISM_RATIO,
                                        FlywheelConstants.FEEDFORWARD_VALUES)),
                        MotorIO.buildMotors(
                                new MotorConfig[] {
                                    new MotorConfig(
                                            DeviceIds.RIGHT_LAUNCHER,
                                            FlywheelConstants.SENSOR_TO_MECHANISM_RATIO,
                                            FlywheelConstants.FEEDFORWARD_VALUES,
                                            MotorAlignmentValue.Opposed)
                                }));
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

    /** Updates the motor inputs. */
    void updateInputs() {
        module.updateInputs();
    }

    /** Applies the behavior for the current state. */
    void execute() {
        switch (state) {
            case FlywheelState.Off() -> module.setVoltage(Volts.of(0.0));
            case FlywheelState.Idle() -> module.setVelocitySetpoint(FlywheelConstants.IDLE_VELOCITY);
            case FlywheelState.Launch(AngularVelocity velocity) -> module.setDynamicVelocity(velocity);
        }
    }

    /** Logs any desired outputs from the flywheels. */
    void logOutputs() {
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
    private static final class FlywheelConstants {

        /** Feedforward and feedback gains for the flywheel motors. */
        private static final FeedforwardValues FEEDFORWARD_VALUES =
                new FeedforwardValues(0.1, 0.0, 0.0, 0.1, 0.1, 0.1);

        /** Sensor-to-mechanism ratio for the flywheels. */
        private static final double SENSOR_TO_MECHANISM_RATIO = 0.8;

        /** Velocity commanded in the {@link FlywheelState#IDLE} state. */
        private static final AngularVelocity IDLE_VELOCITY = RadiansPerSecond.of(84);

        private FlywheelConstants() {
            /* Keep this constructor empty */
        }
    }
}

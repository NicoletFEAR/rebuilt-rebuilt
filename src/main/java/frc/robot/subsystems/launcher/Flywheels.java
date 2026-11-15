package frc.robot.subsystems.launcher;

import static frc.lib.units.AngularVelocityUnit.RadiansPerSecond;
import static frc.lib.units.VoltageUnit.Volts;

import frc.lib.constants.Constants;
import frc.lib.module.velocity.MultiAngularVelocityModule;
import frc.lib.motor.FeedforwardValues;
import frc.lib.motor.MotorIO;
import frc.lib.statemachine.State;
import frc.lib.statemachine.StateMachine;
import frc.lib.units.AngularVelocity;

public class Flywheels extends StateMachine<Flywheels.FlywheelState> {
    private MultiAngularVelocityModule module;

    Flywheels(MotorIO motor, MotorIO[] followers) {
        super("Launcher/Flywheels", FlywheelState.OFF);
        module = new MultiAngularVelocityModule(name, motor, followers, FlywheelConstants.ROTOR_TO_MECHANISM_RATIO);
    }

    sealed interface FlywheelState extends State
            permits FlywheelState.Off, FlywheelState.Idle, FlywheelState.Launch {
        public record Off() implements FlywheelState {}

        public record Idle() implements FlywheelState {}

        public record Launch(AngularVelocity velocity) implements FlywheelState {}

        public static final Off OFF = new Off();
        public static final Idle IDLE = new Idle();
    }

    public void periodic() {
        module.updateInputs();

        switch (state) {
            case FlywheelState.Off() -> module.setVoltage(Volts.of(0.0));
            case FlywheelState.Idle() -> module.setVelocitySetpoint(FlywheelConstants.FLYWHEEL_IDLE);
            case FlywheelState.Launch(AngularVelocity velocity) -> module.setVelocitySetpoint(velocity);
        }

        logState();
    }

    boolean isAtLaunchVelocity() {
        return switch (state) {
            case FlywheelState.Off() -> false;
            case FlywheelState.Idle() -> false;
            case FlywheelState.Launch(AngularVelocity velocity) ->
                    velocity.equal(
                            module.getVelocity(), velocity.times(Constants.VELOCITY_SETPOINT_TOLERANCE_MULTIPLIER));
        };
    }

    public static final class FlywheelConstants {
        public static final FeedforwardValues FEEDFORWARD_VALUES =
                new FeedforwardValues(0.1, 0.0, 0.0, 0.0, 0.0, 0.0);

        private static final double ROTOR_TO_MECHANISM_RATIO = 0.8;
        private static final AngularVelocity FLYWHEEL_IDLE = RadiansPerSecond.of(84);

        private FlywheelConstants() {
            /* Keep this constructor empty */
        }
    }
}

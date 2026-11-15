package frc.robot.subsystems.launcher;

import static frc.lib.units.AngleUnit.Rotations;
import static frc.lib.units.VoltageUnit.Volts;

import frc.lib.constants.Constants;
import frc.lib.module.position.AbsEncoderAngularPositionModule;
import frc.lib.motor.FeedforwardValues;
import frc.lib.motor.MotorIO;
import frc.lib.sensor.absencoder.AbsEncoderIO;
import frc.lib.statemachine.State;
import frc.lib.statemachine.StateMachine;
import frc.lib.units.Angle;

public class Hood extends StateMachine<Hood.HoodState> {
    private AbsEncoderAngularPositionModule module;

    Hood(MotorIO motor, AbsEncoderIO canCoder) {
        super("Launcher/Hood", HoodState.OFF);
        module = new AbsEncoderAngularPositionModule("Launcher/Hood", motor, canCoder, HoodConstants.ROTOR_TO_MECHANISM_RATIO);
    }

    sealed interface HoodState extends State permits HoodState.Off, HoodState.HoldPosition {
        public record Off() implements HoodState {}

        public record HoldPosition(Angle position) implements HoodState {}

        public static final Off OFF = new Off();
    }

    public void periodic() {
        module.updateInputs();

        switch (state) {
            case HoodState.Off() -> module.setVoltage(Volts.of(0.0));
            case HoodState.HoldPosition(Angle position) -> module.setPositionSetpoint(position);
        }

        logState();
    }

    boolean isAtLaunchPosition() {
        return switch (state) {
            case HoodState.Off() -> false;
            case HoodState.HoldPosition(Angle position) ->
                    position.equal(module.getPosition(), Constants.ANGULAR_POSITION_SETPOINT_TOLERANCE);
        };
    }

    public static final class HoodConstants {
        public static final Angle HOOD_OFFSET = Rotations.of(-0.25439453125);
        public static final FeedforwardValues FEEDFORWARD_VALUES =
                new FeedforwardValues(5.0, 0.0, 0.0, 0.0, 0.0, 0.0);

        private static final double ROTOR_TO_MECHANISM_RATIO = 30.0;

        private HoodConstants() {
            /* Keep this constructor empty */
        }
    }
}

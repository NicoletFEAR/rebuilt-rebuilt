package frc.robot.subsystems.launcher;

import static frc.lib.units.AngleUnit.Rotations;
import static frc.lib.units.VoltageUnit.Volts;

import frc.lib.constants.Constants;
import frc.lib.module.position.AbsEncoderAngularPositionModule;
import frc.lib.motor.FeedforwardValues;
import frc.lib.motor.MotorIO;
import frc.lib.sensor.absencoder.AbsEncoderIO;
import frc.lib.units.Angle;
import org.littletonrobotics.junction.Logger;

public class Hood extends AbsEncoderAngularPositionModule {
    private HoodState state;

    Hood(MotorIO motor, AbsEncoderIO canCoder) {
        super("Launcher/Hood", motor, canCoder, HoodConstants.ROTOR_TO_MECHANISM_RATIO);
        state = HoodState.OFF;
    }

    sealed interface HoodState permits HoodState.Off, HoodState.HoldPosition {
        public record Off() implements HoodState {}
        public record HoldPosition(Angle position) implements HoodState {}

        public static final Off OFF = new Off();
    }

    @Override
    public void periodic() {
        super.periodic();
        Logger.recordOutput(name + "/State", (Record) state);

        switch (state) {
            case HoodState.Off() -> super.setVoltage(Volts.of(0.0));
            case HoodState.HoldPosition(Angle position) -> super.setPositionSetpoint(position);
        }
    }

    void setState(HoodState state) {
        this.state = state;
    }

    boolean isAtLaunchPosition() {
        return switch (state) {
            case HoodState.Off() -> false;
            case HoodState.HoldPosition(Angle position) -> position.equal(getPosition(), Constants.ANGULAR_POSITION_SETPOINT_TOLERANCE);
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

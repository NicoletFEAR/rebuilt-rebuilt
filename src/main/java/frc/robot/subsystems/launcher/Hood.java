package frc.robot.subsystems.launcher;

import frc.lib.constants.Constants;
import frc.lib.module.position.AbsEncoderAngularPositionModule;
import frc.lib.motor.FeedforwardValues;
import frc.lib.motor.MotorIO;
import frc.lib.sensor.absencoder.AbsEncoderIO;
import frc.lib.units.Angle;

import static frc.lib.units.AngleUnit.Radians;
import static frc.lib.units.AngleUnit.Rotations;
import static frc.lib.units.VoltageUnit.Volts;

import org.littletonrobotics.junction.Logger;
import org.wpilib.math.util.MathUtil;

public class Hood extends AbsEncoderAngularPositionModule {
    private HoodState state;
    private Angle desiredPosition;

    Hood(MotorIO motor, AbsEncoderIO canCoder) {
        super("Launcher/Hood", motor, canCoder, HoodConstants.ROTOR_TO_MECHANISM_RATIO);
        state = HoodState.OFF;
        desiredPosition = Radians.of(0.0);
    }

    enum HoodState {
        OFF,
        HOLD_POSITION,
    }

    @Override
    public void periodic() {
        super.periodic();
        Logger.recordOutput(name + "/State", state);
        Logger.recordOutput(name + "/DesiredPosition", desiredPosition.in(Radians));

        switch (state) {
            case OFF -> super.setVoltage(Volts.of(0.0));
            case HOLD_POSITION -> super.setPositionSetpoint(desiredPosition);
        }
    }

    void setState(HoodState state) {
        this.state = state;
    }

    void setDesiredPosition(Angle position) {
        desiredPosition = position;
    }

    boolean isAtDesiredPosition() {
        return MathUtil.isNear(
                getPosition().in(Radians),
                desiredPosition.in(Radians),
                Constants.ANGULAR_POSITION_SETPOINT_TOLERANCE.in(Radians));
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

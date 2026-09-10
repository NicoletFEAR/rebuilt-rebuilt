package frc.robot.subsystems.launcher;

import static edu.wpi.first.units.Units.Radians;
import static edu.wpi.first.units.Units.Volts;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.units.measure.Angle;
import frc.lib.cancoder.CanCoderIO;
import frc.lib.constants.Constants;
import frc.lib.module.position.CanCoderAngularPositionModule;
import frc.lib.motor.MotorIO;
import org.littletonrobotics.junction.Logger;

class Hood extends CanCoderAngularPositionModule {
    private HoodState state;
    private Angle desiredPosition;

    Hood(MotorIO motor, CanCoderIO canCoder) {
        super("Launcher/Hood", motor, canCoder, HoodConstants.ROTOR_TO_MECHANISM_RATIO);
        state = HoodState.OFF;
        desiredPosition = Radians.of(0.0);
    }

    private enum HoodState {
        OFF,
        HOLD_POSITION,
    }

    @Override
    public void periodic() {
        super.periodic();
        Logger.recordOutput(name + "/State", state);
        Logger.recordOutput(name + "/DesiredPosition", desiredPosition);

        switch (state) {
            case OFF -> super.setVoltage(Volts.of(0.0));
            case HOLD_POSITION -> super.setPositionSetpoint(desiredPosition);
        }
    }

    void off() {
        state = HoodState.OFF;
    }

    void holdPosition() {
        state = HoodState.HOLD_POSITION;
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

    private static final class HoodConstants {
        private static final double ROTOR_TO_MECHANISM_RATIO = 30.0;

        private HoodConstants() {}
    }
}

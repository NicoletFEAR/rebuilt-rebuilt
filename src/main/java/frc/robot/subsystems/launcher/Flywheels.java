package frc.robot.subsystems.launcher;

import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.Volts;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.units.measure.AngularVelocity;
import frc.lib.constants.Constants;
import frc.lib.module.MultiAngularVelocityModule;
import frc.lib.motor.MotorIO;
import org.littletonrobotics.junction.Logger;

class Flywheels extends MultiAngularVelocityModule {
    private FlywheelState state;
    private AngularVelocity desiredVelocity;

    Flywheels(MotorIO motor, MotorIO[] followers) {
        super("Launcher/Flywheels", motor, followers, FlywheelConstants.ROTOR_TO_MECHANISM_RATIO);
        state = FlywheelState.OFF;
        desiredVelocity = RadiansPerSecond.of(0.0);
    }

    private enum FlywheelState {
        OFF,
        FLYWHEEL_IDLE,
        LAUNCHING,
    }

    @Override
    public void periodic() {
        super.periodic();
        Logger.recordOutput(name + "/State", state);
        Logger.recordOutput(name + "/DesiredVelocity", desiredVelocity);

        switch (state) {
            case OFF -> super.setVoltage(Volts.of(0.0));
            case FLYWHEEL_IDLE -> super.setVelocitySetpoint(FlywheelConstants.FLYWHEEL_IDLE);
            case LAUNCHING -> super.setVelocitySetpoint(desiredVelocity);
        }
    }

    void off() {
        state = FlywheelState.OFF;
    }

    void launch() {
        state = FlywheelState.LAUNCHING;
    }

    void flywheelIdle() {
        state = FlywheelState.FLYWHEEL_IDLE;
    }

    void setDesiredVelocity(AngularVelocity velocity) {
        desiredVelocity = velocity;
    }

    boolean isAtDesiredVelocity() {
        double desiredVelocityRadiansPerSecond = desiredVelocity.in(RadiansPerSecond);
        return MathUtil.isNear(
                getVelocity().in(RadiansPerSecond),
                desiredVelocityRadiansPerSecond,
                Constants.VELOCITY_SETPOINT_TOLERANCE_MULTIPLIER * desiredVelocityRadiansPerSecond);
    }

    private static final class FlywheelConstants {
        private static final double ROTOR_TO_MECHANISM_RATIO = 0.8;
        private static final AngularVelocity FLYWHEEL_IDLE = RadiansPerSecond.of(84);

        private FlywheelConstants() {}
    }
}

package frc.robot.subsystems.launcher;

import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.Volts;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.units.measure.AngularVelocity;
import frc.lib.constants.Constants;
import frc.lib.module.velocity.MultiAngularVelocityModule;
import frc.lib.motor.FeedforwardValues;
import frc.lib.motor.MotorIO;
import org.littletonrobotics.junction.Logger;

public class Flywheels extends MultiAngularVelocityModule {
    private FlywheelState state;
    private AngularVelocity desiredVelocity;

    Flywheels(MotorIO motor, MotorIO[] followers) {
        super("Launcher/Flywheels", motor, followers, FlywheelConstants.ROTOR_TO_MECHANISM_RATIO);
        state = FlywheelState.OFF;
        desiredVelocity = RadiansPerSecond.of(0.0);
    }

    enum FlywheelState {
        OFF,
        IDLE,
        LAUNCH,
    }

    @Override
    public void periodic() {
        super.periodic();
        Logger.recordOutput(name + "/State", state);
        Logger.recordOutput(name + "/DesiredVelocity", desiredVelocity);

        switch (state) {
            case OFF -> super.setVoltage(Volts.of(0.0));
            case IDLE -> super.setVelocitySetpoint(FlywheelConstants.FLYWHEEL_IDLE);
            case LAUNCH -> super.setVelocitySetpoint(desiredVelocity);
        }
    }

    void setState(FlywheelState state) {
        this.state = state;
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

    public static final class FlywheelConstants {
        public static final FeedforwardValues FEEDFORWARD_VALUES = new FeedforwardValues(0.1, 0.0, 0.0, 0.0, 0.0, 0.0);

        private static final double ROTOR_TO_MECHANISM_RATIO = 0.8;
        private static final AngularVelocity FLYWHEEL_IDLE = RadiansPerSecond.of(84);

        private FlywheelConstants() {
            /* Keep this constructor empty */
        }
    }
}

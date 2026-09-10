package frc.robot.subsystems.intake;

import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.Volts;

import edu.wpi.first.units.measure.AngularVelocity;
import frc.lib.module.velocity.AngularVelocityModule;
import frc.lib.motor.FeedforwardValues;
import frc.lib.motor.MotorIO;
import org.littletonrobotics.junction.Logger;

public class Wheels extends AngularVelocityModule {
    private WheelState state;

    Wheels(MotorIO motor) {
        super("Intake/Wheels", motor, WheelConstants.ROTOR_TO_MECHANISM_RATIO);
        state = WheelState.OFF;
    }

    private enum WheelState {
        OFF,
        JOSTLING,
        INTAKING,
    }

    @Override
    public void periodic() {
        super.periodic();
        Logger.recordOutput(name + "/State", state);

        switch (state) {
            case OFF -> super.setVoltage(Volts.of(0.0));
            case JOSTLING -> super.setVelocitySetpoint(WheelConstants.JOSTLE_VELOCITY);
            case INTAKING -> super.setVelocitySetpoint(WheelConstants.INTAKE_VELOCITY);
        }
    }

    void off() {
        state = WheelState.OFF;
    }

    void jostle() {
        state = WheelState.JOSTLING;
    }

    void intake() {
        state = WheelState.INTAKING;
    }

    public static final class WheelConstants {
        public static final FeedforwardValues FEEDFORWARD_VALUES =
                new FeedforwardValues(1.0, 0.0, 0.0, 0.0, 0.0, 0.0);

        private static final double ROTOR_TO_MECHANISM_RATIO = 0.6;

        private static final AngularVelocity JOSTLE_VELOCITY = RadiansPerSecond.of(Math.PI * 15.0);
        private static final AngularVelocity INTAKE_VELOCITY = RadiansPerSecond.of(Math.PI * 100.0);

        private WheelConstants() {/* Keep this constructor empty */}
    }
}

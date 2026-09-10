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

    enum WheelState {
        OFF,
        JOSTLE,
        INTAKE,
        EXTAKE,
    }

    @Override
    public void periodic() {
        super.periodic();
        Logger.recordOutput(name + "/State", state);

        switch (state) {
            case OFF -> super.setVoltage(Volts.of(0.0));
            case JOSTLE -> super.setVelocitySetpoint(WheelConstants.JOSTLE_VELOCITY);
            case INTAKE -> super.setVelocitySetpoint(WheelConstants.INTAKE_VELOCITY);
            case EXTAKE -> super.setVelocitySetpoint(WheelConstants.EXTAKE_VELOCITY);
        }
    }

    void setState(WheelState state) {
        this.state = state;
    }

    public static final class WheelConstants {
        public static final FeedforwardValues FEEDFORWARD_VALUES =
                new FeedforwardValues(1.0, 0.0, 0.0, 0.0, 0.0, 0.0);

        private static final double ROTOR_TO_MECHANISM_RATIO = 0.6;

        private static final AngularVelocity JOSTLE_VELOCITY = RadiansPerSecond.of(Math.PI * 15.0);
        private static final AngularVelocity INTAKE_VELOCITY = RadiansPerSecond.of(Math.PI * 100.0);
        private static final AngularVelocity EXTAKE_VELOCITY = RadiansPerSecond.of(Math.PI * -100.0);

        private WheelConstants() {
            /* Keep this constructor empty */
        }
    }
}

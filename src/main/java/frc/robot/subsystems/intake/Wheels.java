package frc.robot.subsystems.intake;

import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.Volts;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.units.measure.AngularVelocity;
import frc.lib.module.AngularVelocityModule;
import frc.lib.motor.MotorIO;

<<<<<<<< HEAD:src/main/java/frc/robot/subsystems/intake/IntakeFlywheels.java
class IntakeFlywheels extends AngularVelocityModule {
    private FlywheelState state;

    IntakeFlywheels(MotorIO motor) {
        super("Intake/Flywheels", motor, FlywheelConstants.ROTOR_TO_MECHANISM_RATIO);
        state = FlywheelState.OFF;
========
class Wheels extends AngularVelocityModule {
    private WheelState state;

    Wheels(MotorIO motor) {
        super("Intake/Wheels", motor, WheelConstants.ROTOR_TO_MECHANISM_RATIO);
        state = WheelState.OFF;
>>>>>>>> baf7905d0d87ebdb7db2904dcb3c757814acf359:src/main/java/frc/robot/subsystems/intake/Wheels.java
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

    private static final class WheelConstants {
        private static final double ROTOR_TO_MECHANISM_RATIO = 0.6;

        private static final AngularVelocity JOSTLE_VELOCITY = RadiansPerSecond.of(Math.PI * 15.0);
        private static final AngularVelocity INTAKE_VELOCITY = RadiansPerSecond.of(Math.PI * 100.0);

        private WheelConstants() {
        }
    }
}

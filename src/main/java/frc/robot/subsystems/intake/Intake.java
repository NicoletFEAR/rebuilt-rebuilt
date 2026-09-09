package frc.robot.subsystems.intake;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.motor.MotorIO;

public class Intake extends SubsystemBase {
    private final Arm arm;
    private final Flywheels flywheels;

    private IntakeState state;

    public Intake(MotorIO armMotor, MotorIO flywheelMotor) {
        arm = new Arm(armMotor);
        flywheels = new Flywheels(flywheelMotor);

        state = IntakeState.START;
    }

    private enum IntakeState {
        START,
        DEPLOYING,
        INTAKING,
        RETRACTING,
    }

    @Override
    public void periodic() {
        Logger.recordOutput("Intake/State", state);

        switch (state) {
            case START -> {
                arm.retract();
                flywheels.off();
            }

            case DEPLOYING -> {
                arm.deploy();
                flywheels.off();
            }

            case INTAKING -> {
                arm.deploy();
                flywheels.intake();
            }

            case RETRACTING -> {
                arm.retract();
                flywheels.jostle();
            }
        }
    }

    public void start() {
        state = IntakeState.START;
    }

    public void deploy() {
        state = IntakeState.DEPLOYING;
    }

    public void intake() {
        state = IntakeState.INTAKING;
    }

    public void retract() {
        state = IntakeState.RETRACTING;
    }
}

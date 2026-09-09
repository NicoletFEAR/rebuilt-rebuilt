package frc.robot.subsystems.intake;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.motor.MotorIO;

public class Intake extends SubsystemBase {
    private final Arm arm;
    private final Wheels flywheels;

    private IntakeState state;

    public Intake(MotorIO armMotor, MotorIO flywheelMotor) {
        arm = new Arm(armMotor);
        flywheels = new Wheels(flywheelMotor);

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
                intakeFlywheels.off();
            }

            case DEPLOYING -> {
                arm.deploy();
                intakeFlywheels.off();
            }

            case INTAKING -> {
                arm.deploy();
                intakeFlywheels.intake();
            }

            case RETRACTING -> {
                arm.retract();
                intakeFlywheels.jostle();
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

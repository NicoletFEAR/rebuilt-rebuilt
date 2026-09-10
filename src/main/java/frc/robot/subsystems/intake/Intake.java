package frc.robot.subsystems.intake;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.motor.MotorIO;
import org.littletonrobotics.junction.Logger;

public class Intake extends SubsystemBase {
    private final Arm arm;
    private final Wheels wheels;

    private IntakeState state;

    public Intake(MotorIO armMotor, MotorIO wheelMotor) {
        arm = new Arm(armMotor);
        wheels = new Wheels(wheelMotor);

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
                wheels.off();
            }

            case DEPLOYING -> {
                arm.deploy();
                wheels.off();
            }

            case INTAKING -> {
                arm.deploy();
                wheels.intake();
            }

            case RETRACTING -> {
                arm.retract();
                wheels.jostle();
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

package frc.robot.subsystems.intake;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.motor.MotorIO;
import frc.robot.subsystems.intake.Arm.ArmState;
import frc.robot.subsystems.intake.Wheels.WheelState;

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

    public enum IntakeState {
        START,
        DEPLOY,
        INTAKE,
        RETRACT,
        JOSTLE,
        EXTAKE
    }

    @Override
    public void periodic() {
        Logger.recordOutput("Intake/State", state);

        switch (state) {
            case START -> {
                arm.setState(ArmState.RETRACT);
                wheels.setState(WheelState.OFF);
            }

            case DEPLOY -> {
                arm.setState(ArmState.DEPLOY);
                wheels.setState(WheelState.OFF);
            }

            case INTAKE -> {
                arm.setState(ArmState.DEPLOY);
                wheels.setState(WheelState.INTAKE);
            }

            case RETRACT -> {
                arm.setState(ArmState.DEPLOY);
                wheels.setState(WheelState.INTAKE);
            }

            case JOSTLE -> {
                arm.setState(ArmState.DEPLOY);
                wheels.setState(WheelState.JOSTLE);
            }

            case EXTAKE -> {
                arm.setState(ArmState.DEPLOY);
                wheels.setState(WheelState.EXTAKE);
            }
        }
    }

    public void setState(IntakeState state) {
        this.state = state;
    }
}

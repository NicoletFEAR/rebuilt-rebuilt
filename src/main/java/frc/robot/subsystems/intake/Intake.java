package frc.robot.subsystems.intake;

import frc.lib.motor.MotorConfig;
import frc.lib.statemachine.State;
import frc.lib.statemachine.StateMachine;
import frc.robot.subsystems.intake.Arm.ArmState;
import frc.robot.subsystems.intake.Wheels.WheelState;

/**
 * Intake subsystem: an extendable arm carrying intake wheels.
 *
 * <p>Each intake state maps to a set of arm and wheel states; see {@link #periodic()} for the
 * current mapping.
 */
public final class Intake extends StateMachine<Intake.IntakeState> {
    private final Arm arm;
    private final Wheels wheels;

    /**
     * Creates the intake.
     *
     * @param armConfig configuration for the arm motor
     * @param wheelConfig configuration for the wheel motor
     */
    public Intake(MotorConfig armConfig, MotorConfig wheelConfig) {
        super("Intake", IntakeState.START);
        arm = new Arm(armConfig);
        wheels = new Wheels(wheelConfig);
    }

    /** States of the intake. */
    public enum IntakeState implements State {
        START,
        DEPLOY,
        INTAKE,
        RETRACT,
        JOSTLE,
        EXTAKE
    }

    /**
     * Applies the current state to the arm and wheels, runs their periodic logic, and logs the state.
     */
    public void periodic() {
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
                arm.setState(ArmState.RETRACT);
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

        arm.periodic();
        wheels.periodic();
        logState();
    }
}

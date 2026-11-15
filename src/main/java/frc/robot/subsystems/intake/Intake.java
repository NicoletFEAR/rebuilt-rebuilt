package frc.robot.subsystems.intake;

import frc.lib.motor.MotorIO;
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
public class Intake extends StateMachine<Intake.IntakeState> {
    private final Arm arm;
    private final Wheels wheels;

    /**
     * Creates the intake.
     *
     * @param armMotor interface used to communicate with the arm motor
     * @param wheelMotor interface used to communicate with the wheel motor
     */
    public Intake(MotorIO armMotor, MotorIO wheelMotor) {
        super("Intake", IntakeState.START);
        arm = new Arm(armMotor);
        wheels = new Wheels(wheelMotor);
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
     *
     * <p>Note that the {@link IntakeState#RETRACT} state currently deploys the arm and spins the
     * wheels inward, matching {@link IntakeState#INTAKE}; the {@link #setState(IntakeState)} override
     * also bypasses {@link StateMachine#setState(State)} bookkeeping.
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

        arm.periodic();
        wheels.periodic();
        logState();
    }

    /**
     * Sets the intake's state.
     *
     * <p>This override bypasses {@link StateMachine#setState(State)}; it updates the current state
     * without touching the stored previous state or entered timestamp.
     *
     * @param state the state to enter
     */
    public void setState(IntakeState state) {
        this.state = state;
    }
}

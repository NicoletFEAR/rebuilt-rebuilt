package com.nicoletfear.robot.subsystems.intake;

import com.nicoletfear.lib.statemachine.State;
import com.nicoletfear.lib.statemachine.StateMachine;
import com.nicoletfear.robot.subsystems.intake.Arm.ArmState;
import com.nicoletfear.robot.subsystems.intake.Wheels.WheelState;

/**
 * Intake subsystem: an extendable arm carrying intake wheels.
 *
 * <p>Each intake state maps to a set of arm and wheel states; see {@link #applyState()} for the
 * current mapping.
 */
public final class Intake extends StateMachine<Intake.IntakeState> {
    private final Arm arm = new Arm();
    private final Wheels wheels = new Wheels();

    /** Creates the intake. */
    public Intake() {
        super("Intake", IntakeState.START);
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

    /** Updates the inputs from the motors. */
    public void updateInputs() {
        arm.updateInputs();
        wheels.updateInputs();
    }

    /** Sets the arm and wheels to the correct state based on the entire intake state. */
    public void applyState() {
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
    }

    /** Applies the arm and wheel behavior for the current state. */
    public void execute() {
        arm.execute();
        wheels.execute();
    }

    /** Logs any desired outputs from the intake. */
    public void logOutputs() {
        logState();
        arm.logOutputs();
        wheels.logOutputs();
    }
}

package com.nicoletfear.robot;

import com.nicoletfear.robot.operator.OperatorConstants;
import com.nicoletfear.robot.subsystems.Superstructure;
import com.nicoletfear.robot.subsystems.Superstructure.SuperState;
import com.nicoletfear.robot.subsystems.drivebase.DriveParameters;
import org.wpilib.driverstation.Gamepad;

/**
 * Constructs and wires together the robot's subsystems and translates driver input into
 * superstructure states.
 */
public final class RobotContainer {
    private final RobotState robotState = new RobotState();
    private final Superstructure superstructure;
    private final Gamepad driverController = new Gamepad(0);

    /** Applies deadbands to the driver controller. */
    public RobotContainer() {
        superstructure = new Superstructure(robotState);
        driverController.setLeftXDeadband(OperatorConstants.DEADBAND);
        driverController.setLeftYDeadband(OperatorConstants.DEADBAND);
        driverController.setLeftTriggerDeadband(OperatorConstants.DEADBAND);
        driverController.setRightXDeadband(OperatorConstants.DEADBAND);
        driverController.setRightYDeadband(OperatorConstants.DEADBAND);
        driverController.setRightTriggerDeadband(OperatorConstants.DEADBAND);
    }

    /** Updates the inputs for all devices on the robot. */
    public void updateInputs() {
        superstructure.updateInputs();
    }

    /**
     * Decides the superstructure's desired state from the driver controller, runs the
     * superstructure's periodic logic, and logs the robot state.
     *
     * <p>Left and right triggers above {@link OperatorConstants#TRIGGER_THRESHOLD} command the intake
     * and launcher respectively: left trigger intakes (and also spins up the launcher when the right
     * trigger is also held), right trigger launches, and the right bumper extakes. With no trigger
     * held, the robot drives with the launcher idle.
     */
    public void applyState() {
        DriveParameters driveParameters = new DriveParameters(
                -driverController.getLeftY(), -driverController.getLeftX(), driverController.getRightX());

        if (driverController.getLeftTrigger() > OperatorConstants.TRIGGER_THRESHOLD) {
            if (driverController.getRightTrigger() > OperatorConstants.TRIGGER_THRESHOLD) {
                superstructure.setState(new SuperState.LaunchAndIntake(driveParameters));
            } else {
                superstructure.setState(new SuperState.Intake(driveParameters));
            }
        } else if (driverController.getRightTrigger() > OperatorConstants.TRIGGER_THRESHOLD) {
            superstructure.setState(new SuperState.Launch(driveParameters));
        } else if (driverController.getRightBumperButton()) {
            superstructure.setState(new SuperState.Extake(driveParameters));
        } else {
            superstructure.setState(new SuperState.Operate(driveParameters));
        }

        superstructure.applyState();
    }

    /** Runs the subsystems. */
    public void execute() {
        superstructure.execute();
    }

    /** Logs any desired outputs from the whole robot. */
    public void logOutputs() {
        robotState.log();
        superstructure.logOutputs();
    }
}

package com.nicoletfear.robot.subsystems;

import static com.nicoletfear.lib.units.AngleUnit.Radians;
import static com.nicoletfear.lib.units.AngularVelocityUnit.RadiansPerSecond;

import com.nicoletfear.lib.statemachine.State;
import com.nicoletfear.lib.statemachine.StateMachine;
import com.nicoletfear.robot.RobotState;
import com.nicoletfear.robot.subsystems.drivebase.DriveParameters;
import com.nicoletfear.robot.subsystems.drivebase.Drivebase;
import com.nicoletfear.robot.subsystems.drivebase.Drivebase.DrivebaseState;
import com.nicoletfear.robot.subsystems.intake.Intake;
import com.nicoletfear.robot.subsystems.intake.Intake.IntakeState;
import com.nicoletfear.robot.subsystems.launcher.LaunchParameters;
import com.nicoletfear.robot.subsystems.launcher.Launcher;
import com.nicoletfear.robot.subsystems.launcher.Launcher.LauncherState;

/**
 * Top-level state machine that coordinates the drive, launcher, and intake subsystems.
 *
 * <p>Each superstructure state maps to a set of subsystem states; see {@link #applyState()} for the
 * current mapping. Subsystems are updated in the order drive, intake, launcher.
 */
public final class Superstructure extends StateMachine<Superstructure.SuperState> {
    private final Drivebase drivebase;
    private final Launcher launcher = new Launcher();
    private final Intake intake = new Intake();

    /**
     * Creates the superstructure.
     *
     * @param robotState the shared robot state object
     */
    public Superstructure(RobotState robotState) {
        super("Superstructure", SuperState.START);
        this.drivebase = new Drivebase(robotState);
    }

    /** States of the superstructure. */
    public sealed interface SuperState extends State
            permits SuperState.Start,
                    SuperState.Stopped,
                    SuperState.Operate,
                    SuperState.Intake,
                    SuperState.Launch,
                    SuperState.LaunchAndIntake,
                    SuperState.Extake {

        /** Initial state; behaves the same as {@link Stopped}. */
        public record Start() implements SuperState {}

        /** Robot is disabled; drive and launcher are off, intake arm is deployed. */
        public record Stopped() implements SuperState {}

        /** Drive teleop with the launcher idle and the intake deployed. */
        public record Operate(DriveParameters driveParameters) implements SuperState {}

        /** Drive teleop with the launcher idling (flywheels at idle speed) and the intake running. */
        public record Intake(DriveParameters driveParameters) implements SuperState {}

        /** Drive teleop and fire the launcher (spinning up first until ready). */
        public record Launch(DriveParameters driveParameters) implements SuperState {}

        /** Drive teleop, run the intake, and fire the launcher (spinning up first until ready). */
        public record LaunchAndIntake(DriveParameters driveParameters) implements SuperState {}

        /** Drive teleop and run the intake and launcher in reverse (extake). */
        public record Extake(DriveParameters driveParameters) implements SuperState {}

        /** Shared constant for the {@link Start} record. */
        public static final Start START = new Start();

        /** Shared constant for the {@link Stopped} record. */
        public static final Stopped STOPPED = new Stopped();
    }

    /** Updates the inputs for all devices on the robot. */
    public void updateInputs() {
        drivebase.updateInputs();
        intake.updateInputs();
        launcher.updateInputs();
    }

    /** Chooses the state for the subsystems based on the superstructure state. */
    public void applyState() {
        switch (state) {
            case SuperState.Start() -> {
                drivebase.setState(DrivebaseState.OFF);
                launcher.setState(LauncherState.OFF);
                intake.setState(IntakeState.DEPLOY);
            }

            case SuperState.Stopped() -> {
                drivebase.setState(DrivebaseState.OFF);
                launcher.setState(LauncherState.OFF);
                intake.setState(IntakeState.DEPLOY);
            }

            case SuperState.Operate(DriveParameters driveParameters) -> {
                drivebase.setState(new DrivebaseState.Drive(driveParameters));
                launcher.setState(LauncherState.IDLE);
                intake.setState(IntakeState.DEPLOY);
            }

            case SuperState.Intake(DriveParameters driveParameters) -> {
                drivebase.setState(new DrivebaseState.Drive(driveParameters));
                launcher.setState(LauncherState.IDLE);
                intake.setState(IntakeState.INTAKE);
            }

            case SuperState.Launch(DriveParameters driveParameters) -> {
                drivebase.setState(new DrivebaseState.Drive(driveParameters));
                intake.setState(IntakeState.DEPLOY);
                // We use a hardcoded value of 600 rad/s launcher velocity and 2.5 rad hood angle
                // Soon I'll implement auto-aim so that these become dynamic
                launcher.setState(
                        new LauncherState.Launch(new LaunchParameters(RadiansPerSecond.of(600.0), Radians.of(2.5))));
            }

            case SuperState.LaunchAndIntake(DriveParameters driveParameters) -> {
                drivebase.setState(new DrivebaseState.Drive(driveParameters));
                intake.setState(IntakeState.INTAKE);
                launcher.setState(
                        new LauncherState.Launch(new LaunchParameters(RadiansPerSecond.of(600.0), Radians.of(2.5))));
            }

            case SuperState.Extake(DriveParameters driveParameters) -> {
                drivebase.setState(new DrivebaseState.Drive(driveParameters));
                intake.setState(IntakeState.EXTAKE);
                launcher.setState(LauncherState.EXTAKE);
            }
        }

        drivebase.applyState();
        intake.applyState();
        launcher.applyState();
    }

    /** Runs the subsystems. */
    public void execute() {
        drivebase.execute();
        intake.execute();
        launcher.execute();
    }

    /** Logs any desired outputs from the subsystems. */
    public void logOutputs() {
        logState();
        drivebase.logOutputs();
        intake.logOutputs();
        launcher.logOutputs();
    }
}

package frc.robot.subsystems;

import static frc.lib.units.AngleUnit.Radians;
import static frc.lib.units.AngularVelocityUnit.RadiansPerSecond;

import frc.lib.statemachine.State;
import frc.lib.statemachine.StateMachine;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.drive.Drive.DriveState;
import frc.robot.subsystems.drive.DriveParameters;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.intake.Intake.IntakeState;
import frc.robot.subsystems.launcher.LaunchParameters;
import frc.robot.subsystems.launcher.Launcher;
import frc.robot.subsystems.launcher.Launcher.LauncherState;

/**
 * Top-level state machine that coordinates the drive, launcher, and intake subsystems.
 *
 * <p>Each superstructure state maps to a set of subsystem states; see {@link #periodic()} for the
 * current mapping. Subsystems are updated in the order drive, intake, launcher.
 */
public final class Superstructure extends StateMachine<Superstructure.SuperState> {
    private final Drive drive;
    private final Launcher launcher;
    private final Intake intake;

    /**
     * Creates the superstructure.
     *
     * @param drive the drive subsystem
     * @param launcher the launcher subsystem
     * @param intake the intake subsystem
     */
    public Superstructure(Drive drive, Launcher launcher, Intake intake) {
        super("Superstructure", SuperState.START);
        this.drive = drive;
        this.launcher = launcher;
        this.intake = intake;
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

        /** Robot is disabled/idle; drive and launcher are off, intake arm is deployed. */
        public record Stopped() implements SuperState {}

        /** Drive teleop with the launcher off and the intake deployed. */
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

    /**
     * Applies the current superstructure state to the subsystems, runs each subsystem's periodic
     * logic, and logs the superstructure's state.
     *
     * <p>The {@code Launch} and {@code LaunchAndIntake} states command the launcher to spin up until
     * it reports ready (see {@link Launcher#isReadyToLaunch()}), after which they command it to
     * launch.
     */
    public void periodic() {
        switch (state) {
            case SuperState.Start() -> {
                drive.setState(DriveState.OFF);
                launcher.setState(LauncherState.OFF);
                intake.setState(IntakeState.DEPLOY);
            }

            case SuperState.Stopped() -> {
                drive.setState(DriveState.OFF);
                launcher.setState(LauncherState.OFF);
                intake.setState(IntakeState.DEPLOY);
            }

            case SuperState.Operate(DriveParameters driveParameters) -> {
                drive.setState(new DriveState.Teleop(driveParameters));
                launcher.setState(LauncherState.OFF);
                intake.setState(IntakeState.DEPLOY);
            }

            case SuperState.Intake(DriveParameters driveParameters) -> {
                drive.setState(new DriveState.Teleop(driveParameters));
                launcher.setState(LauncherState.IDLE);
                intake.setState(IntakeState.INTAKE);
            }

            case SuperState.Launch(DriveParameters driveParameters) -> {
                drive.setState(new DriveState.Teleop(driveParameters));
                intake.setState(IntakeState.DEPLOY);

                // We use a hardcoded value of 600 rad/s launcher velocity and 2.5 rad hood angle
                // Soon I'll implement auto-aim so that these become dynamic
                if (launcher.isReadyToLaunch()) {
                    launcher.setState(
                            new LauncherState.Launch(
                                    new LaunchParameters(RadiansPerSecond.of(600.0), Radians.of(2.5))));
                } else {
                    launcher.setState(
                            new LauncherState.SpinUp(
                                    new LaunchParameters(RadiansPerSecond.of(600.0), Radians.of(2.5))));
                }
            }

            case SuperState.LaunchAndIntake(DriveParameters driveParameters) -> {
                drive.setState(new DriveState.Teleop(driveParameters));
                intake.setState(IntakeState.INTAKE);

                if (launcher.isReadyToLaunch()) {
                    launcher.setState(
                            new LauncherState.Launch(
                                    new LaunchParameters(RadiansPerSecond.of(600.0), Radians.of(2.5))));
                } else {
                    launcher.setState(
                            new LauncherState.SpinUp(
                                    new LaunchParameters(RadiansPerSecond.of(600.0), Radians.of(2.5))));
                }
            }

            case SuperState.Extake(DriveParameters driveParameters) -> {
                drive.setState(new DriveState.Teleop(driveParameters));
                intake.setState(IntakeState.EXTAKE);
                launcher.setState(LauncherState.EXTAKE);
            }
        }

        drive.periodic();
        intake.periodic();
        launcher.periodic();
        logState();
    }
}

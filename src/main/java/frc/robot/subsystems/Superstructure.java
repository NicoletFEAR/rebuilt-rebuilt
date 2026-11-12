package frc.robot.subsystems;

import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.drive.Drive.DriveState;
import frc.robot.subsystems.drive.DriveParameters;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.intake.Intake.IntakeState;
import frc.robot.subsystems.launcher.Launcher;
import frc.robot.subsystems.launcher.Launcher.LauncherState;

import static frc.lib.units.AngleUnit.Radians;
import static frc.lib.units.AngularVelocityUnit.RadiansPerSecond;

import org.littletonrobotics.junction.Logger;

public class Superstructure {
    private final Drive drive;
    private final Launcher launcher;
    private final Intake intake;

    private SuperState state = new SuperState.Start();

    public Superstructure(Drive drive, Launcher launcher, Intake intake) {
        this.drive = drive;
        this.launcher = launcher;
        this.intake = intake;
    }

    public sealed interface SuperState
            permits SuperState.Start,
                    SuperState.Stopped,
                    SuperState.Operate,
                    SuperState.Intake,
                    SuperState.Launch,
                    SuperState.LaunchAndIntake,
                    SuperState.Extake {
        public record Start() implements SuperState {}

        public record Stopped() implements SuperState {}

        public record Operate(DriveParameters driveParameters) implements SuperState {}

        public record Intake(DriveParameters driveParameters) implements SuperState {}

        public record Launch(DriveParameters driveParameters) implements SuperState {}

        public record LaunchAndIntake(DriveParameters driveParameters) implements SuperState {}

        public record Extake(DriveParameters driveParameters) implements SuperState {}

        public static final Start START = new Start();
        public static final Stopped STOPPED = new Stopped();
    }

    public void periodic() {
        Logger.recordOutput("Superstructure/State", (Record) state);

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

                if (launcher.isReadyToLaunch()) {
                    launcher.setState(new LauncherState.Launch(RadiansPerSecond.of(600.0), Radians.of(2.5)));
                } else {
                    launcher.setState(new LauncherState.SpinUp(RadiansPerSecond.of(600.0), Radians.of(2.5)));
                }
            }

            case SuperState.LaunchAndIntake(DriveParameters driveParameters) -> {
                drive.setState(new DriveState.Teleop(driveParameters));
                intake.setState(IntakeState.INTAKE);
                
                if (launcher.isReadyToLaunch()) {
                    launcher.setState(new LauncherState.Launch(RadiansPerSecond.of(600.0), Radians.of(2.5)));
                } else {
                    launcher.setState(new LauncherState.SpinUp(RadiansPerSecond.of(600.0), Radians.of(2.5)));
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
    }

    public void setState(SuperState state) {
        this.state = state;
    }
}

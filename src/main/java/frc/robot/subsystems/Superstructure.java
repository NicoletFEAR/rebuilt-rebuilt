package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.drive.Drive.DriveState;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.intake.Intake.IntakeState;
import frc.robot.subsystems.launcher.Launcher;
import frc.robot.subsystems.launcher.Launcher.LauncherState;

import org.littletonrobotics.junction.Logger;

public class Superstructure extends SubsystemBase {
    private final Drive drive;
    private final Launcher launcher;
    private final Intake intake;

    private SuperState state;

    public Superstructure(Drive drive, Launcher launcher, Intake intake) {
        this.drive = drive;
        this.launcher = launcher;
        this.intake = intake;

        state = SuperState.START;
    }

    public enum SuperState {
        START,
        IDLE,
        STOPPED,
        DRIVE_AROUND_TEMPORARY,
        INTAKE,
        LAUNCH,
        LAUNCH_AND_INTAKE,
        EXTAKE,
    }

    @Override
    public void periodic() {
        Logger.recordOutput("Superstructure/State", state);

        switch (state) {
            case START -> {
                drive.setState(DriveState.DRIVE);
                launcher.setState(LauncherState.OFF);
                intake.setState(IntakeState.DEPLOY);
            }

            case IDLE -> {
                launcher.setState(LauncherState.IDLE);
                intake.setState(IntakeState.DEPLOY);
            }

            case STOPPED -> {
                drive.setState(DriveState.DRIVE);
                launcher.setState(LauncherState.OFF);
                intake.setState(IntakeState.DEPLOY);
            }

            case DRIVE_AROUND_TEMPORARY -> {
                drive.setState(DriveState.DRIVE);
                launcher.setState(LauncherState.OFF);
                intake.setState(IntakeState.DEPLOY);
            }

            case INTAKE -> {
                drive.setState(DriveState.DRIVE);
                launcher.setState(LauncherState.IDLE);
                intake.setState(IntakeState.INTAKE);
            }

            case LAUNCH -> {
                drive.setState(DriveState.DRIVE);
                intake.setState(IntakeState.DEPLOY);
                launcher.setState(LauncherState.SPIN_UP);
            }

            case LAUNCH_AND_INTAKE -> {
                drive.setState(DriveState.DRIVE);
                intake.setState(IntakeState.INTAKE);
                launcher.setState(LauncherState.SPIN_UP);
            }

            case EXTAKE -> {
                drive.setState(DriveState.DRIVE);
                intake.setState(IntakeState.EXTAKE);
                launcher.setState(LauncherState.EXTAKE);
            }
        }
    }

    public void setState(SuperState state) {
        this.state = state;
    }

    public void applyDriveSpeedsFromControls(double x, double y, double omega) {
        drive.applySpeedsFromControls(x, y, omega);
    }
}

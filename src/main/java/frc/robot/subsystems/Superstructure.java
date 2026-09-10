package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.launcher.Launcher;
import org.littletonrobotics.junction.Logger;

public class Superstructure extends SubsystemBase {
    private final Drive drive;
    private final Launcher launcher;
    private final Intake intake;

    private SuperstructureState state;

    public Superstructure(Drive drive, Launcher launcher, Intake intake) {
        this.drive = drive;
        this.launcher = launcher;
        this.intake = intake;

        state = SuperstructureState.START;
    }

    private enum SuperstructureState {
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
                drive.off();
                launcher.off();
                intake.start();
            }

            case IDLE -> {
                launcher.flywheelIdle();
                intake.deploy();
            }

            case STOPPED -> {
                drive.off();
                launcher.off();
                intake.deploy();
            }

            case DRIVE_AROUND_TEMPORARY -> {
                drive.drive();
                launcher.off();
                intake.deploy();
            }

            case INTAKE -> {
                launcher.flywheelIdle();
                intake.intake();
            }

            case LAUNCH -> {
                if (launcher.isReadyToLaunch()) {
                    launcher.launch();
                } else {
                    launcher.spinUp();
                }
            }

            case LAUNCH_AND_INTAKE -> {
                intake.intake();
                if (launcher.isReadyToLaunch()) {
                    launcher.launch();
                } else {
                    launcher.spinUp();
                }
            }

            case EXTAKE -> {
                intake.extake();
                launcher.extake();
            }
        }
    }

    public void stop() {
        state = SuperstructureState.STOPPED;
    }

    public void driveAround() {
        state = SuperstructureState.DRIVE_AROUND_TEMPORARY;
    }

    public void intake() {
        state = SuperstructureState.INTAKE;
    }

    public void launch() {
        state = SuperstructureState.LAUNCH;
    }

    public void launchAndIntake() {
        state = SuperstructureState.LAUNCH_AND_INTAKE;
    }

    public void extake() {
        state = SuperstructureState.EXTAKE;
    }

    public void applyDriveSpeedsFromControls(double x, double y, double omega) {
        drive.applySpeedsFromControls(x, y, omega);
    }
}

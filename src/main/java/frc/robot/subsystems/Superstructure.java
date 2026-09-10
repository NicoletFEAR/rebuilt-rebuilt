package frc.robot.subsystems;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.launcher.Launcher;

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
        STOPPED,
        DRIVE_AROUND_TEMPORARY,
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
        }
    }

    public void stop() {
        state = SuperstructureState.STOPPED;
    }

    public void driveAround() {
        state = SuperstructureState.DRIVE_AROUND_TEMPORARY;
    }

    public void applyDriveSpeedsFromControls(double x, double y, double omega) {
        drive.applySpeedsFromControls(x, y, omega);
    }
}

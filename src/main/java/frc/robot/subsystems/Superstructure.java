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
        STOPPED,
        OPERATING,
        INTAKING,
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

            case OPERATING -> {
                drive.drive();
                launcher.flywheelIdle();
                intake.retract();
            }

            case INTAKING -> {
                drive.drive();
                launcher.flywheelIdle();
                intake.intake();
            }
        }
    }

    public void stop() {
        state = SuperstructureState.STOPPED;
    }

    public void operate() {
        state = SuperstructureState.OPERATING;
    }

    public void intake() {
        state = SuperstructureState.INTAKING;
    }

    public void applyDriveSpeedsFromControls(double x, double y, double omega) {
        drive.applySpeedsFromControls(x, y, omega);
    }
}

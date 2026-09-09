package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.launcher.Launcher;
import org.littletonrobotics.junction.Logger;

public class Superstructure extends SubsystemBase {
    private final Launcher launcher;
    private final Intake intake;

    private SuperstructureState state;

    public Superstructure(Launcher launcher, Intake intake) {
        this.launcher = launcher;
        this.intake = intake;

        state = SuperstructureState.START;
    }

    private enum SuperstructureState {
        START,
        IDLE,
        STOPPED,
    }

    @Override
    public void periodic() {
        Logger.recordOutput("Superstructure/State", state);

        switch (state) {
            case START -> {
                launcher.off();
                intake.start();
            }

            case IDLE -> {
                launcher.flywheelIdle();
            }

            case STOPPED -> {
                launcher.off();
                intake.deploy();
            }
        }
    }

    public void stop() {
        state = SuperstructureState.STOPPED;
    }
}

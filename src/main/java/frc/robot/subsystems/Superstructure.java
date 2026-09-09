package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.launcher.Launcher;

public class Superstructure extends SubsystemBase {
    private final Launcher launcher;
    private final Intake intake;

    public Superstructure(Launcher launcher, Intake intake) {
        this.launcher = launcher;
        this.intake = intake;
    }

    public Command stop() {
        return launcher.off().alongWith(intake.deploy());
    }
}

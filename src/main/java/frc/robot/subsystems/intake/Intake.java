package frc.robot.subsystems.intake;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.motor.MotorIO;

public class Intake extends SubsystemBase {
    private final Arm arm;
    private final Flywheels flywheels;

    public Intake(MotorIO armMotor, MotorIO flywheelMotor) {
        arm = new Arm(armMotor);
        flywheels = new Flywheels(flywheelMotor);
    }

    public Command deploy() {
        return arm.deploy().alongWith(flywheels.off());
    }

    public Command intake() {
        return arm.deploy().alongWith(flywheels.intake());
    }

    public Command retract() {
        return arm.retract().alongWith(flywheels.jostle());
    }
}

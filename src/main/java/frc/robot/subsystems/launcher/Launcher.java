package frc.robot.subsystems.launcher;

import java.util.function.Supplier;

import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.cancoder.CanCoderIO;
import frc.lib.motor.MotorIO;

public class Launcher extends SubsystemBase {
    private final Flywheels flywheels;
    private final Indexer indexer;
    private final Hood hood;

    public Launcher(MotorIO leftFlywheelMotor, MotorIO rightFlywheelMotor, MotorIO indexMotor, MotorIO hoodMotor,
            CanCoderIO hoodCanCoder) {
        flywheels = new Flywheels(leftFlywheelMotor, new MotorIO[] { rightFlywheelMotor });
        indexer = new Indexer(indexMotor);
        hood = new Hood(hoodMotor, hoodCanCoder);
    }

    public Command off() {
        return flywheels.off().alongWith(indexer.off()).alongWith(hood.off());
    }

    public Command launch(Supplier<AngularVelocity> flywheelVelocity, Supplier<Angle> hoodAngle) {
        return flywheels.launch(flywheelVelocity)
                .alongWith(hood.runToPosition(hoodAngle)).andThen(indexer.index());
    }
}

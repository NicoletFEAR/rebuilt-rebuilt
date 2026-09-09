package frc.robot.subsystems.drive;

import java.util.function.Supplier;

import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.wpilibj2.command.Command;
import frc.lib.module.velocity.LinearVelocityModule;
import frc.lib.motor.MotorIO;

class DriveMotor extends LinearVelocityModule {
    DriveMotor(MotorIO motor, DriveMotorConfig config) {
        super("Drive/" + config.name() + " Drive", motor, config.rotorToMechanismRatio());
    }

    Command drive(Supplier<AngularVelocity> velocity) {
        return super.
    }
}

package frc.robot.subsystems.drive;

import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.cancoder.CanCoderIO;
import frc.lib.motor.MotorIO;

class SwerveModule extends SubsystemBase {
    private final DriveMotor drive;
    private final TurnMotor turn;

    SwerveModule(MotorIO driveMotor, DriveMotorConfig driveConfig, MotorIO turnMotor, CanCoderIO turnCanCoder,
            TurnMotorConfig turnConfig) {
        drive = new DriveMotor(driveMotor, driveConfig);
        turn = new TurnMotor(turnMotor, turnCanCoder, turnConfig);
    }

    public Command applyState(SwerveModuleState state) {
        return new ComputeCommand()
    }
}

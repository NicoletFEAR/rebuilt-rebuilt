package frc.robot.subsystems.drive;

import static edu.wpi.first.units.Units.MetersPerSecond;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.LinearVelocity;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.cancoder.CanCoderIO;
import frc.lib.motor.MotorIO;

class SwerveModule extends SubsystemBase {
    private final String name;

    private final DriveMotor drive;
    private final TurnMotor turn;

    private SwerveModuleState state;

    SwerveModule(MotorIO driveMotor, MotorIO turnMotor, CanCoderIO turnCanCoder, SwerveModuleConfig config) {
        name = config.name();
        drive = new DriveMotor(driveMotor, config.getDriveMotorConfig());
        turn = new TurnMotor(turnMotor, turnCanCoder, config.getTurnMotorConfig());

        state = SwerveModuleState.OFF;
    }

    private enum SwerveModuleState {
        OFF,
        DRIVING,
    }

    @Override
    public void periodic() {
        Logger.recordOutput(name + "/State", state);

        switch (state) {
            case OFF -> {
                drive.off();
                turn.off();
            }

            case DRIVING -> {
                drive.drive();
                turn.turn();
            }
        }
    }

    void off() {
        state = SwerveModuleState.OFF;
    }

    void drive() {
        state = SwerveModuleState.DRIVING;
    }

    void setDesiredSetpoints(LinearVelocity velocity, Angle position) {
        drive.setDesiredVelocity(velocity);
        turn.setDesiredPosition(position);
    }

    void applyState(edu.wpi.first.math.kinematics.SwerveModuleState state) {
        Rotation2d currentRotation = turn.getRotation();
        state.optimize(currentRotation);
        state.cosineScale(currentRotation);
        setDesiredSetpoints(MetersPerSecond.of(state.speedMetersPerSecond), state.angle.getMeasure());
    }
}

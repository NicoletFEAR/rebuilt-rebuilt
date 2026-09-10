package frc.robot.subsystems.drive;

import static edu.wpi.first.units.Units.MetersPerSecond;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.LinearVelocity;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.cancoder.CanCoderIO;
import frc.lib.motor.MotorIO;
import frc.robot.subsystems.drive.DriveMotor.DriveMotorState;
import frc.robot.subsystems.drive.TurnMotor.TurnMotorState;
import org.littletonrobotics.junction.Logger;

public class SwerveModule extends SubsystemBase {
    private final String name;

    private final DriveMotor drive;
    private final TurnMotor turn;

    private SwerveModuleState state;

    public SwerveModule(
            MotorIO driveMotor, MotorIO turnMotor, CanCoderIO turnCanCoder, SwerveModuleConfig config) {
        name = config.name();
        drive = new DriveMotor(driveMotor, config.getDriveMotorConfig());
        turn = new TurnMotor(turnMotor, turnCanCoder, config.getTurnMotorConfig());

        state = SwerveModuleState.OFF;
    }

    public enum SwerveModuleState {
        OFF,
        DRIVE,
    }

    @Override
    public void periodic() {
        Logger.recordOutput(name + "/State", state);

        switch (state) {
            case OFF -> {
                drive.setState(DriveMotorState.OFF);
                turn.setState(TurnMotorState.OFF);
            }

            case DRIVE -> {
                drive.setState(DriveMotorState.DRIVE);
                turn.setState(TurnMotorState.TURN);
            }
        }
    }

    void setState(SwerveModuleState state) {
        this.state = state;
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

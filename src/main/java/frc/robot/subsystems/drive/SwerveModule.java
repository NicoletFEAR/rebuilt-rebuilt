package frc.robot.subsystems.drive;

import static org.wpilib.units.Units.MetersPerSecond;

import frc.lib.motor.MotorIO;
import frc.lib.sensor.absencoder.AbsEncoderIO;
import frc.robot.subsystems.drive.DriveMotor.DriveMotorState;
import frc.robot.subsystems.drive.TurnMotor.TurnMotorState;
import org.littletonrobotics.junction.Logger;
import org.wpilib.math.geometry.Rotation2d;
import org.wpilib.math.kinematics.SwerveModulePosition;
import org.wpilib.math.kinematics.SwerveModuleVelocity;
import org.wpilib.units.measure.Angle;
import org.wpilib.units.measure.LinearVelocity;

public class SwerveModule {
    private final String name;

    private final DriveMotor drive;
    private final TurnMotor turn;

    private SwerveModuleState state;

    public SwerveModule(
            MotorIO driveMotor, MotorIO turnMotor, AbsEncoderIO turnCanCoder, SwerveModuleConfig config) {
        name = config.name();
        drive = new DriveMotor(driveMotor, config.getDriveMotorConfig());
        turn = new TurnMotor(turnMotor, turnCanCoder, config.getTurnMotorConfig());

        state = SwerveModuleState.OFF;
    }

    public enum SwerveModuleState {
        OFF,
        DRIVE,
    }

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

        drive.periodic();
        turn.periodic();
    }

    void setState(SwerveModuleState state) {
        this.state = state;
    }

    void setDesiredSetpoints(LinearVelocity velocity, Angle position) {
        drive.setDesiredVelocity(velocity);
        turn.setDesiredPosition(position);
    }

    void applyState(SwerveModuleVelocity state) {
        Rotation2d currentRotation = turn.getRotation();
        state = state.optimize(currentRotation).cosineScale(currentRotation);
        setDesiredSetpoints(MetersPerSecond.of(state.velocity), state.angle.getMeasure());
    }

    SwerveModulePosition getPosition() {
        return new SwerveModulePosition(drive.getPosition(), turn.getRotation());
    }

    SwerveModuleVelocity getState() {
        return new SwerveModuleVelocity(drive.getVelocity(), turn.getRotation());
    }

    SwerveModuleVelocity getDesiredState() {
        return new SwerveModuleVelocity(drive.getDesiredVelocity(), turn.getDesiredRotation());
    }
}

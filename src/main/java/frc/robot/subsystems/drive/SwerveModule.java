package frc.robot.subsystems.drive;

import static frc.lib.units.DistanceUnit.Meters;
import static frc.lib.units.VelocityUnit.MetersPerSecond;

import frc.lib.motor.MotorIO;
import frc.lib.sensor.absencoder.AbsEncoderIO;
import frc.lib.units.AngleUnit;
import frc.robot.subsystems.drive.DriveMotor.DriveMotorState;
import frc.robot.subsystems.drive.TurnMotor.TurnMotorState;
import org.littletonrobotics.junction.Logger;
import org.wpilib.math.geometry.Rotation2d;
import org.wpilib.math.kinematics.SwerveModulePosition;
import org.wpilib.math.kinematics.SwerveModuleVelocity;

public class SwerveModule {
    private final String name;

    private final DriveMotor drive;
    private final TurnMotor turn;

    private SwerveModuleState state = new SwerveModuleState.Off();

    public SwerveModule(
            MotorIO driveMotor, MotorIO turnMotor, AbsEncoderIO turnCanCoder, SwerveModuleConfig config) {
        name = config.name();
        drive = new DriveMotor(driveMotor, config.getDriveMotorConfig());
        turn = new TurnMotor(turnMotor, turnCanCoder, config.getTurnMotorConfig());
    }

    public sealed interface SwerveModuleState permits SwerveModuleState.Off, SwerveModuleState.Drive {
        public record Off() implements SwerveModuleState {}

        public record Drive(SwerveModuleVelocity state) implements SwerveModuleState {}

        public static final Off OFF = new Off();
    }

    public void periodic() {
        Logger.recordOutput(name + "/State", (Record) state);

        switch (state) {
            case SwerveModuleState.Off() -> {
                drive.setState(DriveMotorState.OFF);
                turn.setState(TurnMotorState.OFF);
            }

            case SwerveModuleState.Drive(SwerveModuleVelocity state) -> {
                Rotation2d currentRotation = turn.getRotation();
                state = state.optimize(currentRotation).cosineScale(currentRotation);
                drive.setState(new DriveMotorState.Drive(MetersPerSecond.of(state.velocity)));
                turn.setState(new TurnMotorState.Turn(AngleUnit.ofRotation(state.angle)));
            }
        }

        drive.periodic();
        turn.periodic();
    }

    void setState(SwerveModuleState state) {
        this.state = state;
    }

    SwerveModulePosition getPosition() {
        return new SwerveModulePosition(drive.getPosition().in(Meters), turn.getRotation());
    }

    SwerveModuleVelocity getState() {
        return new SwerveModuleVelocity(drive.getVelocity().in(MetersPerSecond), turn.getRotation());
    }
}

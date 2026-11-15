package frc.robot.subsystems.drive;

import static frc.lib.units.DistanceUnit.Meters;
import static frc.lib.units.VelocityUnit.MetersPerSecond;

import frc.lib.motor.MotorIO;
import frc.lib.sensor.absencoder.AbsEncoderIO;
import frc.lib.statemachine.State;
import frc.lib.statemachine.StateMachine;
import frc.lib.units.AngleUnit;
import frc.robot.subsystems.drive.DriveMotor.DriveMotorState;
import frc.robot.subsystems.drive.TurnMotor.TurnMotorState;
import org.wpilib.math.geometry.Rotation2d;
import org.wpilib.math.kinematics.SwerveModulePosition;
import org.wpilib.math.kinematics.SwerveModuleVelocity;

public class SwerveModule extends StateMachine<SwerveModule.SwerveModuleState> {
    private final DriveMotor drive;
    private final TurnMotor turn;

    public SwerveModule(
            MotorIO driveMotor, MotorIO turnMotor, AbsEncoderIO turnCanCoder, SwerveModuleConfig config) {
        super("Drive/" + config.name(), SwerveModuleState.OFF);
        drive = new DriveMotor(driveMotor, config.getDriveMotorConfig());
        turn = new TurnMotor(turnMotor, turnCanCoder, config.getTurnMotorConfig());
    }

    public sealed interface SwerveModuleState extends State permits SwerveModuleState.Off, SwerveModuleState.Drive {
        public record Off() implements SwerveModuleState {}

        public record Drive(SwerveModuleVelocity state) implements SwerveModuleState {}

        public static final Off OFF = new Off();
    }

    public void periodic() {
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
        logState();
    }

    SwerveModulePosition getPosition() {
        return new SwerveModulePosition(drive.getPosition().in(Meters), turn.getRotation());
    }

    SwerveModuleVelocity getState() {
        return new SwerveModuleVelocity(drive.getVelocity().in(MetersPerSecond), turn.getRotation());
    }
}

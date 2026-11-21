package frc.robot.subsystems.drive;

import static frc.lib.units.DistanceUnit.Meters;
import static frc.lib.units.VelocityUnit.MetersPerSecond;

import frc.lib.motor.MotorConfig;
import frc.lib.sensor.absencoder.AbsEncoderIO;
import frc.lib.statemachine.State;
import frc.lib.statemachine.StateMachine;
import frc.lib.units.AngleUnit;
import frc.robot.subsystems.drive.DriveMotor.DriveMotorState;
import frc.robot.subsystems.drive.TurnMotor.TurnMotorState;
import org.wpilib.math.geometry.Rotation2d;
import org.wpilib.math.kinematics.SwerveModulePosition;
import org.wpilib.math.kinematics.SwerveModuleVelocity;

/**
 * One swerve module: a drive motor and a turn (azimuth) motor with an absolute encoder.
 *
 * <p>In the {@link SwerveModuleState.Drive} state, the commanded module velocity is optimized
 * against the current turn angle (including cosine scaling) before being applied to the drive and
 * turn motors.
 */
public final class SwerveModule extends StateMachine<SwerveModule.SwerveModuleState> {
    private final DriveMotor drive;
    private final TurnMotor turn;

    /**
     * Creates the swerve module.
     *
     * @param driveConfig configuration for the drive motor
     * @param turnConfig configuration for the turn motor
     * @param absEncoder interface used to communicate with the turn absolute encoder
     * @param name name of the swerve module (for differentiation from others in logs)
     */
    public SwerveModule(
            MotorConfig driveConfig,
            MotorConfig turnConfig,
            AbsEncoderIO absEncoder,
            SwerveModuleName name) {
        super("Drive/" + name, SwerveModuleState.OFF);
        drive = new DriveMotor(driveConfig, name);
        turn = new TurnMotor(turnConfig, absEncoder, name);
    }

    /** States of the swerve module. */
    static sealed interface SwerveModuleState extends State
            permits SwerveModuleState.Off, SwerveModuleState.Drive {
        /** Module is off; both motors receive zero voltage. */
        static record Off() implements SwerveModuleState {}

        /** Module is commanded to the given kinematic module velocity. */
        static record Drive(SwerveModuleVelocity state) implements SwerveModuleState {}

        /** Shared constant for the {@link Off} record to avoid unnecessary allocations. */
        static final Off OFF = new Off();
    }

    /**
     * Applies the current state to the drive and turn motors, runs their periodic logic, and logs the
     * module's state.
     *
     * <p>In the {@link SwerveModuleState.Drive} state, the commanded velocity is optimized and
     * cosine-scaled against the current turn angle before being sent to the motors.
     */
    void periodic() {
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

    /** Returns the module's position (drive distance and turn angle). */
    SwerveModulePosition getPosition() {
        return new SwerveModulePosition(drive.getPosition().in(Meters), turn.getRotation());
    }

    /** Returns the module's state (drive velocity and turn angle). */
    SwerveModuleVelocity getState() {
        return new SwerveModuleVelocity(drive.getVelocity().in(MetersPerSecond), turn.getRotation());
    }
}

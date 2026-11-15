package frc.robot;

import com.ctre.phoenix6.signals.MotorAlignmentValue;
import frc.lib.motor.MotorConfig;
import frc.lib.motor.SimMotor;
import frc.lib.motor.TalonFXMotor;
import frc.lib.sensor.absencoder.AbsEncoderConfig;
import frc.lib.sensor.absencoder.CanCoder;
import frc.lib.sensor.absencoder.SimCanCoder;
import frc.robot.constants.DeviceIds;
import frc.robot.constants.OperatorConstants;
import frc.robot.subsystems.Superstructure;
import frc.robot.subsystems.Superstructure.SuperState;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.drive.Drive.DriveConstants;
import frc.robot.subsystems.drive.DriveParameters;
import frc.robot.subsystems.drive.SwerveModule;
import frc.robot.subsystems.drive.SwerveModuleConfig;
import frc.robot.subsystems.intake.Arm.ArmConstants;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.intake.Wheels.WheelConstants;
import frc.robot.subsystems.launcher.Flywheels.FlywheelConstants;
import frc.robot.subsystems.launcher.Hood.HoodConstants;
import frc.robot.subsystems.launcher.Indexer.IndexerConstants;
import frc.robot.subsystems.launcher.Launcher;
import org.wpilib.driverstation.Gamepad;

/**
 * Constructs and wires together the robot's subsystems and translates driver input into
 * superstructure states.
 *
 * <p>The drive base is built with real hardware IO implementations when running on the robot and
 * with simulation implementations when running in simulation; see {@link #buildDrive(RobotState)}.
 * The launcher and intake are always built with the real hardware IO implementations.
 */
public class RobotContainer {
    private final RobotState robotState;
    private final Superstructure superstructure;
    private final Gamepad driverController;

    /**
     * Creates the robot container: the robot state, the driver gamepad (port 0, with deadbands
     * applied), and the superstructure built from the drive, launcher, and intake subsystems.
     */
    public RobotContainer() {
        robotState = new RobotState();
        driverController = new Gamepad(0);
        driverController.setLeftXDeadband(OperatorConstants.DEADBAND);
        driverController.setLeftYDeadband(OperatorConstants.DEADBAND);
        driverController.setLeftTriggerDeadband(OperatorConstants.DEADBAND);
        driverController.setRightXDeadband(OperatorConstants.DEADBAND);
        driverController.setRightYDeadband(OperatorConstants.DEADBAND);
        driverController.setRightTriggerDeadband(OperatorConstants.DEADBAND);
        superstructure = new Superstructure(buildDrive(robotState), buildLauncher(), buildIntake());
    }

    /**
     * Logs the robot state, decides the superstructure's desired state from the driver controller,
     * and runs the superstructure's periodic logic.
     *
     * <p>Left and right triggers above {@link OperatorConstants#TRIGGER_THRESHOLD} command the intake
     * and launcher respectively: left trigger intakes (and also spins up the launcher when the right
     * trigger is also held), right trigger launches, and the right bumper extakes. With no trigger
     * held, the robot drives with the launcher off.
     */
    public void periodic() {
        robotState.log();
        DriveParameters driveParameters =
                new DriveParameters(
                        -driverController.getLeftY(),
                        -driverController.getLeftX(),
                        driverController.getRightX());

        superstructure.setState(new SuperState.Operate(driveParameters));

        if (driverController.getLeftTrigger() > OperatorConstants.TRIGGER_THRESHOLD) {
            if (driverController.getRightTrigger() > OperatorConstants.TRIGGER_THRESHOLD) {
                superstructure.setState(new SuperState.LaunchAndIntake(driveParameters));
            } else {
                superstructure.setState(new SuperState.Intake(driveParameters));
            }
        } else if (driverController.getRightTrigger() > OperatorConstants.TRIGGER_THRESHOLD) {
            superstructure.setState(new SuperState.Launch(driveParameters));
        } else if (driverController.getRightBumperButton()) {
            superstructure.setState(new SuperState.Extake(driveParameters));
        }

        superstructure.periodic();
    }

    /**
     * Builds the drive subsystem.
     *
     * <p>When running on the robot this creates real TalonFX motors and CANcoders; in simulation it
     * creates simulated motors and CANcoders instead. Four swerve modules are created in front-left,
     * front-right, back-left, back-right order.
     */
    private Drive buildDrive(RobotState robotState) {
        if (Robot.isReal()) {
            return new Drive(
                    robotState,
                    new SwerveModule(
                            new TalonFXMotor(
                                    new MotorConfig(
                                            DeviceIds.FRONT_LEFT_DRIVE, DriveConstants.DRIVE_FEEDFORWARD_VALUES)),
                            new TalonFXMotor(
                                    new MotorConfig(
                                            DeviceIds.FRONT_LEFT_TURN, DriveConstants.TURN_FEEDFORWARD_VALUES)),
                            new CanCoder(
                                    new AbsEncoderConfig(
                                            DeviceIds.FRONT_LEFT_CAN_CODER, DriveConstants.FRONT_LEFT_OFFSET)),
                            new SwerveModuleConfig(
                                    "Front Left",
                                    DriveConstants.DRIVE_ROTOR_TO_MECHANISM_RATIO,
                                    DriveConstants.TURN_ROTOR_TO_MECHANISM_RATIO)),
                    new SwerveModule(
                            new TalonFXMotor(
                                    new MotorConfig(
                                            DeviceIds.FRONT_RIGHT_DRIVE, DriveConstants.DRIVE_FEEDFORWARD_VALUES)),
                            new TalonFXMotor(
                                    new MotorConfig(
                                            DeviceIds.FRONT_RIGHT_TURN, DriveConstants.TURN_FEEDFORWARD_VALUES)),
                            new CanCoder(
                                    new AbsEncoderConfig(
                                            DeviceIds.FRONT_RIGHT_CAN_CODER, DriveConstants.FRONT_RIGHT_OFFSET)),
                            new SwerveModuleConfig(
                                    "Front Right",
                                    DriveConstants.DRIVE_ROTOR_TO_MECHANISM_RATIO,
                                    DriveConstants.TURN_ROTOR_TO_MECHANISM_RATIO)),
                    new SwerveModule(
                            new TalonFXMotor(
                                    new MotorConfig(
                                            DeviceIds.BACK_LEFT_DRIVE, DriveConstants.DRIVE_FEEDFORWARD_VALUES)),
                            new TalonFXMotor(
                                    new MotorConfig(
                                            DeviceIds.BACK_LEFT_TURN, DriveConstants.TURN_FEEDFORWARD_VALUES)),
                            new CanCoder(
                                    new AbsEncoderConfig(
                                            DeviceIds.BACK_LEFT_CAN_CODER, DriveConstants.BACK_LEFT_OFFSET)),
                            new SwerveModuleConfig(
                                    "Back Left",
                                    DriveConstants.DRIVE_ROTOR_TO_MECHANISM_RATIO,
                                    DriveConstants.TURN_ROTOR_TO_MECHANISM_RATIO)),
                    new SwerveModule(
                            new TalonFXMotor(
                                    new MotorConfig(
                                            DeviceIds.BACK_RIGHT_DRIVE, DriveConstants.DRIVE_FEEDFORWARD_VALUES)),
                            new TalonFXMotor(
                                    new MotorConfig(
                                            DeviceIds.BACK_RIGHT_TURN, DriveConstants.TURN_FEEDFORWARD_VALUES)),
                            new CanCoder(
                                    new AbsEncoderConfig(
                                            DeviceIds.BACK_RIGHT_CAN_CODER, DriveConstants.BACK_RIGHT_OFFSET)),
                            new SwerveModuleConfig(
                                    "Back Right",
                                    DriveConstants.DRIVE_ROTOR_TO_MECHANISM_RATIO,
                                    DriveConstants.TURN_ROTOR_TO_MECHANISM_RATIO)));
        } else {
            return new Drive(
                    robotState,
                    new SwerveModule(
                            new SimMotor(
                                    new MotorConfig(
                                            DeviceIds.FRONT_LEFT_DRIVE, DriveConstants.DRIVE_FEEDFORWARD_VALUES)),
                            new SimMotor(
                                    new MotorConfig(
                                            DeviceIds.FRONT_LEFT_TURN, DriveConstants.TURN_FEEDFORWARD_VALUES)),
                            new SimCanCoder(
                                    new AbsEncoderConfig(
                                            DeviceIds.FRONT_LEFT_CAN_CODER, DriveConstants.FRONT_LEFT_OFFSET)),
                            new SwerveModuleConfig(
                                    "Front Left",
                                    DriveConstants.DRIVE_ROTOR_TO_MECHANISM_RATIO,
                                    DriveConstants.TURN_ROTOR_TO_MECHANISM_RATIO)),
                    new SwerveModule(
                            new SimMotor(
                                    new MotorConfig(
                                            DeviceIds.FRONT_RIGHT_DRIVE, DriveConstants.DRIVE_FEEDFORWARD_VALUES)),
                            new SimMotor(
                                    new MotorConfig(
                                            DeviceIds.FRONT_RIGHT_TURN, DriveConstants.TURN_FEEDFORWARD_VALUES)),
                            new SimCanCoder(
                                    new AbsEncoderConfig(
                                            DeviceIds.FRONT_RIGHT_CAN_CODER, DriveConstants.FRONT_RIGHT_OFFSET)),
                            new SwerveModuleConfig(
                                    "Front Right",
                                    DriveConstants.DRIVE_ROTOR_TO_MECHANISM_RATIO,
                                    DriveConstants.TURN_ROTOR_TO_MECHANISM_RATIO)),
                    new SwerveModule(
                            new SimMotor(
                                    new MotorConfig(
                                            DeviceIds.BACK_LEFT_DRIVE, DriveConstants.DRIVE_FEEDFORWARD_VALUES)),
                            new SimMotor(
                                    new MotorConfig(
                                            DeviceIds.BACK_LEFT_TURN, DriveConstants.TURN_FEEDFORWARD_VALUES)),
                            new SimCanCoder(
                                    new AbsEncoderConfig(
                                            DeviceIds.BACK_LEFT_CAN_CODER, DriveConstants.BACK_LEFT_OFFSET)),
                            new SwerveModuleConfig(
                                    "Back Left",
                                    DriveConstants.DRIVE_ROTOR_TO_MECHANISM_RATIO,
                                    DriveConstants.TURN_ROTOR_TO_MECHANISM_RATIO)),
                    new SwerveModule(
                            new SimMotor(
                                    new MotorConfig(
                                            DeviceIds.BACK_RIGHT_DRIVE, DriveConstants.DRIVE_FEEDFORWARD_VALUES)),
                            new SimMotor(
                                    new MotorConfig(
                                            DeviceIds.BACK_RIGHT_TURN, DriveConstants.TURN_FEEDFORWARD_VALUES)),
                            new SimCanCoder(
                                    new AbsEncoderConfig(
                                            DeviceIds.BACK_RIGHT_CAN_CODER, DriveConstants.BACK_RIGHT_OFFSET)),
                            new SwerveModuleConfig(
                                    "Back Right",
                                    DriveConstants.DRIVE_ROTOR_TO_MECHANISM_RATIO,
                                    DriveConstants.TURN_ROTOR_TO_MECHANISM_RATIO)));
        }
    }

    /**
     * Builds the launcher subsystem (two flywheel motors, an indexer, a hood, and a hood encoder).
     *
     * <p>Unlike {@link #buildDrive(RobotState)}, this method does not branch on {@code
     * Robot.isReal()} and always creates real TalonFX motor implementations.
     */
    private Launcher buildLauncher() {
        return new Launcher(
                new TalonFXMotor(
                        new MotorConfig(DeviceIds.LEFT_LAUNCHER, FlywheelConstants.FEEDFORWARD_VALUES)),
                new TalonFXMotor(
                        new MotorConfig(
                                DeviceIds.RIGHT_LAUNCHER,
                                FlywheelConstants.FEEDFORWARD_VALUES,
                                MotorAlignmentValue.Opposed)),
                new TalonFXMotor(new MotorConfig(DeviceIds.INDEXER, IndexerConstants.FEEDFORWARD_VALUES)),
                new TalonFXMotor(new MotorConfig(DeviceIds.HOOD, HoodConstants.FEEDFORWARD_VALUES)),
                new CanCoder(new AbsEncoderConfig(DeviceIds.HOOD_ENCODER, HoodConstants.HOOD_OFFSET)));
    }

    /**
     * Builds the intake subsystem (arm and wheels).
     *
     * <p>Unlike {@link #buildDrive(RobotState)}, this method does not branch on {@code
     * Robot.isReal()} and always creates real TalonFX motor implementations.
     */
    private Intake buildIntake() {
        return new Intake(
                new TalonFXMotor(new MotorConfig(DeviceIds.INTAKE_ARM, ArmConstants.FEEDFORWARD_VALUES)),
                new TalonFXMotor(
                        new MotorConfig(DeviceIds.INTAKE_WHEELS, WheelConstants.FEEDFORWARD_VALUES)));
    }
}

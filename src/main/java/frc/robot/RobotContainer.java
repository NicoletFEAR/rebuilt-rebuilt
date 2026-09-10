package frc.robot;

import static edu.wpi.first.units.Units.KilogramSquareMeters;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.cancoder.CanCoderConfig;
import frc.lib.cancoder.RealCanCoder;
import frc.lib.motor.KrakenType;
import frc.lib.motor.MotorConfig;
import frc.lib.motor.RealMotor;
import frc.robot.constants.DeviceIds;
import frc.robot.subsystems.Superstructure;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.drive.Drive.DriveConstants;
import frc.robot.subsystems.drive.SwerveModule;
import frc.robot.subsystems.drive.SwerveModuleConfig;
import frc.robot.subsystems.intake.Arm.ArmConstants;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.intake.Wheels.WheelConstants;
import frc.robot.subsystems.launcher.Flywheels.FlywheelConstants;
import frc.robot.subsystems.launcher.Hood.HoodConstants;
import frc.robot.subsystems.launcher.Indexer.IndexerConstants;
import frc.robot.subsystems.launcher.Launcher;

public class RobotContainer extends SubsystemBase {
    private final Superstructure superstructure;
    private final XboxController driverController;

    public RobotContainer() {
        driverController = new XboxController(0);
        superstructure = new Superstructure(buildDrive(), buildLauncher(), buildIntake());
    }

    private Drive buildDrive() {
        return new Drive(
                new SwerveModule(
                        new RealMotor(
                                new MotorConfig(
                                        DeviceIds.FRONT_LEFT_DRIVE,
                                        DriveConstants.DRIVE_FEEDFORWARD_VALUES,
                                        KrakenType.X60,
                                        KilogramSquareMeters.of(0.01))),
                        new RealMotor(
                                new MotorConfig(
                                        DeviceIds.FRONT_LEFT_TURN,
                                        DriveConstants.TURN_FEEDFORWARD_VALUES,
                                        KrakenType.X44,
                                        KilogramSquareMeters.of(0.01))),
                        new RealCanCoder(
                                new CanCoderConfig(
                                        DeviceIds.FRONT_LEFT_CAN_CODER, DriveConstants.FRONT_LEFT_OFFSET)),
                        new SwerveModuleConfig(
                                "FrontLeft",
                                DriveConstants.DRIVE_ROTOR_TO_MECHANISM_RATIO,
                                DriveConstants.TURN_ROTOR_TO_MECHANISM_RATIO)),
                new SwerveModule(
                        new RealMotor(
                                new MotorConfig(
                                        DeviceIds.FRONT_RIGHT_DRIVE,
                                        DriveConstants.DRIVE_FEEDFORWARD_VALUES,
                                        KrakenType.X60,
                                        KilogramSquareMeters.of(0.01))),
                        new RealMotor(
                                new MotorConfig(
                                        DeviceIds.FRONT_RIGHT_TURN,
                                        DriveConstants.TURN_FEEDFORWARD_VALUES,
                                        KrakenType.X44,
                                        KilogramSquareMeters.of(0.01))),
                        new RealCanCoder(
                                new CanCoderConfig(
                                        DeviceIds.FRONT_RIGHT_CAN_CODER, DriveConstants.FRONT_RIGHT_OFFSET)),
                        new SwerveModuleConfig(
                                "FrontRight",
                                DriveConstants.DRIVE_ROTOR_TO_MECHANISM_RATIO,
                                DriveConstants.TURN_ROTOR_TO_MECHANISM_RATIO)),
                new SwerveModule(
                        new RealMotor(
                                new MotorConfig(
                                        DeviceIds.BACK_LEFT_DRIVE,
                                        DriveConstants.DRIVE_FEEDFORWARD_VALUES,
                                        KrakenType.X60,
                                        KilogramSquareMeters.of(0.01))),
                        new RealMotor(
                                new MotorConfig(
                                        DeviceIds.BACK_LEFT_TURN,
                                        DriveConstants.TURN_FEEDFORWARD_VALUES,
                                        KrakenType.X44,
                                        KilogramSquareMeters.of(0.01))),
                        new RealCanCoder(
                                new CanCoderConfig(DeviceIds.BACK_LEFT_CAN_CODER, DriveConstants.BACK_LEFT_OFFSET)),
                        new SwerveModuleConfig(
                                "BackLeft",
                                DriveConstants.DRIVE_ROTOR_TO_MECHANISM_RATIO,
                                DriveConstants.TURN_ROTOR_TO_MECHANISM_RATIO)),
                new SwerveModule(
                        new RealMotor(
                                new MotorConfig(
                                        DeviceIds.BACK_RIGHT_DRIVE,
                                        DriveConstants.DRIVE_FEEDFORWARD_VALUES,
                                        KrakenType.X60,
                                        KilogramSquareMeters.of(0.01))),
                        new RealMotor(
                                new MotorConfig(
                                        DeviceIds.BACK_RIGHT_TURN,
                                        DriveConstants.TURN_FEEDFORWARD_VALUES,
                                        KrakenType.X44,
                                        KilogramSquareMeters.of(0.01))),
                        new RealCanCoder(
                                new CanCoderConfig(
                                        DeviceIds.BACK_RIGHT_CAN_CODER, DriveConstants.BACK_RIGHT_OFFSET)),
                        new SwerveModuleConfig(
                                "BackRight",
                                DriveConstants.DRIVE_ROTOR_TO_MECHANISM_RATIO,
                                DriveConstants.TURN_ROTOR_TO_MECHANISM_RATIO)));
    }

    private Launcher buildLauncher() {
        return new Launcher(
                new RealMotor(
                        new MotorConfig(
                                DeviceIds.LEFT_LAUNCHER, FlywheelConstants.FEEDFORWARD_VALUES, KrakenType.X60)),
                new RealMotor(
                        new MotorConfig(
                                DeviceIds.RIGHT_LAUNCHER, FlywheelConstants.FEEDFORWARD_VALUES, KrakenType.X60)),
                new RealMotor(
                        new MotorConfig(
                                DeviceIds.INDEXER, IndexerConstants.FEEDFORWARD_VALUES, KrakenType.X44)),
                new RealMotor(
                        new MotorConfig(DeviceIds.HOOD, HoodConstants.FEEDFORWARD_VALUES, KrakenType.X44)),
                new RealCanCoder(new CanCoderConfig(DeviceIds.HOOD_ENCODER, HoodConstants.HOOD_OFFSET)));
    }

    private Intake buildIntake() {
        return new Intake(
                new RealMotor(
                        new MotorConfig(DeviceIds.INTAKE_ARM, ArmConstants.FEEDFORWARD_VALUES, KrakenType.X44)),
                new RealMotor(
                        new MotorConfig(
                                DeviceIds.INTAKE_WHEELS, WheelConstants.FEEDFORWARD_VALUES, KrakenType.X44)));
    }
}

package frc.robot.subsystems.drive;

public record SwerveModuleConfig(
        String name, double driveRotorToMechanismRatio, double turnRotorToMechanismRatio) {
    public DriveMotorConfig getDriveMotorConfig() {
        return new DriveMotorConfig(name, driveRotorToMechanismRatio);
    }

    public TurnMotorConfig getTurnMotorConfig() {
        return new TurnMotorConfig(name, turnRotorToMechanismRatio);
    }
}

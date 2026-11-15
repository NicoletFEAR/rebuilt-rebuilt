package frc.robot.subsystems.drive;

/**
 * Configuration for a swerve module.
 *
 * @param name name used to identify and log the module (also prefixes its subcomponents)
 * @param driveRotorToMechanismRatio rotor-to-mechanism ratio for the drive mechanism
 * @param turnRotorToMechanismRatio rotor-to-mechanism ratio for the turn mechanism
 */
public record SwerveModuleConfig(
        String name, double driveRotorToMechanismRatio, double turnRotorToMechanismRatio) {

    /** Returns a {@link DriveMotorConfig} derived from this module's name and drive ratio. */
    public DriveMotorConfig getDriveMotorConfig() {
        return new DriveMotorConfig(name, driveRotorToMechanismRatio);
    }

    /** Returns a {@link TurnMotorConfig} derived from this module's name and turn ratio. */
    public TurnMotorConfig getTurnMotorConfig() {
        return new TurnMotorConfig(name, turnRotorToMechanismRatio);
    }
}

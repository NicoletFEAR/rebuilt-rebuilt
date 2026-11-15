package frc.robot.subsystems.drive;

/**
 * Configuration for a swerve module's drive motor.
 *
 * @param name name used to identify and log the module (also prefixes its subcomponents)
 * @param rotorToMechanismRatio rotor-to-mechanism ratio for the drive mechanism
 */
public record DriveMotorConfig(String name, double rotorToMechanismRatio) {}

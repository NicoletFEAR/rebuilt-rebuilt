package frc.robot.subsystems.drive;

/**
 * Configuration for a swerve module's turn motor.
 *
 * @param name name used to identify and log the module (also prefixes its subcomponents)
 * @param rotorToMechanismRatio rotor-to-mechanism ratio for the turn mechanism
 */
public record TurnMotorConfig(String name, double rotorToMechanismRatio) {}

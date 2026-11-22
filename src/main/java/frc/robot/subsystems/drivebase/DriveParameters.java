package frc.robot.subsystems.drivebase;

/**
 * Driver input parameters for teleop driving.
 *
 * @param x normalized forward input (1 = full forward)
 * @param y normalized strafe input (1 = full left)
 * @param omega normalized rotation input (1 = full counterclockwise)
 */
public record DriveParameters(double x, double y, double omega) {}

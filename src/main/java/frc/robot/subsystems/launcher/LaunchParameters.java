package frc.robot.subsystems.launcher;

import frc.lib.units.Angle;
import frc.lib.units.AngularVelocity;

/**
 * Launcher parameters for setting aim.
 * 
 * @param flywheelVelocity velocity at which to spin the flywheels
 * @param hoodAngle angle to which to move the hood
 */
public record LaunchParameters(AngularVelocity flywheelVelocity, Angle hoodAngle) {}

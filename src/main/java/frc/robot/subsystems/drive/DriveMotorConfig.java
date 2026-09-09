package frc.robot.subsystems.drive;

import lombok.Builder;

@Builder
public record DriveMotorConfig(String name, double rotorToMechanismRatio) {
}

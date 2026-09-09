package frc.robot.subsystems.drive;

import lombok.Builder;

@Builder
public record TurnMotorConfig(String name, double rotorToMechanismRatio) {

}

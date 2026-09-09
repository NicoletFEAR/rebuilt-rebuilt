package frc.robot.subsystems.drive;

import frc.lib.cancoder.CanCoderIO;
import frc.lib.module.CanCoderAngularPositionModule;
import frc.lib.motor.MotorIO;

class TurnMotor extends CanCoderAngularPositionModule {
    TurnMotor(MotorIO motor, CanCoderIO canCoder, TurnMotorConfig config) {
        super("Drive/" + config.name() + " Turn", motor, canCoder, config.rotorToMechanismRatio());
    }
}

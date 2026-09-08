package frc.robot.subsystems.intake;

import static edu.wpi.first.units.Units.Meters;

import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.wpilibj2.command.Command;
import frc.lib.module.LinearPositionModule;
import frc.lib.motor.MotorIO;

class Arm extends LinearPositionModule {
    Arm(MotorIO motor) {
        super("Intake/Arm", motor, ArmConstants.ROTOR_TO_MECHANISM_RATIO);
    }

    Command retract() {
        return super.setPositionSetpoint(Meters.of(0.0));
    }

    Command deploy() {
        return super.setPositionSetpoint(ArmConstants.DEPLOY_DISTANCE);
    }

    private static final class ArmConstants {
        private static final double ROTOR_TO_MECHANISM_RATIO = 40.0 / 3.0;

        private static final Distance DEPLOY_DISTANCE = Meters.of(0.2);

        private ArmConstants() {
        }
    }
}

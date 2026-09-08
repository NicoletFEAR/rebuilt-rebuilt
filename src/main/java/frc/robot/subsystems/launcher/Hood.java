package frc.robot.subsystems.launcher;

import static edu.wpi.first.units.Units.Volts;

import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.wpilibj2.command.Command;
import frc.lib.cancoder.CanCoderIO;
import frc.lib.module.CanCoderAngularPositionModule;
import frc.lib.motor.MotorIO;

class Hood extends CanCoderAngularPositionModule {
    Hood(MotorIO motor, CanCoderIO canCoder) {
        super("Launcher/Hood", motor, canCoder, HoodConstants.ROTOR_TO_MECHANISM_RATIO);
    }

    Command off() {
        return super.setVoltage(Volts.of(0.0));
    }

    @Override
    protected Command runToPosition(Angle position) {
        return super.runToPosition(position);
    }

    private static final class HoodConstants {
        private static final double ROTOR_TO_MECHANISM_RATIO = 30.0;

        private HoodConstants() {
        }
    }
}

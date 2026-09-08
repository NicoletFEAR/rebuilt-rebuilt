package frc.robot.subsystems;

import static edu.wpi.first.units.Units.Volts;

import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.wpilibj2.command.Command;
import frc.lib.cancoder.CanCoderIO;
import frc.lib.module.CanCoderAngularPositionModule;
import frc.lib.motor.MotorIO;

public class Hood extends CanCoderAngularPositionModule {
    public Hood(MotorIO motor, CanCoderIO canCoder) {
        super("Launcher/Hood", motor, canCoder, HoodConstants.ROTOR_TO_MECHANISM_RATIO);
    }

    public Command off() {
        return super.setVoltage(Volts.of(0.0));
    }

    @Override
    public Command runToPosition(Angle position) {
        return super.runToPosition(position);
    }

    public static final class HoodConstants {
        private static final double ROTOR_TO_MECHANISM_RATIO = 30.0;

        private HoodConstants() {
        }
    }
}

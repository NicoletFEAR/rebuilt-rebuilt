package frc.lib.module;

import edu.wpi.first.units.measure.Angle;
import frc.lib.cancoder.CanCoderIO;
import frc.lib.cancoder.CanCoderIOInputsAutoLogged;
import frc.lib.motor.MotorIO;
import org.littletonrobotics.junction.Logger;

public class CanCoderAngularPositionModule extends AngularPositionModule {
    private final CanCoderIO canCoderIO;
    private final CanCoderIOInputsAutoLogged canCoderInputs;

    public CanCoderAngularPositionModule(
            String name, MotorIO motorIO, CanCoderIO canCoderIO, double rotorToMechanismRatio) {
        super(name, motorIO, rotorToMechanismRatio);
        this.canCoderIO = canCoderIO;
        canCoderInputs = new CanCoderIOInputsAutoLogged();
    }

    @Override
    public void periodic() {
        super.periodic();
        canCoderIO.updateInputs(canCoderInputs);
        Logger.processInputs(
                name + "/CANCoder", canCoderInputs.applyRotorToMechanismRatio(rotorToMechanismRatio));
    }

    @Override
    public Angle getPosition() {
        if (canCoderInputs.connected) {
            return canCoderInputs.position.div(rotorToMechanismRatio);
        } else {
            return super.getPosition().div(rotorToMechanismRatio);
        }
    }
}

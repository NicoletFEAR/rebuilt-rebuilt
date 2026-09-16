package frc.lib.module.position;

import edu.wpi.first.units.measure.Angle;
import frc.lib.absencoder.AbsEncoderIO;
import frc.lib.cancoder.AbsEncoderIOInputsAutoLogged;
import frc.lib.motor.MotorIO;
import org.littletonrobotics.junction.Logger;

public class AbsEncoderAngularPositionModule extends AngularPositionModule {
    private final AbsEncoderIO absEncoderIO;
    private final AbsEncoderIOInputsAutoLogged absEncoderInputs;

    public AbsEncoderAngularPositionModule(
            String name, MotorIO motorIO, AbsEncoderIO canCoderIO, double rotorToMechanismRatio) {
        super(name, motorIO, rotorToMechanismRatio);
        this.absEncoderIO = canCoderIO;
        absEncoderInputs = new AbsEncoderIOInputsAutoLogged();
    }

    @Override
    public void periodic() {
        super.periodic();
        absEncoderIO.updateInputs(absEncoderInputs);
        Logger.processInputs(
                name + "AbsEncoder", absEncoderInputs.applyRotorToMechanismRatio(rotorToMechanismRatio));
    }

    @Override
    public Angle getPosition() {
        if (absEncoderInputs.connected) {
            return absEncoderInputs.position.div(rotorToMechanismRatio);
        } else {
            return super.getPosition().div(rotorToMechanismRatio);
        }
    }
}

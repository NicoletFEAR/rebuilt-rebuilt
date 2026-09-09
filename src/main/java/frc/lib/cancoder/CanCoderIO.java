package frc.lib.cancoder;

import static edu.wpi.first.units.Units.Radians;
import static edu.wpi.first.units.Units.RadiansPerSecond;

import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;

import org.littletonrobotics.junction.AutoLog;

public interface CanCoderIO {
    @AutoLog
    public static class CanCoderIOInputs {
        public boolean connected = false;
        public Angle position = Radians.of(0.0);
        public AngularVelocity velocity = RadiansPerSecond.of(0.0);

        public CanCoderIOInputsAutoLogged applyRotorToMechanismRatio(double rotorToMechanismRatio) {
            CanCoderIOInputsAutoLogged result = new CanCoderIOInputsAutoLogged();
            result.connected = connected;
            result.position = position.div(rotorToMechanismRatio);
            result.velocity = velocity.div(rotorToMechanismRatio);
            return result;
        }
    }

    void updateInputs(CanCoderIOInputs inputs);

    void setZeroPoint();
}

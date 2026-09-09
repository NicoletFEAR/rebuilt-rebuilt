package frc.lib.motor;

import static edu.wpi.first.units.Units.Amps;
import static edu.wpi.first.units.Units.Celsius;
import static edu.wpi.first.units.Units.Radians;
import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.Volts;

import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.units.measure.Temperature;
import edu.wpi.first.units.measure.Voltage;
import frc.lib.CanId;
import lombok.Getter;
import org.littletonrobotics.junction.AutoLog;

public abstract class MotorIO {
    @Getter
    private final MotorConfig config;

    public MotorIO(MotorConfig config) {
        this.config = config;
    }

    @AutoLog
    public static class MotorIOInputs {
        public boolean connected = false;
        public Angle position = Radians.of(0.0);
        public AngularVelocity velocity = RadiansPerSecond.of(0.0);
        public Voltage voltage = Volts.of(0.0);
        public Current statorCurrent = Amps.of(0.0);
        public Current supplyCurrent = Amps.of(0.0);
        public Temperature temperature = Celsius.of(21.0);

        public MotorIOInputsAutoLogged applyRotorToMechanismRatio(double rotorToMechanismRatio) {
            MotorIOInputsAutoLogged result = new MotorIOInputsAutoLogged();
            result.connected = connected;
            result.position = position.div(rotorToMechanismRatio);
            result.velocity = velocity.div(rotorToMechanismRatio);
            result.voltage = voltage;
            result.statorCurrent = statorCurrent;
            result.supplyCurrent = supplyCurrent;
            result.temperature = temperature;
            return result;
        }
    }

    public abstract void updateInputs(MotorIOInputs inputs);

    public abstract void follow(CanId leader, MotorAlignmentValue alignment);

    public abstract void setNeutralMode(NeutralModeValue mode);

    public abstract void setPositionSetpoint(Angle position);

    public abstract void setVelocitySetpoint(AngularVelocity velocity);

    public abstract void setVoltage(Voltage voltage);
}

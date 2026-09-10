package frc.lib.module;

import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.motor.MotorIO;
import frc.lib.motor.MotorIOInputsAutoLogged;
import org.littletonrobotics.junction.Logger;

public abstract class Module extends SubsystemBase {
    protected final MotorIO io;
    protected final MotorIOInputsAutoLogged inputs;
    protected final String name;
    protected final double rotorToMechanismRatio;

    public Module(String name, MotorIO io, double rotorToMechanismRatio) {
        super(name);
        this.name = getName();
        this.io = io;
        inputs = new MotorIOInputsAutoLogged();
        this.rotorToMechanismRatio = rotorToMechanismRatio;
    }

    @Override
    public void periodic() {
        io.updateInputs(inputs);
        Logger.processInputs(name, inputs.applyRotorToMechanismRatio(rotorToMechanismRatio));
    }

    protected void setNeutralMode(NeutralModeValue mode) {
        io.setNeutralMode(mode);
    }

    protected void setVoltage(Voltage voltage) {
        io.setVoltage(voltage);
    }
}

package frc.lib.module.position;

import java.util.function.Supplier;

import org.littletonrobotics.junction.Logger;

import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.motor.MotorIO;
import frc.lib.motor.MotorIOInputsAutoLogged;

public abstract class PositionModule extends SubsystemBase {
    protected final MotorIO io;
    protected final MotorIOInputsAutoLogged inputs;
    protected final String name;
    protected final double rotorToMechanismRatio;

    public PositionModule(String name, MotorIO io, double rotorToMechanismRatio) {
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

    protected Command setNeutralMode(Supplier<NeutralModeValue> mode) {
        return io.setNeutralMode(mode);
    }

    protected Command setVoltage(Supplier<Voltage> voltage) {
        return io.setVoltage(voltage);
    }
}

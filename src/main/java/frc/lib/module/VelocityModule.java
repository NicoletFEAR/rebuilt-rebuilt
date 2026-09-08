package frc.lib.module;

import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.motor.MotorIO;
import frc.lib.motor.MotorIOInputsAutoLogged;
import frc.lib.constants.Constants;

import static edu.wpi.first.units.Units.RadiansPerSecond;

import org.littletonrobotics.junction.Logger;

public class VelocityModule extends SubsystemBase {
    private final MotorIO io;
    private final MotorIOInputsAutoLogged inputs;
    protected final String name;
    protected final double rotorToMechanismRatio;

    public VelocityModule(String name, MotorIO io, double rotorToMechanismRatio) {
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

    public Command setNeutralMode(NeutralModeValue mode) {
        return io.setNeutralMode(mode);
    }

    public Command setVelocitySetpoint(AngularVelocity velocity) {
        return io.setVelocitySetpoint(velocity);
    }

    public Command runToVelocity(AngularVelocity velocity) {
        double velocityRadiansPerSecond = velocity.in(RadiansPerSecond);
        return setVelocitySetpoint(velocity)
                .andThen(Commands.waitUntil(() -> MathUtil.isNear(velocityRadiansPerSecond,
                        getVelocity().in(RadiansPerSecond),
                        Constants.VELOCITY_SETPOINT_TOLERANCE_MULTIPLIER * velocityRadiansPerSecond)));
    }

    public AngularVelocity getVelocity() {
        return inputs.velocity;
    }
}

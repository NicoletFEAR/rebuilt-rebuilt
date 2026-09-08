package frc.lib.module;

import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.constants.Constants;
import frc.lib.motor.MotorIO;
import frc.lib.motor.MotorIOInputsAutoLogged;

import static edu.wpi.first.units.Units.Radians;

import org.littletonrobotics.junction.Logger;

public class AngularPositionModule extends SubsystemBase {
    private final MotorIO io;
    private final MotorIOInputsAutoLogged inputs;
    protected final String name;
    protected final double rotorToMechanismRatio;

    public AngularPositionModule(String name, MotorIO io, double rotorToMechanismRatio) {
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

    public Command setPositionSetpoint(Angle position) {
        return io.setPositionSetpoint(position.times(rotorToMechanismRatio));
    }

    public Command runToPosition(Angle position) {
        double positionRadians = position.in(Radians);
        return setPositionSetpoint(position)
                .andThen(Commands.waitUntil(() -> MathUtil.isNear(positionRadians,
                        getPosition().in(Radians),
                        Constants.ANGULAR_POSITION_SETPOINT_TOLERANCE.in(Radians))));
    }

    public Angle getPosition() {
        return inputs.position.div(rotorToMechanismRatio);
    }
}

package frc.lib.module.position;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.lib.constants.Constants;
import frc.lib.motor.MotorIO;

import static edu.wpi.first.units.Units.Radians;

public class AngularPositionModule extends PositionModule {
    public AngularPositionModule(String name, MotorIO io, double rotorToMechanismRatio) {
        super(name, io, rotorToMechanismRatio);
    }

    protected Command setPositionSetpoint(Angle position) {
        return io.setPositionSetpoint(position.times(rotorToMechanismRatio));
    }

    protected Command runToPosition(Angle position) {
        return setPositionSetpoint(position)
                .andThen(Commands.waitUntil(() -> MathUtil.isNear(position.in(Radians),
                        getPosition().in(Radians),
                        Constants.ANGULAR_POSITION_SETPOINT_TOLERANCE.in(Radians))));
    }

    public Angle getPosition() {
        return inputs.position.div(rotorToMechanismRatio);
    }
}

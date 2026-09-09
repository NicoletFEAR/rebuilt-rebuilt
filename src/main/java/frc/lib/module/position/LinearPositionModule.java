package frc.lib.module.position;

import static edu.wpi.first.units.Units.Meters;
import static edu.wpi.first.units.Units.Radians;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.lib.constants.Constants;
import frc.lib.motor.MotorIO;

public class LinearPositionModule extends PositionModule {
    public LinearPositionModule(String name, MotorIO io, double rotorToMechanismRatio) {
        super(name, io, rotorToMechanismRatio);
    }

    protected Command setPositionSetpoint(Distance position) {
        return io.setPositionSetpoint(Radians.of(position.in(Meters) * rotorToMechanismRatio));
    }

    protected Command runToPosition(Distance position) {
        return setPositionSetpoint(position)
                .andThen(Commands.waitUntil(() -> MathUtil.isNear(position.in(Meters),
                        getPosition().in(Meters),
                        Constants.LINEAR_POSITION_SETPOINT_TOLERANCE.in(Meters))));
    }

    public Distance getPosition() {
        return Meters.of(inputs.position.in(Radians) / rotorToMechanismRatio);
    }
}

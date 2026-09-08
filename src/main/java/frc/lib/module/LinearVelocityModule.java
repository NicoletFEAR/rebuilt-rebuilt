package frc.lib.module;

import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.RadiansPerSecond;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.units.measure.LinearVelocity;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.lib.constants.Constants;
import frc.lib.motor.MotorIO;

public class LinearVelocityModule extends VelocityModule {
    public LinearVelocityModule(String name, MotorIO io, double rotorToMechanismRatio) {
        super(name, io, rotorToMechanismRatio);
    }

    public Command setVelocitySetpoint(LinearVelocity velocity) {
        return io.setVelocitySetpoint(RadiansPerSecond.of(velocity.in(MetersPerSecond) * rotorToMechanismRatio));
    }

    public Command runToVelocity(LinearVelocity velocity) {
        double velocityMetersPerSecond = velocity.in(MetersPerSecond);
        return setVelocitySetpoint(velocity)
                .andThen(Commands.waitUntil(() -> MathUtil.isNear(velocityMetersPerSecond,
                        getVelocity().in(MetersPerSecond),
                        Constants.VELOCITY_SETPOINT_TOLERANCE_MULTIPLIER * velocityMetersPerSecond)));
    }

    public LinearVelocity getVelocity() {
        return MetersPerSecond.of(inputs.velocity.in(RadiansPerSecond) / rotorToMechanismRatio);
    }
}

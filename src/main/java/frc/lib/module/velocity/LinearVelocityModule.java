package frc.lib.module.velocity;

import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.RadiansPerSecond;

import java.util.function.Supplier;

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

    protected Command setVelocitySetpoint(Supplier<LinearVelocity> velocity) {
        return io.setVelocitySetpoint(
                () -> RadiansPerSecond.of(velocity.get().in(MetersPerSecond) * rotorToMechanismRatio));
    }

    protected Command runToVelocity(Supplier<LinearVelocity> velocity) {
        return setVelocitySetpoint(velocity)
                .alongWith(Commands.waitUntil(() -> MathUtil.isNear(velocity.get().in(MetersPerSecond),
                        getVelocity().in(MetersPerSecond),
                        Constants.VELOCITY_SETPOINT_TOLERANCE_MULTIPLIER * velocity.get().in(MetersPerSecond))));
    }

    public LinearVelocity getVelocity() {
        return MetersPerSecond.of(inputs.velocity.in(RadiansPerSecond) / rotorToMechanismRatio);
    }
}

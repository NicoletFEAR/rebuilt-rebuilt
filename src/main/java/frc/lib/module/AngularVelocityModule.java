package frc.lib.module;

import static edu.wpi.first.units.Units.RadiansPerSecond;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.lib.constants.Constants;
import frc.lib.motor.MotorIO;

public class AngularVelocityModule extends VelocityModule {
    public AngularVelocityModule(String name, MotorIO motor, double rotorToMechanismRatio) {
        super(name, motor, rotorToMechanismRatio);
    }

    protected Command setVelocitySetpoint(AngularVelocity velocity) {
        return io.setVelocitySetpoint(velocity);
    }

    protected Command runToVelocity(AngularVelocity velocity) {
        double velocityRadiansPerSecond = velocity.in(RadiansPerSecond);
        return setVelocitySetpoint(velocity)
                .andThen(Commands.waitUntil(() -> MathUtil.isNear(velocityRadiansPerSecond,
                        getVelocity().in(RadiansPerSecond),
                        Constants.VELOCITY_SETPOINT_TOLERANCE_MULTIPLIER * velocityRadiansPerSecond)));
    }

    public AngularVelocity getVelocity() {
        return inputs.velocity.div(rotorToMechanismRatio);
    }
}

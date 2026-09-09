package frc.lib.module;

import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.RadiansPerSecond;

import edu.wpi.first.units.measure.LinearVelocity;
import frc.lib.motor.MotorIO;

public class LinearVelocityModule extends Module {
    public LinearVelocityModule(String name, MotorIO io, double rotorToMechanismRatio) {
        super(name, io, rotorToMechanismRatio);
    }

    protected void setVelocitySetpoint(LinearVelocity velocity) {
        io.setVelocitySetpoint(RadiansPerSecond.of(velocity.in(MetersPerSecond) * rotorToMechanismRatio));
    }

    public LinearVelocity getVelocity() {
        return MetersPerSecond.of(inputs.velocity.in(RadiansPerSecond) / rotorToMechanismRatio);
    }
}

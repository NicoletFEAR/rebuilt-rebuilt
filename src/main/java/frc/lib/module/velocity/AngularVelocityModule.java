package frc.lib.module.velocity;

import edu.wpi.first.units.measure.AngularVelocity;
import frc.lib.module.Module;
import frc.lib.motor.MotorIO;

public class AngularVelocityModule extends Module {
    public AngularVelocityModule(String name, MotorIO motor, double rotorToMechanismRatio) {
        super(name, motor, rotorToMechanismRatio);
    }

    protected void setVelocitySetpoint(AngularVelocity velocity) {
        io.setVelocitySetpoint(velocity);
    }

    public AngularVelocity getVelocity() {
        return inputs.velocity.div(rotorToMechanismRatio);
    }
}

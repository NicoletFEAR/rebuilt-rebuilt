package frc.lib.module.position;

import static edu.wpi.first.units.Units.Meters;
import static edu.wpi.first.units.Units.Radians;

import edu.wpi.first.units.measure.Distance;
import frc.lib.module.Module;
import frc.lib.motor.MotorIO;

public class LinearPositionModule extends Module {
    public LinearPositionModule(String name, MotorIO io, double rotorToMechanismRatio) {
        super(name, io, rotorToMechanismRatio);
    }

    protected void setPositionSetpoint(Distance position) {
        io.setPositionSetpoint(Radians.of(position.in(Meters) * rotorToMechanismRatio));
    }

    public Distance getPosition() {
        return Meters.of(inputs.position.in(Radians) / rotorToMechanismRatio);
    }
}

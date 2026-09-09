package frc.lib.module;

import static edu.wpi.first.units.Units.Radians;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.units.measure.Angle;
import frc.lib.motor.MotorIO;

public class AngularPositionModule extends Module {
    public AngularPositionModule(String name, MotorIO io, double rotorToMechanismRatio) {
        super(name, io, rotorToMechanismRatio);
    }

    protected void setPositionSetpoint(Angle position) {
        io.setPositionSetpoint(position.times(rotorToMechanismRatio));
    }

    public Angle getPosition() {
        return inputs.position.div(rotorToMechanismRatio);
    }

    public Rotation2d getRotation() {
        return Rotation2d.fromRadians(getPosition().in(Radians));
    }
}

package frc.lib.units;

import org.wpilib.math.geometry.Rotation2d;

public class Angle extends Quantity<Angle, AngleUnit> {
    Angle(double value) {
        super(value);
    }

    public Rotation2d toRotation() {
        return Rotation2d.fromRadians(value);
    }

    public Angle plus(Angle other) {
        return new Angle(value + other.value);
    }

    public Angle minus(Angle other) {
        return new Angle(value - other.value);
    }

    public Angle times(double other) {
        return new Angle(value * other);
    }

    public Angle div(double other) {
        return new Angle(value / other);
    }

    public Angle negative() {
        return new Angle(-value);
    }
}

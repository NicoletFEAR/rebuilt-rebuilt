package frc.lib.units;

import org.wpilib.math.geometry.Rotation2d;

public class Angle extends Quantity<Angle, AngleUnit> {
    Angle(double value) {
        super(value, "rad");
    }

    public Rotation2d toRotation() {
        return Rotation2d.fromRadians(value);
    }

    public Angle max(Angle other) {
        if (value < other.value) {
            return other;
        } else {
            return this;
        }
    }

    public Angle min(Angle other) {
        if (value > other.value) {
            return other;
        } else {
            return this;
        }
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

    public Angle ceil() {
        return new Angle(Math.ceil(value));
    }

    public Angle floor() {
        return new Angle(Math.floor(value));
    }

    public AngularVelocity div(Time other) {
        return new AngularVelocity(value / other.value);
    }

    public Time div(AngularVelocity other) {
        return new Time(value / other.value);
    }

    public AngularAbsement times(Time other) {
        return new AngularAbsement(value * other.value);
    }
}

package frc.lib.units;

public class AngularAbsement extends Quantity<AngularAbsement, AngularAbsementUnit> {
    AngularAbsement(double value) {
        super(value, "rad");
    }

    public AngularAbsement max(AngularAbsement other) {
        if (value < other.value) {
            return other;
        } else {
            return this;
        }
    }

    public AngularAbsement min(AngularAbsement other) {
        if (value > other.value) {
            return other;
        } else {
            return this;
        }
    }

    public AngularAbsement plus(AngularAbsement other) {
        return new AngularAbsement(value + other.value);
    }

    public AngularAbsement minus(AngularAbsement other) {
        return new AngularAbsement(value - other.value);
    }

    public AngularAbsement times(double other) {
        return new AngularAbsement(value * other);
    }

    public AngularAbsement div(double other) {
        return new AngularAbsement(value / other);
    }

    public AngularAbsement negative() {
        return new AngularAbsement(-value);
    }

    public AngularAbsement ceil() {
        return new AngularAbsement(Math.ceil(value));
    }

    public AngularAbsement floor() {
        return new AngularAbsement(Math.floor(value));
    }

    public Angle div(Time other) {
        return new Angle(value / other.value);
    }

    public Time div(Angle other) {
        return new Time(value / other.value);
    }
}

package frc.lib.units;

public class MomentOfInertia extends Quantity<MomentOfInertia, MomentOfInertiaUnit> {
    MomentOfInertia(double value) {
        super(value, "kg⋅m²");
    }

    public MomentOfInertia max(MomentOfInertia other) {
        if (value < other.value) {
            return other;
        } else {
            return this;
        }
    }

    public MomentOfInertia min(MomentOfInertia other) {
        if (value > other.value) {
            return other;
        } else {
            return this;
        }
    }

    public MomentOfInertia clamp(MomentOfInertia low, MomentOfInertia high) {
        return max(low).min(high);
    }

    public MomentOfInertia plus(MomentOfInertia other) {
        return new MomentOfInertia(value + other.value);
    }

    public MomentOfInertia minus(MomentOfInertia other) {
        return new MomentOfInertia(value - other.value);
    }

    public MomentOfInertia times(double other) {
        return new MomentOfInertia(value * other);
    }

    public MomentOfInertia div(double other) {
        return new MomentOfInertia(value / other);
    }

    public MomentOfInertia negative() {
        return new MomentOfInertia(-value);
    }

    public MomentOfInertia ceil() {
        return new MomentOfInertia(Math.ceil(value));
    }

    public MomentOfInertia floor() {
        return new MomentOfInertia(Math.floor(value));
    }

    public Torque times(AngularAcceleration other) {
        return new Torque(value * other.value);
    }
}

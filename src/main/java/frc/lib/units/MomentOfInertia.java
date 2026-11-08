package frc.lib.units;

public class MomentOfInertia extends Quantity<MomentOfInertia, MomentOfInertiaUnit> {
    MomentOfInertia(double value) {
        super(value);
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
}

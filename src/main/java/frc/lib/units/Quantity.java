package frc.lib.units;

public abstract class Quantity<S extends Quantity<S, U>, U extends Unit> {
    protected double value;

    protected Quantity(double value) {
        this.value = value;
    }

    public boolean greaterThan(S other) {
        return value > other.value;
    }

    public boolean lessThan(S other) {
        return value < other.value;
    }

    public double in(U unit) {
        return value * unit.inBaseUnits;
    }
}

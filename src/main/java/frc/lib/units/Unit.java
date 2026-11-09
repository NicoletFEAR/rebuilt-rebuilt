package frc.lib.units;

public abstract class Unit<S extends Unit<S>> {
    protected double inBaseUnits;

    protected Unit(double inBaseUnits) {
        this.inBaseUnits = inBaseUnits;
    }

    public double from(double quantity, S other) {
        return quantity * other.inBaseUnits / inBaseUnits;
    }

    public double raw(double quantity) {
        return quantity;
    }
}

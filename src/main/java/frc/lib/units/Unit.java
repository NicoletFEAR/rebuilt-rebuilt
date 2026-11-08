package frc.lib.units;

public abstract class Unit {
    protected double inBaseUnits;

    protected Unit(double inBaseUnits) {
        this.inBaseUnits = inBaseUnits;
    }

    public double raw(double quantity) {
        return quantity;
    }
}

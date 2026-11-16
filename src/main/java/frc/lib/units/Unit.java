package frc.lib.units;

/**
 * Represents a standard, reference magnitude of a quantity.
 * 
 * @param <S> the unit's own type
 */
public abstract class Unit<S extends Unit<S>> {
    protected double inBaseUnits;

    protected Unit(double inBaseUnits) {
        this.inBaseUnits = inBaseUnits;
    }

    /**
     * Converts a raw double from its unit of measurement into this
     * unit of measurement.
     * 
     * @param quantity the value to be converted, in the other unit
     * of measurement
     * @param other the current unit of measurement of the quantity
     * @return the quantity, still as a raw double, but now in this
     * unit of measurement
     */
    public double from(double quantity, S other) {
        return quantity * other.inBaseUnits / inBaseUnits;
    }

    /**
     * Does nothing to a raw double, but can be used as an annotation
     * to indicate that this is its unit of measurement. Useful to
     * show the unit of measurement of a double literal without adding
     * a comment.
     */
    public double raw(double quantity) {
        return quantity;
    }
}

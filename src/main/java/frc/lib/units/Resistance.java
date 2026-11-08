package frc.lib.units;

public class Resistance extends Quantity<Resistance, ResistanceUnit> {
    Resistance(double value) {
        super(value);
    }

    public Resistance plus(Resistance other) {
        return new Resistance(value + other.value);
    }

    public Resistance minus(Resistance other) {
        return new Resistance(value - other.value);
    }

    public Resistance times(double other) {
        return new Resistance(value * other);
    }

    public Resistance div(double other) {
        return new Resistance(value / other);
    }

    public Resistance negative() {
        return new Resistance(-value);
    }
}

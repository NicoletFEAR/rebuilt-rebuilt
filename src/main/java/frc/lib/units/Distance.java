package frc.lib.units;

public class Distance extends Quantity<Distance, DistanceUnit> {
    Distance(double value) {
        super(value, "m");
    }

    public Distance plus(Distance other) {
        return new Distance(value + other.value);
    }

    public Distance minus(Distance other) {
        return new Distance(value - other.value);
    }

    public Distance times(double other) {
        return new Distance(value * other);
    }

    public Distance div(double other) {
        return new Distance(value / other);
    }

    public Distance negative() {
        return new Distance(-value);
    }
}

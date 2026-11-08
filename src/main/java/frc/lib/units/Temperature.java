package frc.lib.units;

public class Temperature extends Quantity<Temperature, TemperatureUnit> {
    Temperature(double value) {
        super(value);
    }

    public Temperature plus(Distance other) {
        return new Temperature(value + other.value);
    }

    public Temperature minus(Distance other) {
        return new Temperature(value - other.value);
    }

    public Temperature times(double other) {
        return new Temperature(value * other);
    }

    public Temperature div(double other) {
        return new Temperature(value / other);
    }

    public Temperature negative() {
        return new Temperature(-value);
    }
}

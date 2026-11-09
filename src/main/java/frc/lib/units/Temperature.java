package frc.lib.units;

public class Temperature extends Quantity<Temperature, TemperatureUnit> {
    Temperature(double value) {
        super(value, "K");
    }

    public Temperature max(Temperature other) {
        if (value < other.value) {
            return other;
        } else {
            return this;
        }
    }

    public Temperature min(Temperature other) {
        if (value > other.value) {
            return other;
        } else {
            return this;
        }
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

    public Temperature ceil() {
        return new Temperature(Math.ceil(value));
    }

    public Temperature floor() {
        return new Temperature(Math.floor(value));
    }
}

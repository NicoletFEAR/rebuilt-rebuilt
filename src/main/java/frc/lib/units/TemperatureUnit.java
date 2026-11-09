package frc.lib.units;

public class TemperatureUnit extends Unit<TemperatureUnit> {
    public TemperatureUnit(double inBaseUnits) {
        super(inBaseUnits);
    }

    public Temperature of(double quantity) {
        return new Temperature(quantity * inBaseUnits);
    }

    public static Temperature ofWpi(org.wpilib.units.measure.Temperature quantity) {
        return Kelvin.of(quantity.in(org.wpilib.units.Units.Kelvin));
    }

    public static final TemperatureUnit Kelvin = new TemperatureUnit(1.0);
}

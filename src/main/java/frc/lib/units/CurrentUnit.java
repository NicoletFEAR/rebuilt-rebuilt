package frc.lib.units;

public class CurrentUnit extends Unit {
    public CurrentUnit(double inBaseUnits) {
        super(inBaseUnits);
    }

    public Current of(double quantity) {
        return new Current(quantity * inBaseUnits);
    }

    public static Current ofWpi(org.wpilib.units.measure.Current quantity) {
        return Amps.of(quantity.in(org.wpilib.units.Units.Amps));
    }

    public static final CurrentUnit Amps = new CurrentUnit(1.0);
}

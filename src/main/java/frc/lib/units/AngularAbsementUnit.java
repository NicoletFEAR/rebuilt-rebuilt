package frc.lib.units;

public class AngularAbsementUnit extends Unit<AngularAbsementUnit> {
    public AngularAbsementUnit(double inBaseUnits) {
        super(inBaseUnits);
    }

    public AngularAbsement of(double quantity) {
        return new AngularAbsement(quantity * inBaseUnits);
    }

    public static final AngularAbsementUnit RadianSeconds = new AngularAbsementUnit(1.0);
}

package frc.lib.units;

public class ResistanceUnit extends Unit {
    public ResistanceUnit(double inBaseUnits) {
        super(inBaseUnits);
    }

    public Resistance of(double quantity) {
        return new Resistance(quantity * inBaseUnits);
    }

    public static Resistance ofWpi(org.wpilib.units.measure.Resistance quantity) {
        return Ohms.of(quantity.in(org.wpilib.units.Units.Ohms));
    }

    public static final ResistanceUnit Ohms = new ResistanceUnit(1.0);
}

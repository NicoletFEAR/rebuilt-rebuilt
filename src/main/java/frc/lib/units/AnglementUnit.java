package frc.lib.units;

public class AnglementUnit extends Unit<AnglementUnit> {
    public AnglementUnit(double inBaseUnits) {
        super(inBaseUnits);
    }

    public Anglement of(double quantity) {
        return new Anglement(quantity * inBaseUnits);
    }

    public static final AnglementUnit RadianSeconds = new AnglementUnit(1.0);
}

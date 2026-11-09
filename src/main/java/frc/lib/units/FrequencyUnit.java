package frc.lib.units;

public class FrequencyUnit extends Unit<FrequencyUnit> {
    public FrequencyUnit(double inBaseUnits) {
        super(inBaseUnits);
    }

    public Frequency of(double quantity) {
        return new Frequency(quantity * inBaseUnits);
    }

    public static Frequency ofWpi(org.wpilib.units.measure.Frequency quantity) {
        return Hertz.of(quantity.in(org.wpilib.units.Units.Hertz));
    }

    public static final FrequencyUnit Hertz = new FrequencyUnit(1.0);
}

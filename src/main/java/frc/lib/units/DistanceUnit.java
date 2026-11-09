package frc.lib.units;

public class DistanceUnit extends Unit<DistanceUnit> {
    public DistanceUnit(double inBaseUnits) {
        super(inBaseUnits);
    }

    public Distance of(double quantity) {
        return new Distance(quantity * inBaseUnits);
    }

    public static Distance ofWpi(org.wpilib.units.measure.Distance quantity) {
        return Meters.of(quantity.in(org.wpilib.units.Units.Meters));
    }

    public static final DistanceUnit Meters = new DistanceUnit(1.0);
    public static final DistanceUnit Inches = new DistanceUnit(0.0254);
}

package frc.lib.units;

public class MomentOfInertiaUnit extends Unit {
    public MomentOfInertiaUnit(double inBaseUnits) {
        super(inBaseUnits);
    }

    public MomentOfInertia of(double quantity) {
        return new MomentOfInertia(quantity * inBaseUnits);
    }

    public static MomentOfInertia ofWpi(org.wpilib.units.measure.MomentOfInertia quantity) {
        return KilogramSquareMeters.of(quantity.in(org.wpilib.units.Units.KilogramSquareMeters));
    }

    public static final MomentOfInertiaUnit KilogramSquareMeters = new MomentOfInertiaUnit(1.0);
}

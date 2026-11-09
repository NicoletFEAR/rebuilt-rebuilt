package frc.lib.units;

public class AngularVelocityUnit extends Unit<AngularVelocityUnit> {
    public AngularVelocityUnit(double inBaseUnits) {
        super(inBaseUnits);
    }

    public AngularVelocity of(double quantity) {
        return new AngularVelocity(quantity * inBaseUnits);
    }

    public static AngularVelocity ofWpi(org.wpilib.units.measure.AngularVelocity quantity) {
        return RadiansPerSecond.of(quantity.in(org.wpilib.units.Units.RadiansPerSecond));
    }

    public static final AngularVelocityUnit RadiansPerSecond = new AngularVelocityUnit(1.0);
    public static final AngularVelocityUnit RotationsPerSecond =
            new AngularVelocityUnit(Math.PI * 2.0);
    public static final AngularVelocityUnit RotationsPerMinute =
            new AngularVelocityUnit(Math.PI * 120.0);
}

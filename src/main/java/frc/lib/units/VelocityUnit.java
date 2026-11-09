package frc.lib.units;

public class VelocityUnit extends Unit<VelocityUnit> {
    public VelocityUnit(double inBaseUnits) {
        super(inBaseUnits);
    }

    public Velocity of(double quantity) {
        return new Velocity(quantity * inBaseUnits);
    }

    public static Velocity ofWpi(org.wpilib.units.measure.LinearVelocity quantity) {
        return MetersPerSecond.of(quantity.in(org.wpilib.units.Units.MetersPerSecond));
    }

    public static final VelocityUnit MetersPerSecond = new VelocityUnit(1.0);
}

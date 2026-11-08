package frc.lib.units;

public class AngularAccelerationUnit extends Unit {
    public AngularAccelerationUnit(double inBaseUnits) {
        super(inBaseUnits);
    }

    public AngularAcceleration of(double quantity) {
        return new AngularAcceleration(quantity * inBaseUnits);
    }

    public static AngularAcceleration ofWpi(org.wpilib.units.measure.AngularAcceleration quantity) {
        return RadiansPerSecondSquared.of(quantity.in(org.wpilib.units.Units.RadiansPerSecondPerSecond));
    }

    public static final AngularAccelerationUnit RadiansPerSecondSquared = new AngularAccelerationUnit(1.0);
    public static final AngularAccelerationUnit RotationsPerSecondSquared = new AngularAccelerationUnit(Math.PI * 2.0);
}

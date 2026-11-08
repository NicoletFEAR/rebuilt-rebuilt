package frc.lib.units;

import org.wpilib.math.geometry.Rotation2d;

public class AngleUnit extends Unit {
    public AngleUnit(double inBaseUnits) {
        super(inBaseUnits);
    }

    public Angle of(double quantity) {
        return new Angle(quantity * inBaseUnits);
    }

    public static Angle ofWpi(org.wpilib.units.measure.Angle quantity) {
        return Radians.of(quantity.in(org.wpilib.units.Units.Radians));
    }

    public static Angle ofRotation(Rotation2d rotation) {
        return Radians.of(rotation.getRadians());
    }

    public static final AngleUnit Radians = new AngleUnit(1.0);
    public static final AngleUnit Rotations = new AngleUnit(Math.PI * 2.0);
}

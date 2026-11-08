package frc.lib.units;

public class TorqueUnit extends Unit {
    public TorqueUnit(double inBaseUnits) {
        super(inBaseUnits);
    }

    public Torque of(double quantity) {
        return new Torque(quantity * inBaseUnits);
    }

    public static Torque ofWpi(org.wpilib.units.measure.Torque quantity) {
        return NewtonMeters.of(quantity.in(org.wpilib.units.Units.NewtonMeters));
    }

    public static final TorqueUnit NewtonMeters = new TorqueUnit(1.0);
}

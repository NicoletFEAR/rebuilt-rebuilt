package frc.lib.units;

public class VoltageUnit extends Unit<VoltageUnit> {
    public VoltageUnit(double inBaseUnits) {
        super(inBaseUnits);
    }

    public Voltage of(double quantity) {
        return new Voltage(quantity * inBaseUnits);
    }

    public static Voltage ofWpi(org.wpilib.units.measure.Voltage quantity) {
        return Volts.of(quantity.in(org.wpilib.units.Units.Volts));
    }

    public static final VoltageUnit Volts = new VoltageUnit(1.0);
}

package frc.lib.units;

public class TimeUnit extends Unit {
    public TimeUnit(double inBaseUnits) {
        super(inBaseUnits);
    }

    public Time of(double quantity) {
        return new Time(quantity * inBaseUnits);
    }

    public static Time ofWpi(org.wpilib.units.measure.Time quantity) {
        return Seconds.of(quantity.in(org.wpilib.units.Units.Seconds));
    }

    public static final TimeUnit Seconds = new TimeUnit(1.0);
}

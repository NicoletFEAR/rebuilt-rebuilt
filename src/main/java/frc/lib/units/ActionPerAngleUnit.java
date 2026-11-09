package frc.lib.units;

public class ActionPerAngleUnit extends Unit<ActionPerAngleUnit> {
    public ActionPerAngleUnit(double inBaseUnits) {
        super(inBaseUnits);
    }

    public ActionPerAngle of(double quantity) {
        return new ActionPerAngle(quantity * inBaseUnits);
    }

    public static final ActionPerAngleUnit JoulesPerHertzPerRadian = new ActionPerAngleUnit(1.0);
}

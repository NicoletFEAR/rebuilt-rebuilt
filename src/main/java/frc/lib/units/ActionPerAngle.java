package frc.lib.units;

public class ActionPerAngle extends Quantity<ActionPerAngle, ActionPerAngleUnit> {
    ActionPerAngle(double value) {
        super(value, "J/Hz/rad");
    }

    public ActionPerAngle plus(ActionPerAngle other) {
        return new ActionPerAngle(value + other.value);
    }

    public ActionPerAngle minus(ActionPerAngle other) {
        return new ActionPerAngle(value - other.value);
    }

    public ActionPerAngle times(double other) {
        return new ActionPerAngle(value * other);
    }

    public ActionPerAngle div(double other) {
        return new ActionPerAngle(value / other);
    }

    public ActionPerAngle negative() {
        return new ActionPerAngle(-value);
    }
}

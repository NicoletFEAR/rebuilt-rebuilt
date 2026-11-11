package frc.lib.units;

public class ActionPerAngle extends Quantity<ActionPerAngle, ActionPerAngleUnit> {
    ActionPerAngle(double value) {
        super(value, "J/Hz/rad");
    }

    public ActionPerAngle max(ActionPerAngle other) {
        if (value < other.value) {
            return other;
        } else {
            return this;
        }
    }

    public ActionPerAngle min(ActionPerAngle other) {
        if (value > other.value) {
            return other;
        } else {
            return this;
        }
    }

    public ActionPerAngle clamp(ActionPerAngle low, ActionPerAngle high) {
        return max(low).min(high);
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

    public ActionPerAngle ceil() {
        return new ActionPerAngle(Math.ceil(value));
    }

    public ActionPerAngle floor() {
        return new ActionPerAngle(Math.floor(value));
    }

    public Torque times(AngularVelocity other) {
        return new Torque(value * other.value);
    }
}

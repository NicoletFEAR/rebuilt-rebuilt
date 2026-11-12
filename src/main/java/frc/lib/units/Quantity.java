package frc.lib.units;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

public abstract class Quantity<S extends Quantity<S, U>, U extends Unit<U>>
        implements LoggableInputs {
    protected double value;
    protected String baseUnitSymbol;

    protected Quantity(double value, String baseUnitSymbol) {
        this.value = value;
        this.baseUnitSymbol = baseUnitSymbol;
    }

    public boolean greaterThan(S other) {
        return value > other.value;
    }

    public boolean lessThan(S other) {
        return value < other.value;
    }

    public boolean equal(S other, S tolerance) {
        return Math.abs(value - other.value) < tolerance.value;
    }

    public boolean notEqual(S other, S tolerance) {
        return Math.abs(value - other.value) > tolerance.value;
    }

    public double in(U unit) {
        return value / unit.inBaseUnits;
    }

    @Override
    public void toLog(LogTable table) {
        table.put("Value", value);
        table.put("Unit", baseUnitSymbol);
    }

    @Override
    public void fromLog(LogTable table) {
        value = table.get("Value", value);
        baseUnitSymbol = table.get("Unit", baseUnitSymbol);
    }
}

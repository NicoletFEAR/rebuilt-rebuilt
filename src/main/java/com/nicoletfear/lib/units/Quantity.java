package com.nicoletfear.lib.units;

import com.nicoletfear.lib.logging.LoggableOutputs;
import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.inputs.LoggableInputs;

/**
 * Represents a unique physical property that cannot be added to a different physical property, like
 * length, mass, or time.
 *
 * @param <S> the quantity's own type
 * @param <U> the quantity's associated unit type
 */
public abstract class Quantity<S extends Quantity<S, U>, U extends Unit<U>> implements LoggableInputs, LoggableOutputs {
    protected double value;
    protected String baseUnitSymbol;

    protected Quantity(double value, String baseUnitSymbol) {
        this.value = value;
        this.baseUnitSymbol = baseUnitSymbol;
    }

    /**
     * Whether this quantity's value is greater than another's.
     *
     * @param other the quantity being compared
     */
    public boolean greaterThan(S other) {
        return value > other.value;
    }

    /**
     * Whether this quantity's value is less than another's.
     *
     * @param other the quantity being compared
     */
    public boolean lessThan(S other) {
        return value < other.value;
    }

    /**
     * Whether this quantity's value is equal to another's. Because values are always doubles,
     * checking equality via {@code ==} would result in some equal values being considered unequal
     * because of floating-point error. So, instead, we check if the difference between the values is
     * less than a tolerance defined by the caller.
     *
     * @param other the quantity being compared
     * @param tolerance the minimum difference at which quantities are considered unequal
     */
    public boolean equal(S other, S tolerance) {
        return Math.abs(value - other.value) < tolerance.value;
    }

    /**
     * Whether this quantity's value is not equal to another's. Because values are always doubles,
     * checking inequality via {@code !=} could result in some unequal values being considered equal
     * because of floating-point error. So, instead, we check if the difference between the values is
     * greater than a tolerance defined by the caller.
     *
     * @param other the quantity being compared
     * @param tolerance the maximum difference at which quantities are considered equal
     */
    public boolean notEqual(S other, S tolerance) {
        return Math.abs(value - other.value) > tolerance.value;
    }

    /**
     * Converts this quantity from a quantity into a raw double, representing the quantity's value in
     * some unit of measurement.
     *
     * @param unit the unit of measurement of the resulting double
     */
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

    @Override
    public void log(String key) {
        Logger.recordOutput(key + "/Value", value);
        Logger.recordOutput(key + "/Unit", baseUnitSymbol);
    }
}

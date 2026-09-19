package frc.lib.module.position;

import static edu.wpi.first.units.Units.Meters;
import static edu.wpi.first.units.Units.Radians;

import edu.wpi.first.units.measure.Distance;
import frc.lib.module.Module;
import frc.lib.motor.MotorIO;

/**
 * A module that controls and measures the linear position of a mechanism.
 *
 * <p>This module extends {@link Module} and provides functionality for setting and retrieving the
 * mechanism's linear position. Motor rotor position is converted to mechanism position using the
 * configured rotor-to-mechanism ratio.
 */
public class LinearPositionModule extends Module {

    /**
     * Creates a linear position module.
     *
     * @param name name used to identify and log this module
     * @param io interface used to communicate with the motor
     * @param rotorToMechanismRatio ratio between the motor rotor and the mechanism
     */
    public LinearPositionModule(String name, MotorIO io, double rotorToMechanismRatio) {
        super(name, io, rotorToMechanismRatio);
    }

    /**
     * Sets the desired linear position of the mechanism.
     *
     * <p>The requested mechanism position is converted from meters to rotor position using the
     * configured rotor-to-mechanism ratio before being sent to the motor.
     *
     * @param position desired mechanism position
     */
    protected void setPositionSetpoint(Distance position) {
        io.setPositionSetpoint(Radians.of(position.in(Meters) * rotorToMechanismRatio));
    }

    /**
     * Gets the current linear position of the mechanism.
     *
     * <p>The motor's rotor position is converted to meters using the configured rotor-to-mechanism
     * ratio.
     *
     * @return current mechanism position as a {@link Distance}
     */
    public Distance getPosition() {
        return Meters.of(inputs.position.in(Radians) / rotorToMechanismRatio);
    }
}

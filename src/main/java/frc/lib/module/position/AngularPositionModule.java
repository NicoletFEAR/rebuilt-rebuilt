package frc.lib.module.position;

import static edu.wpi.first.units.Units.Radians;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.units.measure.Angle;
import frc.lib.module.Module;
import frc.lib.motor.MotorIO;

/**
 * A module that controls and measures the angular position of a mechanism.
 *
 * <p>This module extends {@link Module} and provides functionality for setting and retrieving the
 * mechanism's angular position. Motor rotor position is converted to mechanism position using the
 * configured rotor-to-mechanism ratio.
 */
public class AngularPositionModule extends Module {

    /**
     * Creates an angular position module.
     *
     * @param name name used to identify and log this module
     * @param io interface used to communicate with the motor
     * @param rotorToMechanismRatio ratio between the motor rotor and the mechanism
     */
    public AngularPositionModule(String name, MotorIO io, double rotorToMechanismRatio) {
        super(name, io, rotorToMechanismRatio);
    }

    /**
     * * Sets the desired angular position of the mechanism.
     *
     * <p>The requested mechanism position is converted to rotor position using the configured
     * rotor-to-mechanism ratio before being sent to the motor.
     *
     * @param position desired mechanism position
     */
    protected void setPositionSetpoint(Angle position) {
        io.setPositionSetpoint(position.times(rotorToMechanismRatio));
    }

    /**
     * Gets the current angular position of the mechanism.
     *
     * <p>The motor's rotor position is converted to mechanism position using the configured
     * rotor-to-mechanism ratio.
     *
     * @return current mechanism positionas an {@link Angle}
     */
    public Angle getPosition() {
        return inputs.position.div(rotorToMechanismRatio);
    }

    /**
     * Gets the current angular position of the mechanism as a {@link Rotation2d}.
     *
     * @return current mechanism rotation
     */
    public Rotation2d getRotation() {
        return Rotation2d.fromRadians(getPosition().in(Radians));
    }
}

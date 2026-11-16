package frc.lib.module;

import frc.lib.motor.MotorIO;
import frc.lib.motor.MotorIOInputsAutoLogged;
import frc.lib.units.Voltage;
import org.littletonrobotics.junction.Logger;

/**
 * Base class for modules that control a motor and interact with a mechanism.
 *
 * <p>This class provides common functionality for motor control, input updates, and converting
 * motor rotor measurements into mechanism measurements.
 *
 * <p>Specific module types, such as position and velocity modules, extend this class to add
 * functionality for controlling a particular type of mechanism.
 */
public abstract class Module {

    /** Interface used to communicate with the motor. */
    protected final MotorIO io;

    /** Logged inputs containing the current state of the motor. */
    protected final MotorIOInputsAutoLogged inputs = new MotorIOInputsAutoLogged();

    /** Name used to identify and log this module. */
    protected final String name;

    /** Ratio between the motor rotor and the mechanism. */
    protected final double rotorToMechanismRatio;

    /**
     * Creates a module using the provided motor and configuration.
     *
     * @param name name used to identify and log this module
     * @param io interface used to communicate with the motor
     * @param rotorToMechanismRatio ratio between the motor rotor and the mechanism
     */
    public Module(String name, MotorIO io, double rotorToMechanismRatio) {
        this.name = name;
        this.io = io;
        this.rotorToMechanismRatio = rotorToMechanismRatio;
    }

    /**
     * Updates the motor inputs and logs the current state of the module.
     *
     * <p>The motor inputs are updated through the {@link MotorIO} interface and then sent to the
     * AdvantageKit logger. The logged inputs are rotor measurements; they are not converted to
     * mechanism measurements here. Use {@code getPosition()}/{@code getVelocity()} on the concrete
     * module to obtain mechanism-relative values.
     */
    public void updateInputs() {
        io.updateInputs(inputs);
        Logger.processInputs(name, inputs);
    }

    /**
     * Sets the voltage output of the motor.
     *
     * @param voltage voltage to apply to the motor
     */
    public void setVoltage(Voltage voltage) {
        io.setVoltage(voltage);
    }
}

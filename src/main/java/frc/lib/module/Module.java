package frc.lib.module;

import frc.lib.motor.MotorIO;
import frc.lib.motor.MotorIOInputsAutoLogged;
import frc.lib.units.Voltage;
import org.littletonrobotics.junction.Logger;

/**
 * Base class for modules that control a motor and interact with a mechanism.
 *
 * <p>This class provides common functionality for motor control and input updates.
 *
 * <p>Specific module types, such as position and velocity modules, extend this class to add
 * functionality for controlling a particular type of mechanism.
 */
public abstract class Module {

    /** Interface used to communicate with the motor. */
    protected final MotorIO motor;

    /** Logged inputs containing the current state of the motor. */
    protected final MotorIOInputsAutoLogged inputs = new MotorIOInputsAutoLogged();

    /** Name used to identify and log this module. */
    protected final String name;

    /**
     * Creates a module using the provided motor and configuration.
     *
     * @param name name used to identify and log this module
     * @param motor interface used to communicate with the motor
     */
    public Module(String name, MotorIO motor) {
        this.name = name;
        this.motor = motor;
    }

    /**
     * Updates the motor inputs and logs the current state of the module.
     *
     * <p>The motor inputs are updated through the {@link MotorIO} interface and then sent to the
     * AdvantageKit logger.
     */
    public void updateInputs() {
        motor.updateInputs(inputs);
        Logger.processInputs(name, inputs);
    }

    /**
     * Sets the voltage output of the motor.
     *
     * @param voltage voltage to apply to the motor
     */
    public void setVoltage(Voltage voltage) {
        motor.setVoltage(voltage);
    }
}

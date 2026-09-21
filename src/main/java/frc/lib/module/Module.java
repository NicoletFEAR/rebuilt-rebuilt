package frc.lib.module;

import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.motor.MotorIO;
import frc.lib.motor.MotorIOInputsAutoLogged;
import org.littletonrobotics.junction.Logger;

/**
 * Base class for modules that control a motor and interact with a mechanism.
 *
 * <p>This class extends WPILib's {@link SubsystemBase} and provides common functionality for motor
 * control, input updates, logging, and converting motor rotor measurements into mechanism
 * measurements.
 *
 * <p>Specific module types, such as position and velocity modules, can extend this class to add
 * functionality for controlling a particular type of mechanism.
 */
public abstract class Module extends SubsystemBase {

    /** Interface used to communicate with the motor. */
    protected final MotorIO io;

    /** Logged inputs containing the current state of the motor. */
    protected final MotorIOInputsAutoLogged inputs;

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
        super(name);
        this.name = getName();
        this.io = io;
        inputs = new MotorIOInputsAutoLogged();
        this.rotorToMechanismRatio = rotorToMechanismRatio;
    }

    /**
     * Updates the motor inputs and logs the current state of the module.
     *
     * <p>The motor inputs are first updated through the {@link MotorIO} interface. The inputs are
     * then converted from rotor measurements to mechanism measurements using the configured
     * rotor-to-mechanism ratio before being sent to the AdvantageKit logger.
     */
    @Override
    public void periodic() {
        io.updateInputs(inputs);
        Logger.processInputs(name, inputs.applyRotorToMechanismRatio(rotorToMechanismRatio));
    }

    /**
     * Sets the neutral mode of the motor.
     *
     * <p>The neutral mode determines how the motor behaves when no output is being applied.
     *
     * @param mode neutral mode to apply to the motor
     */
    protected void setNeutralMode(NeutralModeValue mode) {
        io.setNeutralMode(mode);
    }

    /**
     * Sets the voltage output of the motor.
     *
     * @param voltage voltage to apply to the motor
     */
    protected void setVoltage(Voltage voltage) {
        io.setVoltage(voltage);
    }
}

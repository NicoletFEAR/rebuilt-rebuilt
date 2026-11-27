package com.nicoletfear.lib.motor;

import static org.wpilib.units.Units.Volts;

import com.ctre.phoenix6.sim.TalonFXSimState;
import com.nicoletfear.lib.units.AngleUnit;
import com.nicoletfear.lib.units.AngularVelocityUnit;
import com.nicoletfear.lib.units.CurrentUnit;
import com.nicoletfear.lib.units.TemperatureUnit;
import com.nicoletfear.lib.units.VoltageUnit;
import org.wpilib.math.system.DCMotor;
import org.wpilib.math.system.Models;
import org.wpilib.simulation.DCMotorSim;
import org.wpilib.system.RobotController;
import org.wpilib.system.Timer;

/**
 * Simulation implementation of {@link MotorIO} that models a TalonFX motor using WPILib's {@link
 * DCMotorSim}.
 *
 * <p>This class extends {@link TalonFxMotor} so that the simulated motor uses the same TalonFX
 * interface and control methods as a real motor. The motor's physical behavior is simulated using
 * the Kraken X60's {@link DCMotor} model.
 *
 * <p>The simulation uses the configured feedforward values and the robot's current battery voltage
 * to calculate the motor's simulated position, velocity, and acceleration.
 */
public final class SimTalonFxMotor extends TalonFxMotor {

    private final DCMotorSim motorModel;
    private final TalonFXSimState motorSim;
    private double lastUpdateTimestamp = 0.0;

    /**
     * Creates a simulated TalonFX motor using the provided configuration, in the same way a real
     * motor would be created.
     *
     * @param config configuration for the motor
     */
    public SimTalonFxMotor(MotorConfig config) {
        super(config);
        FeedforwardValues feedforwardValues = config.feedforwardValues();
        motorModel = new DCMotorSim(
                Models.singleJointedArmFromSysId(feedforwardValues.v(), feedforwardValues.a()),
                DCMotor.getKrakenX60(1));
        motorSim = motor.getSimState();
    }

    /**
     * * Updates the simulated motor and the provided motor inputs.
     *
     * <p>The simulation uses the robot battery voltage and the voltage being commanded by the TalonFX
     * to update the motor's position and velocity.The resulting simulated values are then written
     * back to the TalonFX simulation state before the motor inputs are updated.
     *
     * @param inputs object to populate with the motor's current simulated inputs
     */
    @Override
    public void updateInputs(MotorIOInputs inputs) {
        motorSim.setSupplyVoltage(RobotController.getBatteryVoltage());
        motorModel.setInputVoltage(motorSim.getMotorVoltageMeasure().in(Volts));

        double timestamp = Timer.getTimestamp();
        motorModel.update(timestamp - lastUpdateTimestamp);
        lastUpdateTimestamp = timestamp;

        motorSim.setRawRotorPosition(motorModel.getAngularPosition());
        motorSim.setRotorVelocity(motorModel.getAngularVelocity());
        motorSim.setRotorAcceleration(motorModel.getAngularAcceleration());

        signals.refreshAll();
        inputs.connected = signals.isAllGood();
        inputs.position = AngleUnit.ofWpi(positionSignal.getValue());
        inputs.velocity = AngularVelocityUnit.ofWpi(velocitySignal.getValue());
        inputs.voltage = VoltageUnit.ofWpi(voltageSignal.getValue());
        inputs.statorCurrent = CurrentUnit.ofWpi(statorCurrentSignal.getValue());
        inputs.supplyCurrent = CurrentUnit.ofWpi(supplyCurrentSignal.getValue());
        inputs.temperature = TemperatureUnit.ofWpi(temperatureSignal.getValue());
    }
}

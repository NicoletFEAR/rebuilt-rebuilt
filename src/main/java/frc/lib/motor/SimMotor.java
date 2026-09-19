package frc.lib.motor;

import static edu.wpi.first.units.Units.KilogramSquareMeters;
import static edu.wpi.first.units.Units.Volts;

import com.ctre.phoenix6.sim.TalonFXSimState;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;

/**
 * Simulation implementation of {@link MotorIO} that models a TalonFX motor
 * using WPILib's {@link DCMotorSim}.
 * 
 * <p>
 * This class extends {@link RealMotor} so that the simulated motor uses the
 * same TalonFX interface and control methods as a real motor. The motor's
 * physical behavior is simulated using the {@link DCMotor} model associated
 * with the configured {@link KrakenType}.
 * 
 * <p>
 * The simulation uses the configured moment of inertia and the robot's
 * current battery voltage to calculate the motor's simulated position and
 * velocity.
 */
public class SimMotor extends RealMotor {

    /** WPILib simulation model for the motor. */
    private final DCMotorSim simMotor;
    private final TalonFXSimState motorSim;

    /** FPGA timestamp from the previous simulation update. */
    private double lastUpdateTimestamp = 0.0;

    /**
     * Creates a simulated motor using the provided motor configuration.
     * 
     * <p>
     * The motor model and moment of inertia are obtained from the
     * {@link MotorConfig}. The motor is simulated as a single motor.
     * 
     * @param config configuration for the simulated motor
     */
    public SimMotor(MotorConfig config) {
        super(config);

        DCMotor dcMotor = config.type().getDcMotor();
        simMotor =
                new DCMotorSim(
                        LinearSystemId.createDCMotorSystem(
                                dcMotor, config.momentOfInertia().in(KilogramSquareMeters), 1.0),
                        dcMotor);
        motorSim = motor.getSimState();
    }

    /**
     * * Updates the simulated motor and the provided motor inputs.
     * 
     * <p>
     * The simulation uses the robot battery voltage and the voltage being
     * commanded by the TalonFX to update the motor's position and velocity.The
     * resulting simulated values are then written back to the TalonFX simulation
     * state before the motor inputs are updated.
     * 
     * @param inputs object to populate with the motor's current simulated inputs
     */
    @Override
    public void updateInputs(MotorIOInputs inputs) {
        motorSim.setSupplyVoltage(RobotController.getBatteryVoltage());
        simMotor.setInputVoltage(motorSim.getMotorVoltageMeasure().in(Volts));

        double timestamp = Timer.getFPGATimestamp();
        simMotor.update(timestamp - lastUpdateTimestamp);
        lastUpdateTimestamp = timestamp;

        motorSim.setRawRotorPosition(simMotor.getAngularPosition());
        motorSim.setRotorVelocity(simMotor.getAngularVelocity());

        signals.refreshAll();
        inputs.connected = signals.isAllGood();
        inputs.position = positionSignal.getValue();
        inputs.velocity = velocitySignal.getValue();
        inputs.voltage = voltageSignal.getValue();
        inputs.statorCurrent = statorCurrentSignal.getValue();
        inputs.supplyCurrent = supplyCurrentSignal.getValue();
        inputs.temperature = temperatureSignal.getValue();
    }
}

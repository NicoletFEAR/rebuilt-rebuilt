package frc.lib.motor;

import com.ctre.phoenix6.signals.MotorAlignmentValue;
import org.wpilib.math.system.DCMotor;
import org.wpilib.simulation.DCMotorSim;
import org.wpilib.system.Timer;
import org.wpilib.units.measure.Angle;
import org.wpilib.units.measure.AngularVelocity;
import org.wpilib.units.measure.Current;
import org.wpilib.units.measure.Temperature;
import org.wpilib.units.measure.Voltage;

/**
 * Simulation implementation of {@link MotorIO} that models a TalonFX motor using WPILib's {@link
 * DCMotorSim}.
 *
 * <p>This class extends {@link TalonFXMotor} so that the simulated motor uses the same TalonFX
 * interface and control methods as a real motor. The motor's physical behavior is simulated using
 * the {@link DCMotor} model associated with the configured {@link KrakenType}.
 *
 * <p>The simulation uses the configured moment of inertia and the robot's current battery voltage
 * to calculate the motor's simulated position and velocity.
 */
public class SimMotor extends MotorIO {

    private Control control;

    private Angle position;
    private AngularVelocity velocity;
    private Voltage voltage;
    private Current statorCurrent;
    private Current supplyCurrent;
    private Temperature temperature;

    /** FPGA timestamp from the previous simulation update. */
    private double lastUpdateTimestamp = 0.0;

    /**
     * Creates a simulated motor using the provided motor configuration.
     *
     * <p>The motor model and moment of inertia are obtained from the {@link MotorConfig}. The motor
     * is simulated as a single motor.
     *
     * @param config configuration for the simulated motor
     */
    public SimMotor(MotorConfig config) {
        super(config);
    }

    private sealed interface Control
            permits FollowerControl, PositionControl, VelocityControl, VoltageControl {}

    private record FollowerControl(MotorIO leader, MotorAlignmentValue alignment)
            implements Control {}

    private record PositionControl(Angle desiredPosition) implements Control {}

    private record VelocityControl(AngularVelocity desiredVelocity) implements Control {}

    private record VoltageControl(Voltage voltage) implements Control {}

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
        double timestamp = Timer.getTimestamp();

        // switch (control) {
        //     case FollowerControl(MotorIO leader, MotorAlignmentValue alignment) -> {
        //         this.position = leader.
        //     }
        // }

        lastUpdateTimestamp = timestamp;

        inputs.connected = true;
        inputs.position = position;
        inputs.velocity = velocity;
        inputs.voltage = voltage;
        inputs.statorCurrent = statorCurrent;
        inputs.supplyCurrent = supplyCurrent;
        inputs.temperature = temperature;
    }

    @Override
    public void follow(MotorIO leader, MotorAlignmentValue alignment) {
        control = new FollowerControl(leader, alignment);
    }

    @Override
    public void setPositionSetpoint(Angle position) {
        control = new PositionControl(position);
    }

    @Override
    public void setVelocitySetpoint(AngularVelocity velocity) {
        control = new VelocityControl(velocity);
    }

    @Override
    public void setVoltage(Voltage voltage) {
        control = new VoltageControl(voltage);
    }
}

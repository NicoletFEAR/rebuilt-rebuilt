package frc.lib.motor;

import static frc.lib.units.AngleUnit.Radians;
import static frc.lib.units.AngularAccelerationUnit.RadiansPerSecondSquared;
import static frc.lib.units.AngularVelocityUnit.RadiansPerSecond;
import static frc.lib.units.CurrentUnit.Amps;
import static frc.lib.units.TemperatureUnit.Kelvin;
import static frc.lib.units.TimeUnit.Seconds;
import static frc.lib.units.VoltageUnit.Volts;

import com.ctre.phoenix6.signals.MotorAlignmentValue;
import frc.lib.units.Angle;
import frc.lib.units.AngularAcceleration;
import frc.lib.units.AngularVelocity;
import frc.lib.units.Current;
import frc.lib.units.Temperature;
import frc.lib.units.Time;
import frc.lib.units.Timestamp;
import frc.lib.units.Voltage;
import org.wpilib.math.system.DCMotor;
import org.wpilib.simulation.DCMotorSim;
import org.wpilib.system.Timer;

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

    private Control control = new VoltageControl(Volts.of(0.0));
    private MotorIOInputsAutoLogged leaderInputs = new MotorIOInputsAutoLogged();

    private Angle position = Radians.of(0.0);
    private AngularVelocity velocity = RadiansPerSecond.of(0.0);
    private AngularAcceleration acceleration = RadiansPerSecondSquared.of(0.0);
    private Voltage voltage = Volts.of(0.0);
    private Current statorCurrent = Amps.of(0.0);
    private Current supplyCurrent = Amps.of(0.0);
    private Temperature temperature = Kelvin.of(294.0);

    /** FPGA timestamp from the previous simulation update. */
    private Timestamp lastUpdateTimestamp = Seconds.timestamp(0.0);

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

    private record VoltageControl(Voltage desiredVoltage) implements Control {}

    /**
     * * Updates the simulated motor and the provided motor inputs.
     *
     * <p>The simulation uses the robot battery voltage and the voltage being commanded by the TalonFX
     * to update the motor's position and velocity. The resulting simulated values are then written
     * back to the TalonFX simulation state before the motor inputs are updated.
     *
     * @param inputs object to populate with the motor's current simulated inputs
     */
    @Override
    public void updateInputs(MotorIOInputs inputs) {
        Timestamp timestamp = Seconds.timestamp(Timer.getTimestamp());
        Time passedTime = timestamp.since(lastUpdateTimestamp);

        switch (control) {
            case FollowerControl(MotorIO leader, MotorAlignmentValue alignment) -> {
                leader.updateInputs(leaderInputs);
                double alignmentModifier = alignment == MotorAlignmentValue.Aligned ? 1.0 : -1.0;
                position = leaderInputs.position.times(alignmentModifier);
                velocity = leaderInputs.velocity.times(alignmentModifier);
                acceleration = leaderInputs.acceleration.times(alignmentModifier);
                voltage = leaderInputs.voltage.times(alignmentModifier);
                statorCurrent = leaderInputs.statorCurrent;
                supplyCurrent = leaderInputs.supplyCurrent;
                temperature = leaderInputs.temperature;
            }

            case VoltageControl(Voltage desiredVoltage) -> {
                voltage = desiredVoltage.min(MotorConstants.MAX_VOLTAGE);
                acceleration =
                        MotorConstants.TORQUE_CONSTANT
                                .times(
                                        voltage
                                                .minus(MotorConstants.BACK_EMF_CONSTANT.times(velocity))
                                                .div(MotorConstants.WINDING_RESISTANCE))
                                .minus(MotorConstants.VISCOUS_FRICTION.times(velocity))
                                .minus(MotorConstants.LOAD_TORQUE)
                                .div(MotorConstants.MOMENT_OF_INERTIA);
                velocity = velocity.plus(acceleration.times(passedTime));
                position = position.plus(velocity.times(passedTime));
            }

            case VelocityControl(AngularVelocity desiredVelocity) -> {
                AngularAcceleration desiredAcceleration = desiredVelocity.minus(velocity).div(passedTime);
                voltage =
                        desiredAcceleration
                                .times(MotorConstants.MOMENT_OF_INERTIA)
                                .plus(MotorConstants.VISCOUS_FRICTION.times(velocity))
                                .plus(MotorConstants.LOAD_TORQUE)
                                .div(MotorConstants.TORQUE_CONSTANT)
                                .times(MotorConstants.WINDING_RESISTANCE)
                                .plus(MotorConstants.BACK_EMF_CONSTANT.times(velocity))
                                .min(MotorConstants.MAX_VOLTAGE);
                acceleration =
                        MotorConstants.TORQUE_CONSTANT
                                .times(
                                        voltage
                                                .minus(MotorConstants.BACK_EMF_CONSTANT.times(velocity))
                                                .div(MotorConstants.WINDING_RESISTANCE))
                                .minus(MotorConstants.VISCOUS_FRICTION.times(velocity))
                                .minus(MotorConstants.LOAD_TORQUE)
                                .div(MotorConstants.MOMENT_OF_INERTIA);
                velocity = velocity.plus(acceleration.times(passedTime));
                position = position.plus(velocity.times(passedTime));
            }

            case PositionControl(Angle desiredPosition) -> {
                position = desiredPosition;
            }
        }

        lastUpdateTimestamp = timestamp;

        inputs.connected = true;
        inputs.position = position;
        inputs.velocity = velocity;
        inputs.acceleration = acceleration;
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

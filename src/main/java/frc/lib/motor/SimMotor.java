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
import org.wpilib.system.Timer;

/**
 * Simulation implementation of {@link MotorIO}.
 *
 * <p>The motor's electrical and mechanical behavior is modeled using the physical constants defined
 * in {@link MotorConstants} (torque and back-EMF constants, winding resistance, viscous friction,
 * load torque, and rotor moment of inertia).
 *
 * <p>The simulation integrates the motor's acceleration and velocity each time {@link
 * #updateInputs(MotorIOInputs)} is called, using the voltage commanded by whichever control mode is
 * active (voltage, velocity, position, or follower).
 */
public class SimMotor extends MotorIO {

    private Control control = new VoltageControl(Volts.of(0.0));

    /** Reused inputs object for reading the leader's simulated state in follower mode. */
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
     * <p>The simulation uses the physical constants in {@link MotorConstants} for its motor model.
     *
     * @param config configuration for the simulated motor
     */
    public SimMotor(MotorConfig config) {
        super(config);
    }

    /** The control mode currently commanded to the simulated motor. */
    private sealed interface Control
            permits FollowerControl, PositionControl, VelocityControl, VoltageControl {}

    /** Follow another motor, mirroring its inputs (scaled by {@code alignment}). */
    private record FollowerControl(MotorIO leader, MotorAlignmentValue alignment)
            implements Control {}

    /**
     * Command the simulated position to a desired position (the simulation sets position directly,
     * without modeling dynamics).
     */
    private record PositionControl(Angle desiredPosition) implements Control {}

    /** Command a desired angular velocity, computing the required voltage from the model. */
    private record VelocityControl(AngularVelocity desiredVelocity) implements Control {}

    /** Command a desired voltage directly. */
    private record VoltageControl(Voltage desiredVoltage) implements Control {}

    /**
     * Updates the simulated motor and the provided motor inputs.
     *
     * <p>The simulation integrates the motor model from the timestamp of the previous call to now,
     * using whichever control mode is currently active (follower, voltage, velocity, or position),
     * and then writes the resulting simulated values into the provided inputs. The very first call
     * after construction uses an elapsed time computed from a zero timestamp.
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

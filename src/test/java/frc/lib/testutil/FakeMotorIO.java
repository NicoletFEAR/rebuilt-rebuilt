package frc.lib.testutil;

import static frc.lib.units.AngleUnit.Radians;
import static frc.lib.units.AngularVelocityUnit.RadiansPerSecond;
import static frc.lib.units.VoltageUnit.Volts;

import com.ctre.phoenix6.signals.MotorAlignmentValue;
import frc.lib.motor.MotorConfig;
import frc.lib.motor.MotorIO;
import frc.lib.units.Angle;
import frc.lib.units.AngularVelocity;
import frc.lib.units.Voltage;
import java.util.function.Consumer;

/**
 * A {@link MotorIO} implementation that records every command instead of touching hardware.
 *
 * <p>Tests can set {@link #inputUpdater} to populate the inputs object passed to {@link
 * #updateInputs(MotorIOInputs)}.
 */
public class FakeMotorIO extends MotorIO {
    /** Populates the inputs object during {@link #updateInputs(MotorIOInputs)}. */
    public Consumer<MotorIOInputs> inputUpdater = inputs -> {};

    /** Number of times {@link #updateInputs(MotorIOInputs)} has been called. */
    public int updateInputsCalls;

    /** Inputs object most recently passed to {@link #updateInputs(MotorIOInputs)}. */
    public MotorIOInputs lastInputs;

    /** Leader passed to {@link #follow(MotorIO, MotorAlignmentValue)}. */
    public MotorIO followLeader;

    /** Alignment passed to {@link #follow(MotorIO, MotorAlignmentValue)}. */
    public MotorAlignmentValue followAlignment;

    /** Number of times {@link #follow(MotorIO, MotorAlignmentValue)} has been called. */
    public int followCalls;

    /** Position passed to {@link #setPositionSetpoint(Angle)}. */
    public Angle positionSetpoint = Radians.of(0.0);

    /** Number of times {@link #setPositionSetpoint(Angle)} has been called. */
    public int positionSetpointCalls;

    /** Position passed to {@link #setDynamicPosition(Angle)}. */
    public Angle dynamicPosition = Radians.of(0.0);

    /** Number of times {@link #setDynamicPosition(Angle)} has been called. */
    public int dynamicPositionCalls;

    /** Velocity passed to {@link #setVelocitySetpoint(AngularVelocity)}. */
    public AngularVelocity velocitySetpoint = RadiansPerSecond.of(0.0);

    /** Number of times {@link #setVelocitySetpoint(AngularVelocity)} has been called. */
    public int velocitySetpointCalls;

    /** Velocity passed to {@link #setDynamicVelocity(AngularVelocity)}. */
    public AngularVelocity dynamicVelocity = RadiansPerSecond.of(0.0);

    /** Number of times {@link #setDynamicVelocity(AngularVelocity)} has been called. */
    public int dynamicVelocityCalls;

    /** Voltage passed to {@link #setVoltage(Voltage)}. */
    public Voltage voltage = Volts.of(0.0);

    /** Number of times {@link #setVoltage(Voltage)} has been called. */
    public int voltageCalls;

    public FakeMotorIO(MotorConfig config) {
        super(config);
    }

    @Override
    public void updateInputs(MotorIOInputs inputs) {
        updateInputsCalls++;
        lastInputs = inputs;
        inputUpdater.accept(inputs);
    }

    @Override
    public void follow(MotorIO leader, MotorAlignmentValue alignment) {
        followCalls++;
        followLeader = leader;
        followAlignment = alignment;
    }

    @Override
    public void setPositionSetpoint(Angle position) {
        positionSetpointCalls++;
        positionSetpoint = position;
    }

    @Override
    public void setDynamicPosition(Angle position) {
        dynamicPositionCalls++;
        dynamicPosition = position;
    }

    @Override
    public void setVelocitySetpoint(AngularVelocity velocity) {
        velocitySetpointCalls++;
        velocitySetpoint = velocity;
    }

    @Override
    public void setDynamicVelocity(AngularVelocity velocity) {
        dynamicVelocityCalls++;
        dynamicVelocity = velocity;
    }

    @Override
    public void setVoltage(Voltage voltage) {
        voltageCalls++;
        this.voltage = voltage;
    }
}

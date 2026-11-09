package frc.lib.motor;

import static frc.lib.units.AngleUnit.Radians;
import static frc.lib.units.AngleUnit.Rotations;
import static frc.lib.units.AngularVelocityUnit.RadiansPerSecond;
import static frc.lib.units.AngularVelocityUnit.RotationsPerSecond;
import static frc.lib.units.FrequencyUnit.Hertz;
import static frc.lib.units.VoltageUnit.Volts;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.StatusSignalCollection;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.MotionMagicVelocityVoltage;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import frc.lib.units.Angle;
import frc.lib.units.AngleUnit;
import frc.lib.units.AngularAccelerationUnit;
import frc.lib.units.AngularVelocity;
import frc.lib.units.AngularVelocityUnit;
import frc.lib.units.CurrentUnit;
import frc.lib.units.TemperatureUnit;
import frc.lib.units.Voltage;
import frc.lib.units.VoltageUnit;
import org.wpilib.units.measure.AngularAcceleration;
import org.wpilib.units.measure.Current;
import org.wpilib.units.measure.Temperature;

/**
 * Real hardware implementation of {@link MotorIO} for a CTRE TalonFX motor controller.
 *
 * <p>This class provides an interface between the library's motor abstraction and a physical
 * TalonFX. It handles retrieving motor status information, configuring follower motors, and
 * controlling the motor using position, velocity, and voltage control modes.
 *
 * <p>Motor status signals are updated at 100 Hz and are optimized for reduced CAN bus utilization.
 */
public class TalonFXMotor extends MotorIO {

    /** Physical TalonFX motor controller. */
    private final TalonFX motor;

    /** Collection of status signals used to retrieve motor inputs. */
    private final StatusSignalCollection signals = new StatusSignalCollection(6);

    /** Rotor position status signal. */
    private final StatusSignal<org.wpilib.units.measure.Angle> positionSignal;

    /** Rotor angular velocity status signal. */
    private final StatusSignal<org.wpilib.units.measure.AngularVelocity> velocitySignal;

    /** Rotor angular acceleration status signal. */
    private final StatusSignal<AngularAcceleration> accelerationSignal;

    /** Motor voltage status signal. */
    private final StatusSignal<org.wpilib.units.measure.Voltage> voltageSignal;

    /** Stator current status signal. */
    private final StatusSignal<Current> statorCurrentSignal;

    /** Supply current status signal. */
    private final StatusSignal<Current> supplyCurrentSignal;

    /** Motor temperature status signal. */
    private final StatusSignal<Temperature> temperatureSignal;

    /** CTRE control request used to configure this motor as a follower. */
    private final Follower followerControl = new Follower(0, MotorAlignmentValue.Aligned);

    /** CTRE Motion Magic position control request. */
    private final MotionMagicVoltage motionMagicPositionControl =
            new MotionMagicVoltage(Radians.raw(0.0)).withSlot(0);

    /** CTRE Motion Magic velocity control request. */
    private final MotionMagicVelocityVoltage velocityControl =
            new MotionMagicVelocityVoltage(RadiansPerSecond.raw(0.0)).withSlot(0);

    /** CTRE voltage control request. */
    private final VoltageOut voltageControl = new VoltageOut(Volts.raw(0.0));

    /** CTRE position control request. */
    private final PositionVoltage positionControl = new PositionVoltage(Radians.raw(0.0));

    /**
     * Creates a real motor using the provided configuration.
     *
     * <p>The TalonFX is initialized using the CAN ID, CAN bus, neutral mode, and Slot 0 configuration
     * specified by the {@link MotorConfig}.
     *
     * <p>The motor's status signals are configured to update at 100 Hz.
     *
     * @param config configuration for the motor
     */
    public TalonFXMotor(MotorConfig config) {
        super(config);
        motor = new TalonFX(config.id().id(), config.id().bus());
        motor.getConfigurator().apply(config.getTalonFXConfiguration());

        positionSignal = motor.getRotorPosition();
        velocitySignal = motor.getRotorVelocity();
        accelerationSignal = motor.getAcceleration();
        voltageSignal = motor.getMotorVoltage();
        statorCurrentSignal = motor.getStatorCurrent();
        supplyCurrentSignal = motor.getSupplyCurrent();
        temperatureSignal = motor.getDeviceTemp();

        signals.addSignals(
                positionSignal,
                velocitySignal,
                accelerationSignal,
                voltageSignal,
                statorCurrentSignal,
                supplyCurrentSignal,
                temperatureSignal);
        signals.setUpdateFrequencyForAll(Hertz.raw(100.0));
        motor.optimizeBusUtilization();
    }

    /**
     * Updates the provided inputs with the motor's current status.
     *
     * <p>The motor's status signals are refreshed and their values are copied into the provided
     * {@link MotorIOInputs} object.
     *
     * @param inputs object to populate with the motor's current inputs
     */
    @Override
    public void updateInputs(MotorIOInputs inputs) {
        signals.refreshAll();
        inputs.connected = signals.isAllGood();
        inputs.position = AngleUnit.ofWpi(positionSignal.getValue());
        inputs.velocity = AngularVelocityUnit.ofWpi(velocitySignal.getValue());
        inputs.acceleration = AngularAccelerationUnit.ofWpi(accelerationSignal.getValue());
        inputs.voltage = VoltageUnit.ofWpi(voltageSignal.getValue());
        inputs.statorCurrent = CurrentUnit.ofWpi(statorCurrentSignal.getValue());
        inputs.supplyCurrent = CurrentUnit.ofWpi(supplyCurrentSignal.getValue());
        inputs.temperature = TemperatureUnit.ofWpi(temperatureSignal.getValue());
    }

    /**
     * Configures this motor to follow another TalonFX.
     *
     * @param leader CAN ID of the motor to follow
     * @param alignment alignment of this motor relative to the leader
     */
    @Override
    public void follow(MotorIO leader, MotorAlignmentValue alignment) {
        motor.setControl(
                followerControl.withLeaderID(leader.getConfig().id().id()).withMotorAlignment(alignment));
    }

    /**
     * Sets the desired rotor position using CTRE Motion Magic position control. This is best for
     * optimizing the path to a known, static setpoint according to 1690.
     *
     * @param position desired rotor position
     */
    @Override
    public void setPositionSetpoint(Angle position) {
        motor.setControl(motionMagicPositionControl.withPosition(position.in(Rotations)));
    }

    /**
     * Sets the desired rotor position using a simple PositionVoltage control. This is better for
     * moving setpoints because the position will adjust without the delay of a Motion Magic profile.
     *
     * @param position desired rotor position
     */
    @Override
    public void setDynamicPosition(Angle position) {
        motor.setControl(positionControl.withPosition(position.in(Rotations)));
    }

    /**
     * Sets the desired rotor angular velocity using CTRE Motion Magic velocity control.
     *
     * @param velocity desired rotor angular velocity
     */
    @Override
    public void setVelocitySetpoint(AngularVelocity velocity) {
        motor.setControl(velocityControl.withVelocity(velocity.in(RotationsPerSecond)));
    }

    /**
     * Sets the voltage output of the motor.
     *
     * @param voltage voltage to apply to the motor
     */
    @Override
    public void setVoltage(Voltage voltage) {
        motor.setControl(voltageControl.withOutput(voltage.in(Volts)));
    }
}

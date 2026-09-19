package frc.lib.motor;

import static edu.wpi.first.units.Units.Hertz;
import static edu.wpi.first.units.Units.Radians;
import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.Volts;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.StatusSignalCollection;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.MotionMagicVelocityVoltage;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.units.measure.Temperature;
import edu.wpi.first.units.measure.Voltage;
import frc.lib.CanId;

/**
 * Real hardware implementation of {@link MotorIO} for a CTRE TalonFX motor
 * controller.
 * 
 * <p>
 * This class provides an interface between the library's motor abstraction
 * and a physical TalonFX. It handles retrieving motor status information,
 * configuring follower motors, and controlling the motor using position,
 * velocity, and voltage control modes.
 * 
 * <p>
 * Motor status signals are updated at 100 Hz and are optimized for reduced
 * CAN bus utilization.
 */
public class RealMotor extends MotorIO {

    /** Physical TalonFX motor controller. */
    protected final TalonFX motor;

    /** Current neutral mode configured for the motor. */
    private NeutralModeValue neutralMode;

    /** Collection of status signals used to retrieve motor inputs. */
    protected final StatusSignalCollection signals = new StatusSignalCollection(6);

    /** Rotor position status signal. */
    protected final StatusSignal<Angle> positionSignal;

    /** Rotor angular velocity status signal. */
    protected final StatusSignal<AngularVelocity> velocitySignal;

    /** Motor voltage status signal. */
    protected final StatusSignal<Voltage> voltageSignal;

    /** Stator current status signal. */
    protected final StatusSignal<Current> statorCurrentSignal;

    /** Supply current status signal. */
    protected final StatusSignal<Current> supplyCurrentSignal;

    /** Motor temperature status signal. */
    protected final StatusSignal<Temperature> temperatureSignal;

    /** CTRE control request used to configure this motor as a follower. */
    private final Follower followerControl = new Follower(0, MotorAlignmentValue.Aligned);

    /** CTRE Motion Magic position control request. */
    private final MotionMagicVoltage positionControl = new MotionMagicVoltage(Radians.of(0.0)).withSlot(0);

    /** CTRE Motion Magic velocity control request. */
    private final MotionMagicVelocityVoltage velocityControl = new MotionMagicVelocityVoltage(RadiansPerSecond.of(0.0))
            .withSlot(0);

    /** CTRE voltage control request. */
    private final VoltageOut voltageControl = new VoltageOut(Volts.of(0.0));

    /**
     * Creates a real motor using the provided configuration.
     * 
     * <p>
     * The TalonFX is initialized using the CAN ID, CAN bus, neutral mode, and
     * Slot 0 configuration specified by the {@link MotorConfig}.
     * 
     * <p>
     * The motor's status signals are configured to update at 100 Hz.
     * 
     * @param config configuration for the motor
     */
    public RealMotor(MotorConfig config) {
        super(config);
        this.neutralMode = config.neutralMode();
        motor = new TalonFX(config.id().id(), config.id().bus());
        motor.getConfigurator().apply(config.getTalonFXConfiguration());

        positionSignal = motor.getRotorPosition();
        velocitySignal = motor.getRotorVelocity();
        voltageSignal = motor.getMotorVoltage();
        statorCurrentSignal = motor.getStatorCurrent();
        supplyCurrentSignal = motor.getSupplyCurrent();
        temperatureSignal = motor.getDeviceTemp();

        signals.addSignals(
                positionSignal,
                velocitySignal,
                voltageSignal,
                statorCurrentSignal,
                supplyCurrentSignal,
                temperatureSignal);
        signals.setUpdateFrequencyForAll(Hertz.of(100.0));
        motor.optimizeBusUtilization();
    }

    /**
     * Updates the provided inputs with the motor's current status.
     * 
     * <p>
     * The motor's status signals are refreshed and their values are copied into
     * the provided {@link MotorIOInputs} object.
     * 
     * @param inputs object to populate with the motor's current inputs
     */
    @Override
    public void updateInputs(MotorIOInputs inputs) {
        signals.refreshAll();
        inputs.connected = signals.isAllGood();
        inputs.position = positionSignal.getValue();
        inputs.velocity = velocitySignal.getValue();
        inputs.voltage = voltageSignal.getValue();
        inputs.statorCurrent = statorCurrentSignal.getValue();
        inputs.supplyCurrent = supplyCurrentSignal.getValue();
        inputs.temperature = temperatureSignal.getValue();
    }

    /**
     * Configures this motor to follow another TalonFX.
     * 
     * @param leader    CAN ID of the motor to follow
     * @param alignment alignment of this motor relative to the leader
     */
    @Override
    public void follow(CanId leader, MotorAlignmentValue alignment) {
        motor.setControl(followerControl.withLeaderID(leader.id()).withMotorAlignment(alignment));
    }

    /**
     * Sets the neutral mode of the motor.
     * 
     * <p>
     * The neutral mode is only sent to the motor controller when it differs from
     * the currently configured mode.
     * 
     * @param mode neutral mode to apply to the motor
     */
    @Override
    public void setNeutralMode(NeutralModeValue mode) {
        if (mode != neutralMode) {
            neutralMode = mode;
            motor.setNeutralMode(neutralMode);
        }
    }

    /**
     * Sets the desired rotor position using CTRE Motion Magic position control.
     * 
     * @param position desired rotor position
     */
    @Override
    public void setPositionSetpoint(Angle position) {
        motor.setControl(positionControl.withPosition(position));
    }

    /**
     * Sets the desired rotor angular velocity using CTRE Motion Magic velocity
     * control.
     * 
     * @param velocity desired rotor angular velocity
     */
    @Override
    public void setVelocitySetpoint(AngularVelocity velocity) {
        motor.setControl(velocityControl.withVelocity(velocity));
    }

    /**
     * Sets the voltage output of the motor.
     * 
     * @param voltage voltage to apply to the motor
     */
    @Override
    public void setVoltage(Voltage voltage) {
        motor.setControl(voltageControl.withOutput(voltage));
    }
}

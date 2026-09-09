package frc.lib.motor;

import static edu.wpi.first.units.Units.Hertz;
import static edu.wpi.first.units.Units.Radians;
import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.Volts;

import java.util.function.Supplier;

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
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.lib.CanId;

public class RealMotor extends MotorIO {
    protected final TalonFX motor;
    private NeutralModeValue neutralMode;

    protected final StatusSignalCollection signals = new StatusSignalCollection(6);
    protected final StatusSignal<Angle> positionSignal;
    protected final StatusSignal<AngularVelocity> velocitySignal;
    protected final StatusSignal<Voltage> voltageSignal;
    protected final StatusSignal<Current> statorCurrentSignal;
    protected final StatusSignal<Current> supplyCurrentSignal;
    protected final StatusSignal<Temperature> temperatureSignal;

    private final Follower followerControl = new Follower(0, MotorAlignmentValue.Aligned);
    private final MotionMagicVoltage positionControl = new MotionMagicVoltage(Radians.of(0.0)).withSlot(0);
    private final MotionMagicVelocityVoltage velocityControl = new MotionMagicVelocityVoltage(RadiansPerSecond.of(0.0))
            .withSlot(0);
    private final VoltageOut voltageControl = new VoltageOut(Volts.of(0.0));

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

    @Override
    public void follow(CanId leader, MotorAlignmentValue alignment) {
        motor.setControl(followerControl.withLeaderID(leader.id()).withMotorAlignment(alignment));
    }

    @Override
    public Command setNeutralMode(Supplier<NeutralModeValue> mode) {
        return Commands.either(Commands.run(() -> {
            neutralMode = mode.get();
            motor.setNeutralMode(neutralMode);
        }), Commands.none(), () -> mode.get() != neutralMode);
    }

    @Override
    public Command setPositionSetpoint(Supplier<Angle> position) {
        return Commands.run(() -> motor.setControl(positionControl.withPosition(position.get())));
    }

    @Override
    public Command setVelocitySetpoint(Supplier<AngularVelocity> velocity) {
        return Commands.run(() -> motor.setControl(velocityControl.withVelocity(velocity.get())));
    }

    @Override
    public Command setVoltage(Supplier<Voltage> voltage) {
        return Commands.run(() -> motor.setControl(voltageControl.withOutput(voltage.get())));
    }
}

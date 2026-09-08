package frc.lib.cancoder;

import static edu.wpi.first.units.Units.Hertz;
import static edu.wpi.first.units.Units.Radians;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.StatusSignalCollection;
import com.ctre.phoenix6.hardware.CANcoder;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;

public class RealCanCoder implements CanCoderIO {
    private final CANcoder canCoder;

    private final StatusSignalCollection signals = new StatusSignalCollection(2);
    private final StatusSignal<Angle> positionSignal;
    private final StatusSignal<AngularVelocity> velocitySignal;

    public RealCanCoder(CanCoderConfig config) {
        canCoder = new CANcoder(config.id().id(), config.id().bus());
        canCoder.getConfigurator().apply(config.getCanCoderConfiguration());

        positionSignal = canCoder.getAbsolutePosition();
        velocitySignal = canCoder.getVelocity();

        signals.addSignals(positionSignal, velocitySignal);
        signals.setUpdateFrequencyForAll(Hertz.of(100.0));
        canCoder.optimizeBusUtilization();
    }

    @Override
    public void updateInputs(CanCoderIOInputs inputs) {
        signals.refreshAll();
        inputs.connected = signals.isAllGood();
        inputs.position = positionSignal.getValue();
        inputs.velocity = velocitySignal.getValue();
    }

    @Override
    public Command setZeroPoint() {
        return Commands.run(() -> canCoder.setPosition(Radians.of(0.0)));
    }
}

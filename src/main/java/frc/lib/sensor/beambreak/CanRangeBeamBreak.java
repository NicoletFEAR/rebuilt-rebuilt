package frc.lib.sensor.beambreak;

import static edu.wpi.first.units.Units.Hertz;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.hardware.CANrange;
import frc.lib.CanId;

public class CanRangeBeamBreak implements BeamBreakIO {
    private final CANrange canRange;

    private final StatusSignal<Boolean> trippedSignal;

    public CanRangeBeamBreak(CanId id) {
        canRange = new CANrange(id.id());

        trippedSignal = canRange.getIsDetected();
        trippedSignal.setUpdateFrequency(Hertz.of(100));
        canRange.optimizeBusUtilization();
    }

    @Override
    public void updateInputs(BeamBreakIOInputs inputs) {
        trippedSignal.refresh();
        inputs.connected = StatusSignal.isAllGood();
        inputs.tripped = trippedSignal.getValue();
    }
}

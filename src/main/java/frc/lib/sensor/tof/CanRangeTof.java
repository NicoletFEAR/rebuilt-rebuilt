package frc.lib.sensor.tof;

import static edu.wpi.first.units.Units.Hertz;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.hardware.CANrange;
import edu.wpi.first.units.measure.Distance;

public class CanRangeTof implements TofIO {
    private final CANrange canRange;

    private final StatusSignal<Distance> distanceSignal;

    public CanRangeTof(CanRangeTofConfig config) {
        canRange = new CANrange(config.id().id(), config.id().bus());
        canRange.getConfigurator().apply(config.getCanRangeConfiguration());

        distanceSignal = canRange.getDistance();
        distanceSignal.setUpdateFrequency(Hertz.of(100));
        canRange.optimizeBusUtilization();
    }

    @Override
    public void updateInputs(TofIOInputs inputs) {
        distanceSignal.refresh();
        inputs.connected = StatusSignal.isAllGood();
        inputs.distance = distanceSignal.getValue();
    }
}

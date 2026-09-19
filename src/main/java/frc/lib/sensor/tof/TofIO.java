package frc.lib.sensor.tof;

import static edu.wpi.first.units.Units.Meters;

import edu.wpi.first.units.measure.Distance;
import org.littletonrobotics.junction.AutoLog;

public interface TofIO {

    @AutoLog
    public static class TofIOInputs {
        public boolean connected = false;

        public Distance distance = Meters.of(0.0);
    }

    void updateInputs(TofIOInputs input);
}

package frc.lib.sensor.beambreak;

import org.littletonrobotics.junction.AutoLog;

public interface BeamBreakIO {

    @AutoLog
    public static class BeamBreakIOInputs {

        public boolean connected = false;

        public boolean tripped = false;
    }

    void updateInputs(BeamBreakIOInputs inputs);
}

package frc.lib.candle;

import org.littletonrobotics.junction.AutoLog;

public interface CandleIO {
    @AutoLog
    public static class CandleIOInputs {
        public boolean connected = false;
    }
}

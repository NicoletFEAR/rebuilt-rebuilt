package frc.lib.cancoder;

import static edu.wpi.first.units.Units.Radians;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.MagnetSensorConfigs;
import com.ctre.phoenix6.signals.SensorDirectionValue;
import edu.wpi.first.units.measure.Angle;
import frc.lib.CanId;

/**
 * Used to automatically configure a CTRE CANcoder
 * 
 * @param id     The ID of the CANcoder in Phoenix Tuner
 * @param offset The zero offset of the CANcoder
 */
public record CanCoderConfig(CanId id, Angle offset) {

    /**
     * Configures and returns a fully configured CANcoder
     * 
     * @return The configuration of the CANcoder
     */
    public CANcoderConfiguration getCanCoderConfiguration() {
        return new CANcoderConfiguration()
                .withMagnetSensor(
                        new MagnetSensorConfigs()
                                .withAbsoluteSensorDiscontinuityPoint(Radians.of(Math.PI * 2.0))
                                .withMagnetOffset(offset)
                                .withSensorDirection(SensorDirectionValue.CounterClockwise_Positive));
    }
}

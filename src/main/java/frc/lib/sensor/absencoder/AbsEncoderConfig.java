package frc.lib.sensor.absencoder;

import static frc.lib.units.AngleUnit.Rotations;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.MagnetSensorConfigs;
import com.ctre.phoenix6.signals.SensorDirectionValue;
import frc.lib.CanId;
import frc.lib.units.Angle;

/**
 * Used to represent a vendor-agnostic absolute encoder configuration.
 *
 * @param id the CAN ID of the absolute encoder
 * @param offset the zero offset of the absolute encoder
 */
public record AbsEncoderConfig(CanId id, Angle offset) {

    /**
     * Gets the CTRE CANcoderConfiguration according to this object's fields. Sets a default
     * discontinuity point at 1 rotation, meaning the interval of potential outputs is [0, 1).
     * Additionally, sets the sensor direction to be counter-clockwise positive.
     *
     * @return the configuration of the CANcoder
     */
    public CANcoderConfiguration getCanCoderConfiguration() {
        return new CANcoderConfiguration()
                .withMagnetSensor(new MagnetSensorConfigs()
                        .withAbsoluteSensorDiscontinuityPoint(Rotations.raw(1.0))
                        .withMagnetOffset(offset.in(Rotations))
                        .withSensorDirection(SensorDirectionValue.CounterClockwise_Positive));
    }
}

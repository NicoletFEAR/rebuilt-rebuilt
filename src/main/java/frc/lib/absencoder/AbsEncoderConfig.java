package frc.lib.absencoder;

import static edu.wpi.first.units.Units.Radians;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.MagnetSensorConfigs;
import com.ctre.phoenix6.signals.SensorDirectionValue;
import edu.wpi.first.units.measure.Angle;
import frc.lib.CanId;

/**
 * Used to represent a vendor-agnostic absolute encoder configuration
 *
 * @param id The CAN ID of the absolute encoder
 * @param offset The zero offset of the absolute encoder
 */
public record AbsEncoderConfig(CanId id, Angle offset) {

    /**
     * Gets the CTRE CANcoderConfiguration according to this object's fields
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

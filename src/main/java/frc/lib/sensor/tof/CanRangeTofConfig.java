package frc.lib.sensor.tof;

import com.ctre.phoenix6.configs.CANrangeConfiguration;
import com.ctre.phoenix6.configs.ProximityParamsConfigs;
import frc.lib.CanId;
import frc.lib.constants.Constants;

/**
 * Stores the configuration for a CTRE CANrange time-of-flight sensor.
 * 
 * <p>
 * This record contains the CAN ID and CAN bus used by the sensor. The
 * configuration can be converted into a CTRE {@link CANrangeConfiguration}
 * using {@link #getCanRangeConfiguration()}.
 * 
 * @param id CAN ID and CAN bus of the CANrange sensor
 */
public record CanRangeTofConfig(CanId id) {

    /**
     * Creates a CTRE CANrange configuration using the library's configured
     * time-of-flight sensor tolerance.
     * 
     * <p>
     * The proximity threshold is set using {@link Constants#TOF_SENSOR_TOLERANCE}.
     * 
     * @return a configured {@link CANrangeConfiguration}
     */
    public CANrangeConfiguration getCanRangeConfiguration() {
        return new CANrangeConfiguration()
                .withProximityParams(
                        new ProximityParamsConfigs().withProximityThreshold(Constants.TOF_SENSOR_TOLERANCE));
    }
}

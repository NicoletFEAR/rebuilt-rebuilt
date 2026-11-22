package frc.lib.sensor.beambreak;

import com.ctre.phoenix6.configs.CANrangeConfiguration;
import com.ctre.phoenix6.configs.ProximityParamsConfigs;
import frc.lib.CanId;
import org.wpilib.units.measure.Distance;

/**
 * Stores the configuration for a beam break sensor.
 *
 * <p>This record contains the CAN ID and CAN bus used by the sensor along with the sensor's
 * proximity threshold. The configuration can be converted into a CTRE {@link CANrangeConfiguration}
 * using {@link #getCanRangeConfiguration()}.
 *
 * @param id CAN ID and CAN bus of the beam break sensor
 * @param proximityThreshold proximity threshold of the sensor
 */
public record BeamBreakConfig(CanId id, Distance proximityThreshold) {

    /**
     * Creates a CTRE CANrange configuration using this record's fields.
     *
     * <p>The proximity threshold is set from {@link #proximityThreshold()}.
     *
     * @return a configured {@link CANrangeConfiguration}
     */
    public CANrangeConfiguration getCanRangeConfiguration() {
        return new CANrangeConfiguration()
                .withProximityParams(new ProximityParamsConfigs().withProximityThreshold(proximityThreshold));
    }
}

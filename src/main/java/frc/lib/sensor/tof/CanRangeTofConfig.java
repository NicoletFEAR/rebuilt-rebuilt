package frc.lib.sensor.tof;

import com.ctre.phoenix6.configs.CANrangeConfiguration;
import com.ctre.phoenix6.configs.ProximityParamsConfigs;
import frc.lib.CanId;
import frc.lib.constants.Constants;

public record CanRangeTofConfig(CanId id) {
    public CANrangeConfiguration getCanRangeConfiguration() {
        return new CANrangeConfiguration()
                .withProximityParams(
                        new ProximityParamsConfigs().withProximityThreshold(Constants.TOF_SENSOR_TOLERANCE));
    }
}

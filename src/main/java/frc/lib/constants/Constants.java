package frc.lib.constants;

import static edu.wpi.first.units.Units.Meters;
import static edu.wpi.first.units.Units.Radians;

import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.Distance;

public final class Constants {
    public static final double VELOCITY_SETPOINT_TOLERANCE_MULTIPLIER = 0.05;
    public static final Angle ANGULAR_POSITION_SETPOINT_TOLERANCE = Radians.of(0.05);
    public static final Distance LINEAR_POSITION_SETPOINT_TOLERANCE = Meters.of(0.005);

    private Constants() {
    }
}

package frc.lib.constants;

import static edu.wpi.first.units.Units.Hertz;
import static edu.wpi.first.units.Units.Meters;
import static edu.wpi.first.units.Units.Radians;

import com.ctre.phoenix6.CANBus;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.units.measure.Frequency;

/**
 * This file houses all of the general constants that are needed to run the
 * robot
 */
public final class Constants {
    /**
     * Used as a reference for how long each periodic loop runs for
     */
    public static final Frequency LOOP_PERIOD = Hertz.of(0.02);

    /**
     * These are the tolerances for setpoints on for all types of mechanisms
     */
    public static final double VELOCITY_SETPOINT_TOLERANCE_MULTIPLIER = 0.05;
    public static final Angle ANGULAR_POSITION_SETPOINT_TOLERANCE = Radians.of(0.05);
    public static final Distance LINEAR_POSITION_SETPOINT_TOLERANCE = Meters.of(0.005);

    /**
     * Used to define the different CAN Busses on the Robot
     * Important for the SystemCore since there are 5 CAN Busses
     */
    public static final CANBus RIO_BUS = new CANBus("rio");
    public static final CANBus CANIVORE_BUS = new CANBus("*");

    private Constants() {
        /* Keep this constructor empty */}
}

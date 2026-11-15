package frc.robot.subsystems;

import com.ctre.phoenix6.CANBus;
import frc.lib.CanId;

/**
 * CAN IDs of every device on the robot.
 *
 * <p>All devices are on the {@code CANIVORE_BUS} bus defined in {@link frc.lib.ToleranceConstants}.
 * Front left/right and back left/right refer to the four swerve modules; the launcher consists of
 * the left and right flywheel motors, the indexer, the hood, and the hood encoder.
 */
public final class DeviceIds {

    /**
     * The RoboRIO's CAN bus, which is slower than the CANivore bus but must be used by non-CTRE
     * devices.
     */
    public static final CANBus RIO_BUS = new CANBus("rio");

    /**
     * The CANivore's CAN bus, which should be used by all CTRE devices because it's faster, but
     * cannot be used by any non-CTRE devices.
     */
    public static final CANBus CANIVORE_BUS = new CANBus("*");

    public static final CanId FRONT_LEFT_DRIVE = new CanId(CANIVORE_BUS, 2);
    public static final CanId FRONT_LEFT_TURN = new CanId(CANIVORE_BUS, 1);
    public static final CanId FRONT_LEFT_CAN_CODER = new CanId(CANIVORE_BUS, 3);

    public static final CanId FRONT_RIGHT_DRIVE = new CanId(CANIVORE_BUS, 5);
    public static final CanId FRONT_RIGHT_TURN = new CanId(CANIVORE_BUS, 4);
    public static final CanId FRONT_RIGHT_CAN_CODER = new CanId(CANIVORE_BUS, 6);

    public static final CanId BACK_LEFT_DRIVE = new CanId(CANIVORE_BUS, 11);
    public static final CanId BACK_LEFT_TURN = new CanId(CANIVORE_BUS, 10);
    public static final CanId BACK_LEFT_CAN_CODER = new CanId(CANIVORE_BUS, 12);

    public static final CanId BACK_RIGHT_DRIVE = new CanId(CANIVORE_BUS, 8);
    public static final CanId BACK_RIGHT_TURN = new CanId(CANIVORE_BUS, 7);
    public static final CanId BACK_RIGHT_CAN_CODER = new CanId(CANIVORE_BUS, 9);

    public static final CanId LEFT_LAUNCHER = new CanId(CANIVORE_BUS, 14);
    public static final CanId RIGHT_LAUNCHER = new CanId(CANIVORE_BUS, 15);
    public static final CanId INDEXER = new CanId(CANIVORE_BUS, 16);
    public static final CanId HOOD = new CanId(CANIVORE_BUS, 23);
    public static final CanId HOOD_ENCODER = new CanId(CANIVORE_BUS, 22);

    public static final CanId INTAKE_WHEELS = new CanId(CANIVORE_BUS, 18);
    public static final CanId INTAKE_ARM = new CanId(CANIVORE_BUS, 19);

    private DeviceIds() {
        // Keep this constructor empty.
    }
}

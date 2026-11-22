package frc.robot.subsystems;

import com.ctre.phoenix6.CANBus;
import frc.lib.CanId;
import frc.robot.subsystems.drivebase.SwerveModuleName;
import java.util.HashMap;

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

    /**
     * The drive motor IDs for each swerve module. Use {@link SwerveModuleName#drive()} instead of
     * referencing this directly.
     */
    public static final HashMap<SwerveModuleName, CanId> DRIVE = new HashMap<>();

    /**
     * The turn motor IDs for each swerve module. Use {@link SwerveModuleName#turn()} instead of
     * referencing this directly.
     */
    public static final HashMap<SwerveModuleName, CanId> TURN = new HashMap<>();

    /**
     * The absolute encoder IDs for each swerve module. Use {@link SwerveModuleName#absEncoder()}
     * instead of referencing this directly.
     */
    public static final HashMap<SwerveModuleName, CanId> SWERVE_ABS_ENCODER = new HashMap<>();

    static {
        TURN.put(SwerveModuleName.FRONT_LEFT, new CanId(CANIVORE_BUS, 1));
        TURN.put(SwerveModuleName.FRONT_RIGHT, new CanId(CANIVORE_BUS, 4));
        TURN.put(SwerveModuleName.BACK_LEFT, new CanId(CANIVORE_BUS, 10));
        TURN.put(SwerveModuleName.BACK_RIGHT, new CanId(CANIVORE_BUS, 7));

        DRIVE.put(SwerveModuleName.FRONT_LEFT, new CanId(CANIVORE_BUS, 2));
        DRIVE.put(SwerveModuleName.FRONT_RIGHT, new CanId(CANIVORE_BUS, 5));
        DRIVE.put(SwerveModuleName.BACK_LEFT, new CanId(CANIVORE_BUS, 11));
        DRIVE.put(SwerveModuleName.BACK_RIGHT, new CanId(CANIVORE_BUS, 8));

        SWERVE_ABS_ENCODER.put(SwerveModuleName.FRONT_LEFT, new CanId(CANIVORE_BUS, 3));
        SWERVE_ABS_ENCODER.put(SwerveModuleName.FRONT_RIGHT, new CanId(CANIVORE_BUS, 6));
        SWERVE_ABS_ENCODER.put(SwerveModuleName.BACK_LEFT, new CanId(CANIVORE_BUS, 12));
        SWERVE_ABS_ENCODER.put(SwerveModuleName.BACK_RIGHT, new CanId(CANIVORE_BUS, 9));
    }

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

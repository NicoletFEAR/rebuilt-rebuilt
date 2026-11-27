package com.nicoletfear.robot.subsystems;

import com.ctre.phoenix6.CANBus;
import com.nicoletfear.lib.CanId;
import com.nicoletfear.robot.subsystems.drivebase.SwerveModuleName;
import java.util.HashMap;
import org.wpilib.hardware.bus.CANPort;

/** CAN IDs of every device on the robot. */
public final class DeviceIds {

    /** The CAN bus for all devices associated with the drivebase. */
    public static final CANBus DRIVEBASE_BUS = new CANBus(CANPort.CAN_S0);

    /** The CAN bus for all devices associated with the intake. */
    public static final CANBus INTAKE_BUS = new CANBus(CANPort.CAN_S1);

    /** The CAN bus for all devices associated with the launcher. */
    public static final CANBus LAUNCHER_BUS = new CANBus(CANPort.CAN_S2);

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
        TURN.put(SwerveModuleName.FRONT_LEFT, new CanId(DRIVEBASE_BUS, 1));
        TURN.put(SwerveModuleName.FRONT_RIGHT, new CanId(DRIVEBASE_BUS, 4));
        TURN.put(SwerveModuleName.BACK_LEFT, new CanId(DRIVEBASE_BUS, 10));
        TURN.put(SwerveModuleName.BACK_RIGHT, new CanId(DRIVEBASE_BUS, 7));

        DRIVE.put(SwerveModuleName.FRONT_LEFT, new CanId(DRIVEBASE_BUS, 2));
        DRIVE.put(SwerveModuleName.FRONT_RIGHT, new CanId(DRIVEBASE_BUS, 5));
        DRIVE.put(SwerveModuleName.BACK_LEFT, new CanId(DRIVEBASE_BUS, 11));
        DRIVE.put(SwerveModuleName.BACK_RIGHT, new CanId(DRIVEBASE_BUS, 8));

        SWERVE_ABS_ENCODER.put(SwerveModuleName.FRONT_LEFT, new CanId(DRIVEBASE_BUS, 3));
        SWERVE_ABS_ENCODER.put(SwerveModuleName.FRONT_RIGHT, new CanId(DRIVEBASE_BUS, 6));
        SWERVE_ABS_ENCODER.put(SwerveModuleName.BACK_LEFT, new CanId(DRIVEBASE_BUS, 12));
        SWERVE_ABS_ENCODER.put(SwerveModuleName.BACK_RIGHT, new CanId(DRIVEBASE_BUS, 9));
    }

    public static final CanId LEFT_LAUNCHER = new CanId(LAUNCHER_BUS, 14);
    public static final CanId RIGHT_LAUNCHER = new CanId(LAUNCHER_BUS, 15);
    public static final CanId INDEXER = new CanId(LAUNCHER_BUS, 16);
    public static final CanId HOOD = new CanId(LAUNCHER_BUS, 23);
    public static final CanId HOOD_ENCODER = new CanId(LAUNCHER_BUS, 22);

    public static final CanId INTAKE_WHEELS = new CanId(INTAKE_BUS, 18);
    public static final CanId INTAKE_ARM = new CanId(INTAKE_BUS, 19);

    private DeviceIds() {
        // Keep this constructor empty.
    }
}

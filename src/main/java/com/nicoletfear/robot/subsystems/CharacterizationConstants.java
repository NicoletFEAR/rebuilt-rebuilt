package com.nicoletfear.robot.subsystems;

import static com.nicoletfear.lib.units.TimeUnit.Seconds;
import static com.nicoletfear.lib.units.VoltageUnit.Volts;
import static com.nicoletfear.lib.units.VoltageVelocityUnit.VoltsPerSecond;

import com.nicoletfear.lib.units.Time;
import com.nicoletfear.lib.units.Voltage;
import com.nicoletfear.lib.units.VoltageVelocity;

/** Constants relating to subsystem characterization. */
public final class CharacterizationConstants {
    /** Voltage ramp rate for dynamic drive SysID for the drivebase. */
    public static final VoltageVelocity DRIVE_RAMP_RATE = VoltsPerSecond.of(1.0);

    /** Voltage ramp rate for dynamic turn SysID for the drivebase. */
    public static final VoltageVelocity TURN_RAMP_RATE = VoltsPerSecond.of(1.0);

    /** Voltage ramp rate for dynamic rotation SysID for the drivebase. */
    public static final VoltageVelocity ROTATION_RAMP_RATE = VoltsPerSecond.of(Math.PI / 6.0);

    /** Voltage step amount for quasistatic drive SysID for the drivebase. */
    public static final Voltage DRIVE_STEP_VOLTAGE = Volts.of(4.0);

    /** Voltage step amount for quasistatic turn SysID for the drivebase. */
    public static final Voltage TURN_STEP_VOLTAGE = Volts.of(7.0);

    /** Voltage step amount for quasistatic rotation SysID for the drivebase. */
    public static final Voltage ROTATION_STEP_VOLTAGE = Volts.of(Math.PI);

    /** Timeout between SysID phases. */
    public static final Time SYS_ID_TIMEOUT = Seconds.of(5.0);

    private CharacterizationConstants() {
        /* Keep this constructor empty. */
    }
}

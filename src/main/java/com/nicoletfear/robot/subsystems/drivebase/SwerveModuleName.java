package com.nicoletfear.robot.subsystems.drivebase;

import com.nicoletfear.lib.CanId;
import com.nicoletfear.lib.StringHelpers;
import com.nicoletfear.lib.units.Angle;
import com.nicoletfear.robot.subsystems.DeviceIds;
import com.nicoletfear.robot.subsystems.drivebase.TurnMotor.TurnMotorConstants;
import java.util.Optional;

/** All the different swerve modules on the robot. */
public enum SwerveModuleName {
    FRONT_LEFT,
    FRONT_RIGHT,
    BACK_LEFT,
    BACK_RIGHT,
    ;

    private final String name;

    private SwerveModuleName() {
        // Converts the name of the enum variant to a more readable version for logging
        this.name = StringHelpers.screamingSnakeCaseToWords(this.name());
    }

    /** Returns the name of the module, as title-case words. */
    @Override
    public String toString() {
        return name;
    }

    /** Returns this module's absolute encoder offset from {@link TurnMotorConstants#OFFSET}. */
    public Angle offset() {
        // This `Optional.ofNullable` part followed by `orElseThrow()` will throw an exception
        // if the value we get from the map is null, since if the map doesn't have a value
        // for a specific swerve module, it should be an error.
        return Optional.ofNullable(TurnMotorConstants.OFFSET.get(this)).orElseThrow();
    }

    /** Returns this module's drive motor ID from {@link DeviceIds#DRIVE}. */
    public CanId drive() {
        return Optional.ofNullable(DeviceIds.DRIVE.get(this)).orElseThrow();
    }

    /** Returns this module's turn motor ID from {@link DeviceIds#TURN}. */
    public CanId turn() {
        return Optional.ofNullable(DeviceIds.TURN.get(this)).orElseThrow();
    }

    /** Returns this module's absolute encoder ID from {@link DeviceIds#SWERVE_ABS_ENCODER}. */
    public CanId absEncoder() {
        return Optional.ofNullable(DeviceIds.SWERVE_ABS_ENCODER.get(this)).orElseThrow();
    }
}

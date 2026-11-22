package frc.robot.subsystems.drive;

import frc.lib.CanId;
import frc.lib.StringHelpers;
import frc.lib.units.Angle;
import frc.robot.subsystems.DeviceIds;
import frc.robot.subsystems.drive.Drive.DriveConstants;

/** All the different swerve modules on the robot. */
public enum SwerveModuleName {
    FRONT_LEFT,
    FRONT_RIGHT,
    BACK_LEFT,
    BACK_RIGHT,
    ;

    private String name;

    private SwerveModuleName() {
        // Converts the name of the enum variant to a more readable version for logging
        this.name = StringHelpers.screamingSnakeCaseToWords(this.name());
    }

    /** Returns the name of the module, as title-case words. */
    @Override
    public String toString() {
        return name;
    }

    /** Returns this module's absolute encoder offset from {@link DriveConstants#OFFSET}. */
    public Angle offset() {
        return DriveConstants.OFFSET.get(this);
    }

    /** Returns this module's drive motor ID from {@link DeviceIds#DRIVE}. */
    public CanId drive() {
        return DeviceIds.DRIVE.get(this);
    }

    /** Returns this module's turn motor ID from {@link DeviceIds#TURN}. */
    public CanId turn() {
        return DeviceIds.TURN.get(this);
    }

    /** Returns this module's absolute encoder ID from {@link DeviceIds#SWERVE_ABS_ENCODER}. */
    public CanId absEncoder() {
        return DeviceIds.SWERVE_ABS_ENCODER.get(this);
    }
}

package frc.robot.subsystems.drive;

import frc.lib.StringHelpers;
import frc.lib.units.Angle;
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
        this.name = StringHelpers.screamingSnakeCaseToWords(this.name());
    }

    /** Returns the name of the module, as title-case words. */
    @Override
    public String toString() {
        return name;
    }

    public Angle offset() {
        return DriveConstants.OFFSET.get(this);
    }
}

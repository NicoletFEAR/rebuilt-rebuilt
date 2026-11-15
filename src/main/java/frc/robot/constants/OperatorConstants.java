package frc.robot.constants;

/** Constants related to operator input handling. */
public final class OperatorConstants {

    /** Deadband applied to the driver gamepad's axes and triggers. */
    public static final double DEADBAND = 0.1;

    /** Value above which a trigger is considered pressed. */
    public static final double TRIGGER_THRESHOLD = 0.9;

    private OperatorConstants() {
        /* Keep this constructor empty */
    }
}

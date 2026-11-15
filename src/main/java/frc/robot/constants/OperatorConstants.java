package frc.robot.constants;

/** Constants related to operator input handling. */
public final class OperatorConstants {

    /**
     * Deadband applied to the driver gamepad's axes and triggers. 0.1 is a good value
     * because it eliminates tiny accidental brushes but still allows precise control
     * when desired.
     */
    public static final double DEADBAND = 0.1;

    /** 
     * Value above which a trigger is considered pressed. High to prevent accidental
     * triggering, but still needs to be tested to make sure it's high enough to
     * prevent intentional triggering.
     */
    public static final double TRIGGER_THRESHOLD = 0.9;

    private OperatorConstants() {
        /* Keep this constructor empty */
    }
}

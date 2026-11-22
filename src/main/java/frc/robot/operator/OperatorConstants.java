package frc.robot.operator;

/** Constants related to operator input handling. */
public final class OperatorConstants {

    /**
     * Deadband applied to the driver gamepad's axes and triggers. 0.1 is a good value because it
     * eliminates tiny accidental brushes but still allows precise control when desired.
     */
    public static final double DEADBAND = 0.1;

    /**
     * Value above which a trigger is considered pressed. High to prevent accidental triggering.
     */
    public static final double TRIGGER_THRESHOLD = 0.95;

    private OperatorConstants() {
        // Keep this constructor empty.
    }
}

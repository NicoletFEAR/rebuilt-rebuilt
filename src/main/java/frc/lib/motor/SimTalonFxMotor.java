package frc.lib.motor;

/**
 * Simulation implementation of {@link MotorIO} for a CTRE TalonFX motor controller.
 *
 * <p>This class is intended to run as much of the code for the {@link TalonFxMotor} as possible so
 * that any errors that could happen in that class will be caught in simulation by this one.
 */
public final class SimTalonFxMotor extends TalonFxMotor {
    /**
     * Creates a simulated TalonFX motor using the provided configuration, in the same way a real
     * motor would be created.
     *
     * @param config configuration for the motor
     */
    public SimTalonFxMotor(MotorConfig config) {
        super(config);
    }
}

package frc.lib.motor;

/**
 * Simulation implementation of {@link MotorIO}.
 */
public final class SimTalonFxMotor extends TalonFxMotor {
    /**
     * Creates a simulated TalonFX motor using the provided configuration, in the same way
     * a real motor would be created.
     * 
     * @param config configuration for the motor
     */
    public SimTalonFxMotor(MotorConfig config) {
        super(config);
    }
}

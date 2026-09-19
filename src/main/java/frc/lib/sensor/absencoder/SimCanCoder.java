package frc.lib.sensor.absencoder;

/** Creates a Simulation CANcoder for when we are simulating the robot */
public class SimCanCoder extends RealCanCoder {

    /**
     * Sets up the Simulated CANcoder the same way a real CANcoder would be setup
     *
     * @param config Is the configuration for the CANcoder you want to simulate. This should be the
     *     same config used for the real CANcoder counterpart
     */
    public SimCanCoder(AbsEncoderConfig config) {
        super(config);
    }

    /**
     * Sets the connected value as false, causing the subsystem to default to using the motor's
     * position rather than the position provided by the absolute encoder. This is fine because in
     * simulation, we only care about the relative position, and assume the absolute position is
     * correct.
     */
    @Override
    public void updateInputs(AbsEncoderIOInputs inputs) {
        inputs.connected = false;
    }
}

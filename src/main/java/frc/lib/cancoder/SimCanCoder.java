package frc.lib.cancoder;

/** Creates a Simulation CANcoder for when we are simulating the robot */
public class SimCanCoder extends RealCanCoder {

    /**
     * Sets up the Simulated CANcoder the same way a real CANcoder would be setup
     *
     * @param config Is the configuration for the CANcoder you want to simulate. This should be the
     *     same config used for the real CANcoder counterpart
     */
    public SimCanCoder(CanCoderConfig config) {
        super(config);
    }

    /** Sets the connected value as false since the CANcoder is not real */
    @Override
    public void updateInputs(CanCoderIOInputs inputs) {
        inputs.connected = false;
    }
}

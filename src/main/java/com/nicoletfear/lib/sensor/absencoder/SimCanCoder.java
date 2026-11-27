package com.nicoletfear.lib.sensor.absencoder;

/** Creates a simulation CANcoder for when we are simulating the robot. */
public final class SimCanCoder extends CanCoder {

    /**
     * Sets up the simulated CANcoder the same way a real CANcoder would be set up.
     *
     * @param config the configuration for the CANcoder
     */
    public SimCanCoder(AbsEncoderConfig config) {
        super(config);
    }

    /**
     * Sets the connected value as false, causing the subsystem to default to using the motor's
     * position rather than the position provided by the absolute encoder. This is fine because in
     * simulation, we only care about the relative position, and assume the absolute position is
     * correct.
     *
     * @param inputs object to populate with the CANcoder's current inputs
     */
    @Override
    public void updateInputs(AbsEncoderIOInputs inputs) {
        inputs.connected = false;
    }
}

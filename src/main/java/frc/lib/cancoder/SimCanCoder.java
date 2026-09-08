package frc.lib.cancoder;

public class SimCanCoder extends RealCanCoder {
    public SimCanCoder(CanCoderConfig config) {
        super(config);
    }

    @Override
    public void updateInputs(CanCoderIOInputs inputs) {
        inputs.connected = false;
    }
}

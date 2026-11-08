package frc.lib.sensor.tof;

import frc.lib.CanId;

public class SimCanTof extends CanTof {

    public SimCanTof(CanId id) {
        super(id);
    }

    @Override
    public void updateInputs(TofIOInputs inputs) {
        inputs.connected = false;
    }
}

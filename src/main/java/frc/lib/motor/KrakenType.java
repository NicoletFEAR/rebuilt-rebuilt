package frc.lib.motor;

import edu.wpi.first.math.system.plant.DCMotor;

public enum KrakenType {
    X44,
    X60,
    ;

    DCMotor getDcMotor() {
        return switch (this) {
            case X44 -> DCMotor.getKrakenX44(1);
            case X60 -> DCMotor.getKrakenX60(1);
        };
    }
}

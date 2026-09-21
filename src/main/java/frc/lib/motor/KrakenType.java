package frc.lib.motor;

import edu.wpi.first.math.system.plant.DCMotor;

/**
 * Represents the available Kraken motor types supported by the library.
 *
 * <p>Each motor type can be converted into a {@link DCMotor} model for use with WPILib's system
 * identification and simulation tools.
 */
public enum KrakenType {

    /** CTRE Kraken x44 Motor */
    X44,

    /** CTRE Kraken x60 Motor */
    X60,
    ;

    /**
     * Gets the WPILib {@link DCMotor} model corresponding to this Kraken motor type.
     *
     * <p>The returned motor model represents a single motor.
     *
     * @return the {@link DCMotor} model for this motor type
     */
    DCMotor getDcMotor() {
        return switch (this) {
            case X44 -> DCMotor.getKrakenX44(1);
            case X60 -> DCMotor.getKrakenX60(1);
        };
    }
}

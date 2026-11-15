package frc.lib;

import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.CANcoderConfigurator;
import com.ctre.phoenix6.configs.CANrangeConfiguration;
import com.ctre.phoenix6.configs.CANrangeConfigurator;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfigurator;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.CANrange;
import com.ctre.phoenix6.hardware.TalonFX;

/**
 * Applies CTRE Phoenix 6 configurations to devices.
 *
 * <p>Each method applies the given configuration to the device and, if the initial attempt does not
 * succeed, retries the apply once. The return value reports whether either attempt succeeded.
 */
public final class Configurator {

    /**
     * Applies a {@link TalonFXConfiguration} to a TalonFX, retrying once if the first attempt fails.
     *
     * @param motor the motor to configure
     * @param config the configuration to apply
     * @return {@code true} if the configuration was applied successfully, {@code false} otherwise
     */
    public static boolean applyTalonFxConfiguration(TalonFX motor, TalonFXConfiguration config) {
        TalonFXConfigurator configurator = motor.getConfigurator();
        StatusCode result = configurator.apply(config);

        if (result.isOK()) {
            return true;
        }

        result = configurator.apply(config);
        return result.isOK();
    }

    /**
     * Applies a {@link CANcoderConfiguration} to a CANcoder, retrying once if the first attempt
     * fails.
     *
     * @param canCoder the CANcoder to configure
     * @param config the configuration to apply
     * @return {@code true} if the configuration was applied successfully, {@code false} otherwise
     */
    public static boolean applyCanCoderConfiguration(
            CANcoder canCoder, CANcoderConfiguration config) {
        CANcoderConfigurator configurator = canCoder.getConfigurator();
        StatusCode result = configurator.apply(config);

        if (result.isOK()) {
            return true;
        }

        result = configurator.apply(config);
        return result.isOK();
    }

    /**
     * Applies a {@link CANrangeConfiguration} to a CANrange, retrying once if the first attempt
     * fails.
     *
     * @param canRange the CANrange to configure
     * @param config the configuration to apply
     * @return {@code true} if the configuration was applied successfully, {@code false} otherwise
     */
    public static boolean applyCanRangeConfiguration(
            CANrange canRange, CANrangeConfiguration config) {
        CANrangeConfigurator configurator = canRange.getConfigurator();
        StatusCode result = configurator.apply(config);

        if (result.isOK()) {
            return true;
        }

        result = configurator.apply(config);
        return result.isOK();
    }

    private Configurator() {
        // Keep this constructor empty.
    }
}

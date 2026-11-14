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

public final class Configurator {
    public static boolean applyTalonFxConfiguration(TalonFX motor, TalonFXConfiguration config) {
        TalonFXConfigurator configurator = motor.getConfigurator();
        StatusCode result = configurator.apply(config);

        if (result.isOK()) {
            return true;
        }

        result = configurator.apply(config);
        return result.isOK();
    }

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

    private Configurator() {}
}

package frc.lib.motor;

import com.ctre.phoenix6.configs.Slot0Configs;
import lombok.Builder;

/**
 * Stores the feedforward and feedback gains used to configure a motor's
 * Phoenix 6 Slot 0 configuration.
 * 
 * <p>
 * This record contains the PID gains ({@code p}, {@code i}, and {@code d})
 * along with the feedforward gains ({@code s}, {@code v}, {@code a}, and
 * {@code g}). These values can be converted into a CTRE {@link Slot0Configs}
 * object using {@link #getSlot0Configs()}.
 * 
 * <p>
 * Lombok's {@link Builder} annotation provides a builder for conveniently
 * creating instances of this record.
 * 
 * @param p proportional gain
 * @param i integral gain
 * @param d derivative gain
 * @param s static feedforward gain
 * @param v velocity feedforward gain
 * @param a acceleration feedforward gain
 * @param g gravity feedforward gain
 */
@Builder
public record FeedforwardValues(
        double p,
        double i,
        double d,
        double s,
        double v,
        double a,
        double g) {

    /**
     * Creates feedforward values with the gravity feedforward gain set to zero.
     * 
     * @param p proportional gain
     * @param i integral gain
     * @param d derivative gain
     * @param s static feedforward gain
     * @param v velocity feedforward gain
     * @param a acceleration feedforward gain
     */
    public FeedforwardValues(
            double p,
            double i,
            double d,
            double s,
            double v,
            double a) {
        this(p, i, d, s, v, a, 0.0);
    }

    /**
     * Converts these feedforward and feedback values into a CTRE Phoenix 6
     * {@link Slot0Configs} configuration.
     * 
     * @return a {@link Slot0Configs} containing the configured PID and feedforward
     *         gains
     */
    public Slot0Configs getSlot0Configs() {
        return new Slot0Configs()
                .withKP(p)
                .withKI(i)
                .withKD(d)
                .withKS(s)
                .withKV(v)
                .withKA(a)
                .withKG(g);
    }
}

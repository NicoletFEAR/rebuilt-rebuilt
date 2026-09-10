package frc.lib.motor;

import com.ctre.phoenix6.configs.Slot0Configs;

import lombok.Builder;

@Builder
public record FeedforwardValues(double p, double i, double d, double s, double v, double a, double g) {
    public FeedforwardValues(double p, double i, double d, double s, double v, double a) {
        this(p, i, d, s, v, a, 0.0);
    }

    public Slot0Configs getSlot0Configs() {
        return new Slot0Configs().withKP(p).withKI(i).withKD(d).withKS(s).withKV(v).withKA(a).withKG(g);
    }
}

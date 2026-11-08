package frc.lib.units;

public class MagneticFluxUnit extends Unit {
    public MagneticFluxUnit(double inBaseUnits) {
        super(inBaseUnits);
    }

    public MagneticFlux of(double quantity) {
        return new MagneticFlux(quantity * inBaseUnits);
    }

    public static final MagneticFluxUnit Webers = new MagneticFluxUnit(1.0);
}

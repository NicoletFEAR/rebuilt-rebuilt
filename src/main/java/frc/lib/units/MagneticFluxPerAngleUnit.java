package frc.lib.units;

public class MagneticFluxPerAngleUnit extends Unit {
    public MagneticFluxPerAngleUnit(double inBaseUnits) {
        super(inBaseUnits);
    }

    public MagneticFluxPerAngle of(double quantity) {
        return new MagneticFluxPerAngle(quantity * inBaseUnits);
    }

    public static final MagneticFluxPerAngleUnit WebersPerRadian = new MagneticFluxPerAngleUnit(1.0);
}

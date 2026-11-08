package frc.robot.subsystems.drive;

import static org.wpilib.units.Units.Radians;
import static org.wpilib.units.Units.Volts;

import frc.lib.module.position.AbsEncoderAngularPositionModule;
import frc.lib.motor.MotorIO;
import frc.lib.sensor.absencoder.AbsEncoderIO;
import org.littletonrobotics.junction.Logger;
import org.wpilib.math.geometry.Rotation2d;
import org.wpilib.units.measure.Angle;

class TurnMotor extends AbsEncoderAngularPositionModule {
    private TurnMotorState state;
    private Angle desiredPosition;

    TurnMotor(MotorIO motor, AbsEncoderIO canCoder, TurnMotorConfig config) {
        super("Drive/" + config.name() + " Turn", motor, canCoder, config.rotorToMechanismRatio());
        state = TurnMotorState.OFF;
        desiredPosition = Radians.of(0.0);
    }

    enum TurnMotorState {
        OFF,
        TURN,
    }

    @Override
    public void periodic() {
        super.periodic();
        Logger.recordOutput(name + "/State", state);
        Logger.recordOutput(name + "/DesiredPosition", desiredPosition);

        switch (state) {
            case OFF -> super.setVoltage(Volts.of(0.0));
            case TURN -> super.setPositionSetpoint(desiredPosition);
        }
    }

    void setState(TurnMotorState state) {
        this.state = state;
    }

    void setDesiredPosition(Angle position) {
        desiredPosition = position;
    }

    Rotation2d getDesiredRotation() {
        return Rotation2d.fromRadians(desiredPosition.in(Radians));
    }
}

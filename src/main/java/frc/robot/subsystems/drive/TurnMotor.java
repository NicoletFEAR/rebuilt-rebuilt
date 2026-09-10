package frc.robot.subsystems.drive;

import static edu.wpi.first.units.Units.Radians;
import static edu.wpi.first.units.Units.Volts;

import edu.wpi.first.units.measure.Angle;
import frc.lib.cancoder.CanCoderIO;
import frc.lib.module.CanCoderAngularPositionModule;
import frc.lib.motor.MotorIO;
import org.littletonrobotics.junction.Logger;

class TurnMotor extends CanCoderAngularPositionModule {
    private TurnMotorState state;
    private Angle desiredPosition;

    TurnMotor(MotorIO motor, CanCoderIO canCoder, TurnMotorConfig config) {
        super("Drive/" + config.name() + " Turn", motor, canCoder, config.rotorToMechanismRatio());
        state = TurnMotorState.OFF;
        desiredPosition = Radians.of(0.0);
    }

    private enum TurnMotorState {
        OFF,
        TURNING,
    }

    @Override
    public void periodic() {
        super.periodic();
        Logger.recordOutput(name + "/State", state);
        Logger.recordOutput(name + "/DesiredPosition", desiredPosition);

        switch (state) {
            case OFF -> super.setVoltage(Volts.of(0.0));
            case TURNING -> super.setPositionSetpoint(desiredPosition);
        }
    }

    void off() {
        state = TurnMotorState.OFF;
    }

    void turn() {
        state = TurnMotorState.TURNING;
    }

    void setDesiredPosition(Angle position) {
        desiredPosition = position;
    }
}

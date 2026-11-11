package frc.robot.subsystems.drive;

import static frc.lib.units.VoltageUnit.Volts;

import frc.lib.module.position.AbsEncoderAngularPositionModule;
import frc.lib.motor.MotorIO;
import frc.lib.sensor.absencoder.AbsEncoderIO;
import frc.lib.units.Angle;
import org.littletonrobotics.junction.Logger;

class TurnMotor extends AbsEncoderAngularPositionModule {
    private TurnMotorState state;

    TurnMotor(MotorIO motor, AbsEncoderIO canCoder, TurnMotorConfig config) {
        super("Drive/" + config.name() + " Turn", motor, canCoder, config.rotorToMechanismRatio());
        state = new TurnMotorState.Off();
    }

    sealed interface TurnMotorState permits TurnMotorState.Off, TurnMotorState.Turn {
        public record Off() implements TurnMotorState {}

        public record Turn(Angle position) implements TurnMotorState {}

        public static final Off OFF = new Off();
    }

    @Override
    public void periodic() {
        super.periodic();

        Logger.recordOutput(name + "/State", (Record) state);

        switch (state) {
            case TurnMotorState.Off() -> super.setVoltage(Volts.of(0.0));
            case TurnMotorState.Turn(Angle position) -> super.setPositionSetpoint(position);
        }
    }

    void setState(TurnMotorState state) {
        this.state = state;
    }
}

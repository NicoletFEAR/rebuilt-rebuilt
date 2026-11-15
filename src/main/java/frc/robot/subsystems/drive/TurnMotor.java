package frc.robot.subsystems.drive;

import static frc.lib.units.VoltageUnit.Volts;

import org.wpilib.math.geometry.Rotation2d;

import frc.lib.module.position.AbsEncoderAngularPositionModule;
import frc.lib.motor.MotorIO;
import frc.lib.sensor.absencoder.AbsEncoderIO;
import frc.lib.statemachine.State;
import frc.lib.statemachine.StateMachine;
import frc.lib.units.Angle;

class TurnMotor extends StateMachine<TurnMotor.TurnMotorState> {
    private AbsEncoderAngularPositionModule module;

    TurnMotor(MotorIO motor, AbsEncoderIO canCoder, TurnMotorConfig config) {
        super("Drive/" + config.name() + "/Turn", TurnMotorState.OFF);
        module = new AbsEncoderAngularPositionModule(name, motor, canCoder, config.rotorToMechanismRatio());
    }

    sealed interface TurnMotorState extends State permits TurnMotorState.Off, TurnMotorState.Turn {
        public record Off() implements TurnMotorState {}

        public record Turn(Angle position) implements TurnMotorState {}

        public static final Off OFF = new Off();
    }

    void periodic() {
        module.updateInputs();

        switch (state) {
            case TurnMotorState.Off() -> module.setVoltage(Volts.of(0.0));
            case TurnMotorState.Turn(Angle position) -> module.setPositionSetpoint(position);
        }

        logState();
    }

    Rotation2d getRotation() {
        return module.getRotation();
    }
}

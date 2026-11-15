package frc.robot.subsystems.drive;

import static frc.lib.units.VoltageUnit.Volts;

import frc.lib.module.velocity.LinearVelocityModule;
import frc.lib.motor.MotorIO;
import frc.lib.statemachine.State;
import frc.lib.statemachine.StateMachine;
import frc.lib.units.Distance;
import frc.lib.units.Velocity;

class DriveMotor extends StateMachine<DriveMotor.DriveMotorState> {
    private LinearVelocityModule module;

    DriveMotor(MotorIO motor, DriveMotorConfig config) {
        super("Drive/" + config.name() + "/Drive", DriveMotorState.OFF);
        module = new LinearVelocityModule(name, motor, config.rotorToMechanismRatio());
    }

    sealed interface DriveMotorState extends State permits DriveMotorState.Off, DriveMotorState.Drive {
        public record Off() implements DriveMotorState {}

        public record Drive(Velocity velocity) implements DriveMotorState {}

        public static final Off OFF = new Off();
    }

    void periodic() {
        module.updateInputs();

        switch (state) {
            case DriveMotorState.Off() -> module.setVoltage(Volts.of(0.0));
            case DriveMotorState.Drive(Velocity velocity) -> module.setVelocitySetpoint(velocity);
        }

        logState();
    }

    Distance getPosition() {
        return module.getPosition();
    }

    Velocity getVelocity() {
        return module.getVelocity();
    }
}

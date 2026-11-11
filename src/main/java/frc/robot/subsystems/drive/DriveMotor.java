package frc.robot.subsystems.drive;

import static frc.lib.units.VoltageUnit.Volts;

import frc.lib.module.velocity.LinearVelocityModule;
import frc.lib.motor.MotorIO;
import frc.lib.units.Velocity;
import org.littletonrobotics.junction.Logger;

class DriveMotor extends LinearVelocityModule {
    private DriveMotorState state;

    DriveMotor(MotorIO motor, DriveMotorConfig config) {
        super("Drive/" + config.name() + " Drive", motor, config.rotorToMechanismRatio());
        state = new DriveMotorState.Off();
    }

    sealed interface DriveMotorState permits DriveMotorState.Off, DriveMotorState.Drive {
        public record Off() implements DriveMotorState {}

        public record Drive(Velocity velocity) implements DriveMotorState {}

        public static final Off OFF = new Off();
    }

    @Override
    public void periodic() {
        super.periodic();

        Logger.recordOutput(name + "/State", (Record) state);

        switch (state) {
            case DriveMotorState.Off() -> super.setVoltage(Volts.of(0.0));
            case DriveMotorState.Drive(Velocity velocity) -> super.setVelocitySetpoint(velocity);
        }
    }

    void setState(DriveMotorState state) {
        this.state = state;
    }
}

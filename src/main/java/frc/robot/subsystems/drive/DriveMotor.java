package frc.robot.subsystems.drive;

import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.Volts;

import edu.wpi.first.units.measure.LinearVelocity;
import frc.lib.module.velocity.LinearVelocityModule;
import frc.lib.motor.MotorIO;
import org.littletonrobotics.junction.Logger;

class DriveMotor extends LinearVelocityModule {
    private DriveMotorState state;
    private LinearVelocity desiredVelocity;

    DriveMotor(MotorIO motor, DriveMotorConfig config) {
        super("Drive/" + config.name() + "Drive", motor, config.rotorToMechanismRatio());
        state = DriveMotorState.OFF;
        desiredVelocity = MetersPerSecond.of(0.0);
    }

    private enum DriveMotorState {
        OFF,
        DRIVING,
    }

    @Override
    public void periodic() {
        super.periodic();
        Logger.recordOutput(name + "/State", state);
        Logger.recordOutput(name + "/DesiredVelocity", desiredVelocity);

        switch (state) {
            case OFF -> super.setVoltage(Volts.of(0.0));
            case DRIVING -> super.setVelocitySetpoint(desiredVelocity);
        }
    }

    void off() {
        state = DriveMotorState.OFF;
    }

    void drive() {
        state = DriveMotorState.DRIVING;
    }

    void setDesiredVelocity(LinearVelocity velocity) {
        desiredVelocity = velocity;
    }
}

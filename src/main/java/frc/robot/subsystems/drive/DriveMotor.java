package frc.robot.subsystems.drive;

import static org.wpilib.units.Units.MetersPerSecond;
import static org.wpilib.units.Units.Volts;

import frc.lib.module.velocity.LinearVelocityModule;
import frc.lib.motor.MotorIO;
import org.littletonrobotics.junction.Logger;
import org.wpilib.units.measure.LinearVelocity;

class DriveMotor extends LinearVelocityModule {
    private DriveMotorState state;
    private LinearVelocity desiredVelocity;

    DriveMotor(MotorIO motor, DriveMotorConfig config) {
        super("Drive/" + config.name() + "Drive", motor, config.rotorToMechanismRatio());
        state = DriveMotorState.OFF;
        desiredVelocity = MetersPerSecond.of(0.0);
    }

    enum DriveMotorState {
        OFF,
        DRIVE,
    }

    @Override
    public void periodic() {
        super.periodic();
        Logger.recordOutput(name + "/State", state);
        Logger.recordOutput(name + "/DesiredVelocity", desiredVelocity);

        switch (state) {
            case OFF -> super.setVoltage(Volts.of(0.0));
            case DRIVE -> super.setVelocitySetpoint(desiredVelocity);
        }
    }

    void setState(DriveMotorState state) {
        this.state = state;
    }

    void setDesiredVelocity(LinearVelocity velocity) {
        desiredVelocity = velocity;
    }

    LinearVelocity getDesiredVelocity() {
        return desiredVelocity;
    }
}

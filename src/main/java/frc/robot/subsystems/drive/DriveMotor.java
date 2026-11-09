package frc.robot.subsystems.drive;

import static frc.lib.units.VelocityUnit.MetersPerSecond;
import static frc.lib.units.VoltageUnit.Volts;

import frc.lib.module.velocity.LinearVelocityModule;
import frc.lib.motor.MotorIO;
import frc.lib.units.Velocity;
import org.littletonrobotics.junction.Logger;

class DriveMotor extends LinearVelocityModule {
    private DriveMotorState state;
    private Velocity desiredVelocity;

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
        Logger.recordOutput(name + "/DesiredVelocity", desiredVelocity.in(MetersPerSecond));

        switch (state) {
            case OFF -> super.setVoltage(Volts.of(0.0));
            case DRIVE -> super.setVelocitySetpoint(desiredVelocity);
        }
    }

    void setState(DriveMotorState state) {
        this.state = state;
    }

    void setDesiredVelocity(Velocity velocity) {
        desiredVelocity = velocity;
    }

    Velocity getDesiredVelocity() {
        return desiredVelocity;
    }
}

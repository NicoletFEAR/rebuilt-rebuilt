package frc.robot.subsystems.drive;

import static frc.lib.units.VoltageUnit.Volts;

import frc.lib.module.velocity.LinearVelocityModule;
import frc.lib.motor.MotorConfig;
import frc.lib.motor.MotorIO;
import frc.lib.statemachine.State;
import frc.lib.statemachine.StateMachine;
import frc.lib.units.Distance;
import frc.lib.units.Velocity;
import frc.robot.subsystems.drive.Drive.DriveConstants;

/**
 * Drive motor component of a swerve module.
 *
 * <p>Wraps a {@link LinearVelocityModule} in a state machine that either holds zero voltage or
 * commands a linear velocity setpoint.
 */
final class DriveMotor extends StateMachine<DriveMotor.DriveMotorState> {
    private final LinearVelocityModule module;

    /**
     * Creates the drive motor component.
     *
     * @param name name of the associated swerve module (for differentiation from others in logs)
     */
    DriveMotor(SwerveModuleName name) {
        super("Drive/" + name + "/Drive", DriveMotorState.OFF);
        module =
                new LinearVelocityModule(
                        this.name,
                        MotorIO.buildMotor(
                                new MotorConfig(
                                        name.drive(),
                                        DriveConstants.DRIVE_SENSOR_TO_MECHANISM_RATIO,
                                        DriveConstants.DRIVE_FEEDFORWARD_VALUES)));
    }

    /** States of the drive motor. */
    static sealed interface DriveMotorState extends State
            permits DriveMotorState.Off, DriveMotorState.Drive {
        /** Drive motor is off (zero voltage). */
        static record Off() implements DriveMotorState {}

        /** Drive motor is commanded to a linear velocity setpoint. */
        static record Drive(Velocity velocity) implements DriveMotorState {}

        /** Shared constant for the {@link Off} record to avoid unnecessary allocations. */
        static final Off OFF = new Off();
    }

    /** Updates the motor inputs, applies the behavior for the current state, and logs the state. */
    void periodic() {
        module.updateInputs();

        switch (state) {
            case DriveMotorState.Off() -> module.setVoltage(Volts.of(0.0));
            case DriveMotorState.Drive(Velocity velocity) -> module.setDynamicVelocity(velocity);
        }

        logState();
    }

    /** Returns the current position of the drive mechanism. */
    Distance getPosition() {
        return module.getPosition();
    }

    /** Returns the current linear velocity of the drive mechanism. */
    Velocity getVelocity() {
        return module.getVelocity();
    }
}

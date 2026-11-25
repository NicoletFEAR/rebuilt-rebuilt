package frc.robot.subsystems.drivebase;

import static frc.lib.units.DistanceUnit.Meters;
import static frc.lib.units.VoltageUnit.Volts;

import frc.lib.module.velocity.LinearVelocityModule;
import frc.lib.motor.FeedforwardValues;
import frc.lib.motor.MotorConfig;
import frc.lib.motor.MotorIO;
import frc.lib.statemachine.State;
import frc.lib.statemachine.StateMachine;
import frc.lib.units.Distance;
import frc.lib.units.Velocity;
import frc.robot.subsystems.drivebase.Drivebase.DrivebaseConstants;

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
     * @param name which swerve module this drive motor is associated with
     */
    DriveMotor(SwerveModuleName name) {
        super("Drive/" + name + "/Drive", DriveMotorState.OFF);
        module =
                new LinearVelocityModule(
                        this.name,
                        MotorIO.buildMotor(
                                new MotorConfig(
                                        name.drive(),
                                        DriveMotorConstants.SENSOR_TO_MECHANISM_RATIO,
                                        DriveMotorConstants.FEEDFORWARD_VALUES)));
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

    /** Updates the motor inputs. */
    void updateInputs() {
        module.updateInputs();
    }

    /** Applies the behavior for the current state. */
    void execute() {
        switch (state) {
            case DriveMotorState.Off() -> module.setVoltage(Volts.of(0.0));
            case DriveMotorState.Drive(Velocity velocity) -> module.setDynamicVelocity(velocity);
        }
    }

    /** Logs any desired outputs from the drive motor. */
    void logOutputs() {
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

    /** Constants for the swerve module drive motors. */
    private static final class DriveMotorConstants {
        /** Sensor-to-mechanism ratio for the drive motors. */
        private static final double SENSOR_TO_MECHANISM_RATIO =
                425.0 / 63.0 / DrivebaseConstants.WHEEL_RADIUS.in(Meters);

        /** Feedforward and feedback gains for the drive motors. */
        private static final FeedforwardValues FEEDFORWARD_VALUES =
                new FeedforwardValues(0.1, 0.0, 0.0, 0.1, 0.1, 0.1);

        private DriveMotorConstants() {
            /* Keep this constructor empty. */
        }
    }
}

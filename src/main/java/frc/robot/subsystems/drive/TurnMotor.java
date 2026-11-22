package frc.robot.subsystems.drive;

import static frc.lib.units.VoltageUnit.Volts;

import frc.lib.module.position.AbsEncoderAngularPositionModule;
import frc.lib.motor.MotorConfig;
import frc.lib.motor.MotorIO;
import frc.lib.sensor.absencoder.AbsEncoderConfig;
import frc.lib.sensor.absencoder.AbsEncoderIO;
import frc.lib.statemachine.State;
import frc.lib.statemachine.StateMachine;
import frc.lib.units.Angle;
import frc.robot.subsystems.drive.Drive.DriveConstants;
import org.wpilib.math.geometry.Rotation2d;

/**
 * Turn (azimuth) motor component of a swerve module.
 *
 * <p>Wraps an {@link AbsEncoderAngularPositionModule} in a state machine that either holds zero
 * voltage or commands an angular position setpoint. Position feedback comes from the absolute
 * encoder when it is connected, falling back to the motor's relative position otherwise.
 */
final class TurnMotor extends StateMachine<TurnMotor.TurnMotorState> {
    private final AbsEncoderAngularPositionModule module;

    /**
     * Creates the turn motor component.
     *
     * @param name name of the associated swerve module (for differentiation from others in logs)
     */
    TurnMotor(SwerveModuleName name) {
        super("Drive/" + name + "/Turn", TurnMotorState.OFF);
        module =
                new AbsEncoderAngularPositionModule(
                        this.name,
                        MotorIO.buildMotor(
                                new MotorConfig(
                                        name.turn(),
                                        DriveConstants.TURN_SENSOR_TO_MECHANISM_RATIO,
                                        DriveConstants.TURN_FEEDFORWARD_VALUES)),
                        AbsEncoderIO.buildAbsEncoder(new AbsEncoderConfig(name.absEncoder(), name.offset())));
    }

    /** States of the turn motor. */
    static sealed interface TurnMotorState extends State
            permits TurnMotorState.Off, TurnMotorState.Turn {
        /** Turn motor is off (zero voltage). */
        static record Off() implements TurnMotorState {}

        /** Turn motor is commanded to an angular position setpoint. */
        static record Turn(Angle position) implements TurnMotorState {}

        /** Shared constant for the {@link Off} record to avoid unnecessary allocations. */
        static final Off OFF = new Off();
    }

    /**
     * Updates the motor and encoder inputs, applies the behavior for the current state, and logs the
     * state.
     */
    void periodic() {
        module.updateInputs();

        switch (state) {
            case TurnMotorState.Off() -> module.setVoltage(Volts.of(0.0));
            case TurnMotorState.Turn(Angle position) -> module.setDynamicPosition(position);
        }

        logState();
    }

    /** Returns the current rotation of the turn mechanism. */
    Rotation2d getRotation() {
        return module.getRotation();
    }
}

package frc.robot.subsystems.drivebase;

import static frc.lib.units.AngleUnit.Rotations;
import static frc.lib.units.VoltageUnit.Volts;

import frc.lib.module.position.AbsEncoderAngularPositionModule;
import frc.lib.motor.FeedforwardValues;
import frc.lib.motor.MotorConfig;
import frc.lib.motor.MotorIO;
import frc.lib.sensor.absencoder.AbsEncoderConfig;
import frc.lib.sensor.absencoder.AbsEncoderIO;
import frc.lib.statemachine.State;
import frc.lib.statemachine.StateMachine;
import frc.lib.units.Angle;
import java.util.HashMap;
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
                                        TurnMotorConstants.SENSOR_TO_MECHANISM_RATIO,
                                        TurnMotorConstants.FEEDFORWARD_VALUES)),
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

    /** Updates the motor and encoder inputs. */
    void updateInputs() {
        module.updateInputs();
    }

    /**
     * Applies the behavior for the current state.
     */
    void execute() {
        switch (state) {
            case TurnMotorState.Off() -> module.setVoltage(Volts.of(0.0));
            case TurnMotorState.Turn(Angle position) -> module.setDynamicPosition(position);
        }
    }

    /** Logs any desired outputs from the turn motor. */
    void logOutputs() {
        logState();
    }

    /** Returns the current rotation of the turn mechanism. */
    Rotation2d getRotation() {
        return module.getRotation();
    }

    /** Constants for the swerve module turn motors. */
    static final class TurnMotorConstants {
        /** Sensor-to-mechanism ratio for the turn motors. */
        private static final double SENSOR_TO_MECHANISM_RATIO = 150.0 / 7.0;

        /** Feedforward and feedback gains for the turn motors. */
        private static final FeedforwardValues FEEDFORWARD_VALUES =
                new FeedforwardValues(0.1, 0.0, 0.0, 0.1, 0.1, 0.1);

        /**
         * Absolute encoder offset for each turn mechanism. Use {@link SwerveModuleName#offset()}
         * instead of referencing this directly.
         */
        static final HashMap<SwerveModuleName, Angle> OFFSET = new HashMap<>();

        static {
            OFFSET.put(SwerveModuleName.FRONT_LEFT, Rotations.of(-0.8896484375));
            OFFSET.put(SwerveModuleName.FRONT_RIGHT, Rotations.of(-0.09716796875));
            OFFSET.put(SwerveModuleName.BACK_LEFT, Rotations.of(-0.721923828125));
            OFFSET.put(SwerveModuleName.BACK_RIGHT, Rotations.of(-0.37841796875));
        }

        private TurnMotorConstants() {
            /* Keep this constructor empty. */
        }
    }
}

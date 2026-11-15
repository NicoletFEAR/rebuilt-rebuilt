package frc.robot.subsystems.drive;

import static frc.lib.units.VoltageUnit.Volts;

import frc.lib.module.position.AbsEncoderAngularPositionModule;
import frc.lib.motor.MotorIO;
import frc.lib.sensor.absencoder.AbsEncoderIO;
import frc.lib.statemachine.State;
import frc.lib.statemachine.StateMachine;
import frc.lib.units.Angle;
import org.wpilib.math.geometry.Rotation2d;

/**
 * Turn (azimuth) motor component of a swerve module.
 *
 * <p>Wraps an {@link AbsEncoderAngularPositionModule} in a state machine that either holds zero
 * voltage or commands an angular position setpoint. Position feedback comes from the absolute
 * encoder when it is connected, falling back to the motor's relative position otherwise.
 */
class TurnMotor extends StateMachine<TurnMotor.TurnMotorState> {
    private AbsEncoderAngularPositionModule module;

    /**
     * Creates the turn motor component.
     *
     * @param motor interface used to communicate with the motor
     * @param canCoder interface used to communicate with the absolute encoder
     * @param config turn motor configuration
     */
    TurnMotor(MotorIO motor, AbsEncoderIO canCoder, TurnMotorConfig config) {
        super("Drive/" + config.name() + "/Turn", TurnMotorState.OFF);
        module =
                new AbsEncoderAngularPositionModule(name, motor, canCoder, config.rotorToMechanismRatio());
    }

    /** States of the turn motor. */
    static sealed interface TurnMotorState extends State
            permits TurnMotorState.Off, TurnMotorState.Turn {
        /** Turn motor is off (zero voltage). */
        static record Off() implements TurnMotorState {}

        /** Turn motor is commanded to an angular position setpoint. */
        static record Turn(Angle position) implements TurnMotorState {}

        /** Shared constant for the {@link Off} record. */
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
            case TurnMotorState.Turn(Angle position) -> module.setPositionSetpoint(position);
        }

        logState();
    }

    /** Returns the current rotation of the turn mechanism. */
    Rotation2d getRotation() {
        return module.getRotation();
    }
}

package frc.robot.subsystems.intake;

import static frc.lib.units.DistanceUnit.Meters;

import frc.lib.module.position.LinearPositionModule;
import frc.lib.motor.FeedforwardValues;
import frc.lib.motor.MotorConfig;
import frc.lib.motor.MotorIO;
import frc.lib.statemachine.State;
import frc.lib.statemachine.StateMachine;
import frc.lib.units.Distance;
import frc.robot.subsystems.DeviceIds;

/**
 * Intake arm that extends and retracts the intake.
 *
 * <p>The arm is a linear mechanism whose position is commanded with a position setpoint; see {@link
 * #periodic()} for the positions commanded by each state.
 */
final class Arm extends StateMachine<Arm.ArmState> {
    private final LinearPositionModule module;

    /** Creates the arm. */
    Arm() {
        super("Intake/Arm", ArmState.RETRACT);
        module =
                new LinearPositionModule(
                        name,
                        MotorIO.buildMotor(
                                new MotorConfig(
                                        DeviceIds.INTAKE_ARM,
                                        ArmConstants.SENSOR_TO_MECHANISM_RATIO,
                                        ArmConstants.FEEDFORWARD_VALUES)));
    }

    /** States of the arm. */
    enum ArmState implements State {
        RETRACT,
        DEPLOY,
    }

    /** Updates the motor inputs, applies the behavior for the current state, and logs the state. */
    void periodic() {
        module.updateInputs();

        switch (state) {
            case RETRACT -> module.setPositionSetpoint(Meters.of(0.0));
            case DEPLOY -> module.setPositionSetpoint(ArmConstants.DEPLOY_DISTANCE);
        }

        logState();
    }

    /** Constants for the intake arm. */
    private static final class ArmConstants {

        /** Feedforward and feedback gains for the arm motor. */
        private static final FeedforwardValues FEEDFORWARD_VALUES =
                new FeedforwardValues(25.0, 0.0, 0.0, 0.0, 0.0, 0.0);

        /** Rotor-to-mechanism ratio for the arm, in rotor radians per meter of extension. */
        private static final double SENSOR_TO_MECHANISM_RATIO = 40.0 / 3.0;

        /** Extension of the deployed arm position. */
        private static final Distance DEPLOY_DISTANCE = Meters.of(0.2);

        private ArmConstants() {
            /* Keep this constructor empty */
        }
    }
}

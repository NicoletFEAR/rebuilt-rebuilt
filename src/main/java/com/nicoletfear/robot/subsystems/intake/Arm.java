package com.nicoletfear.robot.subsystems.intake;

import static com.nicoletfear.lib.units.DistanceUnit.Meters;

import com.nicoletfear.lib.module.position.LinearPositionModule;
import com.nicoletfear.lib.motor.FeedforwardValues;
import com.nicoletfear.lib.motor.MotorConfig;
import com.nicoletfear.lib.motor.MotorIO;
import com.nicoletfear.lib.statemachine.State;
import com.nicoletfear.lib.statemachine.StateMachine;
import com.nicoletfear.lib.units.Distance;
import com.nicoletfear.robot.subsystems.DeviceIds;

/**
 * Intake arm that extends and retracts the intake.
 *
 * <p>The arm is a linear mechanism whose position is commanded with a position setpoint; see {@link
 * #execute()} for the positions commanded by each state.
 */
final class Arm extends StateMachine<Arm.ArmState> {
    private final LinearPositionModule module;

    /** Creates the arm. */
    Arm() {
        super("Intake/Arm", ArmState.RETRACT);
        module = new LinearPositionModule(
                name,
                MotorIO.buildMotor(new MotorConfig(
                        DeviceIds.INTAKE_ARM,
                        ArmConstants.SENSOR_TO_MECHANISM_RATIO,
                        ArmConstants.FEEDFORWARD_VALUES)));
    }

    /** States of the arm. */
    enum ArmState implements State {
        RETRACT,
        DEPLOY,
    }

    /** Updates the motor inputs. */
    void updateInputs() {
        module.updateInputs();
    }

    /** Applies the behavior for the current state. */
    void execute() {
        switch (state) {
            case RETRACT -> module.setPositionSetpoint(Meters.of(0.0));
            case DEPLOY -> module.setPositionSetpoint(ArmConstants.DEPLOY_DISTANCE);
        }
    }

    /** Logs any desired outputs from the arm. */
    void logOutputs() {
        logState();
    }

    /** Constants for the intake arm. */
    private static final class ArmConstants {

        /** Feedforward and feedback gains for the arm motor. */
        private static final FeedforwardValues FEEDFORWARD_VALUES = new FeedforwardValues(0.1, 0.0, 0.0, 0.1, 0.1, 0.1);

        /** Sensor-to-mechanism ratio for the arm, in rotor radians per meter of extension. */
        private static final double SENSOR_TO_MECHANISM_RATIO = 40.0 / 3.0;

        /** Extension of the deployed arm position. */
        private static final Distance DEPLOY_DISTANCE = Meters.of(0.2);

        private ArmConstants() {
            /* Keep this constructor empty */
        }
    }
}

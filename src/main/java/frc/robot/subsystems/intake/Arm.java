package frc.robot.subsystems.intake;

import static frc.lib.units.DistanceUnit.Meters;

import frc.lib.module.position.LinearPositionModule;
import frc.lib.motor.FeedforwardValues;
import frc.lib.motor.MotorIO;
import frc.lib.statemachine.State;
import frc.lib.statemachine.StateMachine;
import frc.lib.units.Distance;

public class Arm extends StateMachine<Arm.ArmState> {
    private LinearPositionModule module;

    Arm(MotorIO motor) {
        super("Intake/Arm", ArmState.RETRACT);
        module = new LinearPositionModule(name, motor, ArmConstants.ROTOR_TO_MECHANISM_RATIO);
    }

    enum ArmState implements State {
        RETRACT,
        DEPLOY,
    }

    public void periodic() {
        module.updateInputs();

        switch (state) {
            case RETRACT -> module.setPositionSetpoint(Meters.of(0.0));
            case DEPLOY -> module.setPositionSetpoint(ArmConstants.DEPLOY_DISTANCE);
        }

        logState();
    }

    public static final class ArmConstants {
        public static final FeedforwardValues FEEDFORWARD_VALUES =
                new FeedforwardValues(25.0, 0.0, 0.0, 0.0, 0.0, 0.0);

        private static final double ROTOR_TO_MECHANISM_RATIO = 40.0 / 3.0;

        private static final Distance DEPLOY_DISTANCE = Meters.of(0.2);

        private ArmConstants() {
            /* Keep this constructor empty */
        }
    }
}

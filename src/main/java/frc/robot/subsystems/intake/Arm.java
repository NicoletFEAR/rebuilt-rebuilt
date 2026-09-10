package frc.robot.subsystems.intake;

import static edu.wpi.first.units.Units.Meters;

import edu.wpi.first.units.measure.Distance;
import frc.lib.module.position.LinearPositionModule;
import frc.lib.motor.FeedforwardValues;
import frc.lib.motor.MotorIO;
import org.littletonrobotics.junction.Logger;

public class Arm extends LinearPositionModule {
    private ArmState state;

    Arm(MotorIO motor) {
        super("Intake/Arm", motor, ArmConstants.ROTOR_TO_MECHANISM_RATIO);
        state = ArmState.RETRACTING;
    }

    private enum ArmState {
        RETRACTING,
        DEPLOYING,
    }

    @Override
    public void periodic() {
        super.periodic();
        Logger.recordOutput(name + "/State", state);

        switch (state) {
            case RETRACTING -> super.setPositionSetpoint(Meters.of(0.0));
            case DEPLOYING -> super.setPositionSetpoint(ArmConstants.DEPLOY_DISTANCE);
        }
    }

    void retract() {
        state = ArmState.RETRACTING;
    }

    void deploy() {
        state = ArmState.DEPLOYING;
    }

    public static final class ArmConstants {
        public static final FeedforwardValues FEEDFORWARD_VALUES =
                new FeedforwardValues(25.0, 0.0, 0.0, 0.0, 0.0, 0.0);

        private static final double ROTOR_TO_MECHANISM_RATIO = 40.0 / 3.0;

        private static final Distance DEPLOY_DISTANCE = Meters.of(0.2);

        private ArmConstants() {/* Keep this constructor empty */}
    }
}

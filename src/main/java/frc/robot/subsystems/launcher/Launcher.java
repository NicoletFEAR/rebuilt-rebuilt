package frc.robot.subsystems.launcher;

import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.cancoder.CanCoderIO;
import frc.lib.motor.MotorIO;
import org.littletonrobotics.junction.Logger;

public class Launcher extends SubsystemBase {
    private final Flywheels flywheels;
    private final Indexer indexer;
    private final Hood hood;

    private LauncherState state;

    public Launcher(
            MotorIO leftFlywheelMotor,
            MotorIO rightFlywheelMotor,
            MotorIO indexMotor,
            MotorIO hoodMotor,
            CanCoderIO hoodCanCoder) {
        flywheels = new Flywheels(leftFlywheelMotor, new MotorIO[] {rightFlywheelMotor});
        indexer = new Indexer(indexMotor);
        hood = new Hood(hoodMotor, hoodCanCoder);

        state = LauncherState.OFF;
    }

    private enum LauncherState {
        OFF,
        FLYWHEEL_IDLE,
        SPINNING_UP,
        LAUNCHING,
    }

    @Override
    public void periodic() {
        Logger.recordOutput("Launcher/State", state);

        switch (state) {
            case OFF -> {
                flywheels.off();
                indexer.off();
                hood.off();
            }

            case FLYWHEEL_IDLE -> {
                flywheels.idle();
            }

            case SPINNING_UP -> {
                flywheels.launch();
                indexer.off();
                hood.holdPosition();
            }

            case LAUNCHING -> {
                flywheels.launch();
                indexer.index();
                hood.holdPosition();
            }
        }
    }

    public void off() {
        state = LauncherState.OFF;
    }

    public void flywheelIdle() {
        state = LauncherState.FLYWHEEL_IDLE;
    }

    public void spinUp() {
        state = LauncherState.SPINNING_UP;
    }

    public void launch() {
        state = LauncherState.LAUNCHING;
    }

    public void setLaunchParameters(AngularVelocity flywheelVelocity, Angle hoodAngle) {
        flywheels.setDesiredVelocity(flywheelVelocity);
        hood.setDesiredPosition(hoodAngle);
    }

    public boolean isReadyToLaunch() {
        return flywheels.isAtDesiredVelocity() && hood.isAtDesiredPosition();
    }
}

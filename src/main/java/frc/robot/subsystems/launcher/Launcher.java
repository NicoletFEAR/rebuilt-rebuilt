package frc.robot.subsystems.launcher;

import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.cancoder.CanCoderIO;
import frc.lib.motor.MotorIO;
import frc.robot.subsystems.launcher.Flywheels.FlywheelState;
import frc.robot.subsystems.launcher.Hood.HoodState;
import frc.robot.subsystems.launcher.Indexer.IndexerState;

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
        flywheels = new Flywheels(leftFlywheelMotor, new MotorIO[] { rightFlywheelMotor });
        indexer = new Indexer(indexMotor);
        hood = new Hood(hoodMotor, hoodCanCoder);

        state = LauncherState.OFF;
    }

    public enum LauncherState {
        OFF,
        IDLE,
        SPIN_UP,
        LAUNCH,
        EXTAKE,
    }

    @Override
    public void periodic() {
        Logger.recordOutput("Launcher/State", state);

        switch (state) {
            case OFF -> {
                flywheels.setState(FlywheelState.OFF);
                indexer.setState(IndexerState.OFF);
                hood.setState(HoodState.OFF);
            }

            case IDLE -> {
                flywheels.setState(FlywheelState.IDLE);
            }

            case SPIN_UP -> {
                flywheels.setState(FlywheelState.LAUNCH);
                indexer.setState(IndexerState.OFF);
                hood.setState(HoodState.HOLD_POSITION);
            }

            case LAUNCH -> {
                flywheels.setState(FlywheelState.LAUNCH);
                indexer.setState(IndexerState.INDEX);
                hood.setState(HoodState.HOLD_POSITION);
            }

            case EXTAKE -> {
                flywheels.setState(FlywheelState.IDLE);
                indexer.setState(IndexerState.EXTAKE);
                hood.setState(HoodState.OFF);
            }
        }
    }

    public void setState(LauncherState state) {
        this.state = state;
    }

    public void setLaunchParameters(AngularVelocity flywheelVelocity, Angle hoodAngle) {
        flywheels.setDesiredVelocity(flywheelVelocity);
        hood.setDesiredPosition(hoodAngle);
    }

    public boolean isReadyToLaunch() {
        return flywheels.isAtDesiredVelocity() && hood.isAtDesiredPosition();
    }
}

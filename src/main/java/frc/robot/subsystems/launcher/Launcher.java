package frc.robot.subsystems.launcher;

import frc.lib.motor.MotorIO;
import frc.lib.sensor.absencoder.AbsEncoderIO;
import frc.lib.units.Angle;
import frc.lib.units.AngularVelocity;
import frc.robot.subsystems.launcher.Flywheels.FlywheelState;
import frc.robot.subsystems.launcher.Hood.HoodState;
import frc.robot.subsystems.launcher.Indexer.IndexerState;
import org.littletonrobotics.junction.Logger;

public class Launcher {
    private final Flywheels flywheels;
    private final Indexer indexer;
    private final Hood hood;

    private LauncherState state;

    public Launcher(
            MotorIO leftFlywheelMotor,
            MotorIO rightFlywheelMotor,
            MotorIO indexMotor,
            MotorIO hoodMotor,
            AbsEncoderIO hoodCanCoder) {
        flywheels = new Flywheels(leftFlywheelMotor, new MotorIO[] {rightFlywheelMotor});
        indexer = new Indexer(indexMotor);
        hood = new Hood(hoodMotor, hoodCanCoder);

        state = LauncherState.OFF;
    }

    public sealed interface LauncherState permits LauncherState.Off, LauncherState.Idle, LauncherState.SpinUp, LauncherState.Launch, LauncherState.Extake {
        public record Off() implements LauncherState {}
        public record Idle() implements LauncherState {}
        public record SpinUp(AngularVelocity flywheelVelocity, Angle hoodAngle) implements LauncherState {}
        public record Launch(AngularVelocity flywheelVelocity, Angle hoodAngle) implements LauncherState {}
        public record Extake() implements LauncherState {}

        public static final Off OFF = new Off();
        public static final Idle IDLE = new Idle();
        public static final Extake EXTAKE = new Extake();
    }

    public void periodic() {
        Logger.recordOutput("Launcher/State", (Record) state);

        switch (state) {
            case LauncherState.Off() -> {
                flywheels.setState(FlywheelState.OFF);
                indexer.setState(IndexerState.OFF);
                hood.setState(HoodState.OFF);
            }

            case LauncherState.Idle() -> {
                flywheels.setState(FlywheelState.IDLE);
                indexer.setState(IndexerState.OFF);
                hood.setState(HoodState.OFF);
            }

            case LauncherState.SpinUp(AngularVelocity flywheelVelocity, Angle hoodAngle) -> {
                flywheels.setState(new FlywheelState.Launch(flywheelVelocity));
                indexer.setState(IndexerState.OFF);
                hood.setState(new HoodState.HoldPosition(hoodAngle));
            }

            case LauncherState.Launch(AngularVelocity flywheelVelocity, Angle hoodAngle) -> {
                flywheels.setState(new FlywheelState.Launch(flywheelVelocity));
                indexer.setState(IndexerState.INDEX);
                hood.setState(new HoodState.HoldPosition(hoodAngle));
            }

            case LauncherState.Extake() -> {
                flywheels.setState(FlywheelState.IDLE);
                indexer.setState(IndexerState.EXTAKE);
                hood.setState(HoodState.OFF);
            }
        }

        flywheels.periodic();
        indexer.periodic();
        hood.periodic();
    }

    public void setState(LauncherState state) {
        this.state = state;
    }

    public boolean isReadyToLaunch() {
        return flywheels.isAtLaunchVelocity() && hood.isAtLaunchPosition();
    }
}

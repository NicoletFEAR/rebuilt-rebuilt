package frc.robot.subsystems.launcher;

import frc.lib.motor.MotorConfig;
import frc.lib.sensor.absencoder.AbsEncoderConfig;
import frc.lib.statemachine.State;
import frc.lib.statemachine.StateMachine;
import frc.robot.subsystems.launcher.Flywheels.FlywheelState;
import frc.robot.subsystems.launcher.Hood.HoodState;
import frc.robot.subsystems.launcher.Indexer.IndexerState;

/**
 * Launcher subsystem: two flywheel motors, an indexer, and a hood.
 *
 * <p>Each launcher state maps to a set of flywheel, indexer, and hood states; see {@link
 * #periodic()} for the current mapping. The right flywheel motor is configured as a follower of the
 * left flywheel motor (with the alignment taken from the follower's configuration).
 */
public final class Launcher extends StateMachine<Launcher.LauncherState> {
    private final Flywheels flywheels;
    private final Indexer indexer;
    private final Hood hood;

    /**
     * Creates the launcher.
     *
     * @param leftFlywheelConfig configuration for the left flywheel motor
     * @param rightFlywheelConfig configuration for the right flywheel motor; needs an alignment value
     *     so it can follow the left flywheel motor
     * @param indexConfig configuration for the indexer motor
     * @param hoodConfig configuration for the hood motor
     * @param hoodAbsEncoderConfig configuration for the hood absolute encoder
     */
    public Launcher(
            MotorConfig leftFlywheelConfig,
            MotorConfig rightFlywheelConfig,
            MotorConfig indexConfig,
            MotorConfig hoodConfig,
            AbsEncoderConfig hoodAbsEncoderConfig) {
        super("Launcher", LauncherState.OFF);
        flywheels = new Flywheels(leftFlywheelConfig, new MotorConfig[] {rightFlywheelConfig});
        indexer = new Indexer(indexConfig);
        hood = new Hood(hoodConfig, hoodAbsEncoderConfig);
    }

    /** States of the launcher. */
    public static sealed interface LauncherState extends State
            permits LauncherState.Off,
                    LauncherState.Idle,
                    LauncherState.SpinUp,
                    LauncherState.Launch,
                    LauncherState.Extake {

        /** Launcher is off; flywheels, indexer, and hood receive zero voltage. */
        static record Off() implements LauncherState {}

        /** Flywheels are spinning at the idle velocity; indexer and hood are off. */
        static record Idle() implements LauncherState {}

        /** Flywheels and hood are set up to launch, but the indexer is off. */
        static record SpinUp(LaunchParameters parameters)
                implements LauncherState {}

        /** Flywheels and hood are set up to launch, and the indexer feeds game pieces. */
        static record Launch(LaunchParameters parameters)
                implements LauncherState {}

        /** Flywheels idle, indexer runs in reverse, and the hood is off. */
        static record Extake() implements LauncherState {}

        /** Shared constant for the {@link Off} record to avoid unnecessary allocations. */
        static final Off OFF = new Off();

        /** Shared constant for the {@link Idle} record to avoid unnecessary allocations. */
        static final Idle IDLE = new Idle();

        /** Shared constant for the {@link Extake} record to avoid unnecessary allocations. */
        static final Extake EXTAKE = new Extake();
    }

    /**
     * Logs the launcher state, applies the current state to the flywheels, indexer, and hood, and
     * runs their periodic logic.
     */
    public void periodic() {
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

            case LauncherState.SpinUp(LaunchParameters parameters) -> {
                flywheels.setState(new FlywheelState.Launch(parameters.flywheelVelocity()));
                indexer.setState(IndexerState.OFF);
                hood.setState(new HoodState.HoldPosition(parameters.hoodAngle()));
            }

            case LauncherState.Launch(LaunchParameters parameters) -> {
                flywheels.setState(new FlywheelState.Launch(parameters.flywheelVelocity()));
                indexer.setState(IndexerState.INDEX);
                hood.setState(new HoodState.HoldPosition(parameters.hoodAngle()));
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
        logState();
    }

    /**
     * Returns whether the flywheels are at the launch velocity and the hood is at the launch
     * position.
     */
    public boolean isReadyToLaunch() {
        return flywheels.isAtLaunchVelocity() && hood.isAtLaunchPosition();
    }
}

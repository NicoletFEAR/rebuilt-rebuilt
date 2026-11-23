package frc.robot.subsystems.launcher;

import frc.lib.statemachine.State;
import frc.lib.statemachine.StateMachine;
import frc.robot.subsystems.launcher.Flywheels.FlywheelState;
import frc.robot.subsystems.launcher.Hood.HoodState;
import frc.robot.subsystems.launcher.Indexer.IndexerState;

/**
 * Launcher subsystem: two flywheel motors, an indexer, and a hood.
 *
 * <p>Each launcher state maps to a set of flywheel, indexer, and hood states; see {@link
 * #execute()} for the current mapping. The right flywheel motor is configured as a follower of the
 * left flywheel motor (with the alignment taken from the follower's configuration).
 */
public final class Launcher extends StateMachine<Launcher.LauncherState> {
    private final Flywheels flywheels = new Flywheels();
    private final Indexer indexer = new Indexer();
    private final Hood hood = new Hood();

    /** Creates the launcher. */
    public Launcher() {
        super("Launcher", LauncherState.OFF);
    }

    /** States of the launcher. */
    public static sealed interface LauncherState extends State
            permits LauncherState.Off,
                    LauncherState.Idle,
                    LauncherState.Launch,
                    LauncherState.Extake,
                    TransitionalLauncherState {

        /** Launcher is off; flywheels, indexer, and hood receive zero voltage. */
        static record Off() implements LauncherState {}

        /** Flywheels are spinning at the idle velocity; indexer and hood are off. */
        static record Idle() implements LauncherState {}

        /** Flywheels and hood are set up to launch, and the indexer feeds game pieces. */
        static record Launch(LaunchParameters parameters) implements LauncherState {}

        /** Flywheels idle, indexer runs in reverse, and the hood is off. */
        static record Extake() implements LauncherState {}

        /** Shared constant for the {@link Off} record to avoid unnecessary allocations. */
        static final Off OFF = new Off();

        /** Shared constant for the {@link Idle} record to avoid unnecessary allocations. */
        static final Idle IDLE = new Idle();

        /** Shared constant for the {@link Extake} record to avoid unnecessary allocations. */
        static final Extake EXTAKE = new Extake();
    }

    /** Internal transitional states of the launcher. */
    private static sealed interface TransitionalLauncherState extends LauncherState
            permits TransitionalLauncherState.SpinUp {
        /** Flywheels and hood are set to launch, and the indexer waits for them to be ready. */
        static record SpinUp(LaunchParameters parameters) implements TransitionalLauncherState {}
    }

    /** Updates the inputs from the devices associated with the launcher. */
    public void updateInputs() {
        flywheels.updateInputs();
        indexer.updateInputs();
        hood.updateInputs();
    }

    /** Determines actual launcher state, then sets the states of the flywheels, indexer, and hood. */
    public void applyState() {
        if (state instanceof LauncherState.Launch(LaunchParameters parameters) && !isReadyToLaunch()) {
            state = new TransitionalLauncherState.SpinUp(parameters);
        }

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

            case TransitionalLauncherState.SpinUp(LaunchParameters parameters) -> {
                flywheels.setState(new FlywheelState.Launch(parameters.flywheelVelocity()));
                indexer.setState(IndexerState.OFF);
                hood.setState(new HoodState.HoldPosition(parameters.hoodAngle()));
            }
        }
    }

    /** Applies the current state to the flywheels, indexer, and hood. */
    public void execute() {
        flywheels.execute();
        indexer.execute();
        hood.execute();
    }

    /** Logs any desired outputs from the launcher. */
    public void logOutputs() {
        logState();
        flywheels.logOutputs();
        indexer.logOutputs();
        hood.logOutputs();
    }

    /**
     * Returns whether the flywheels are at the launch velocity and the hood is at the launch
     * position.
     */
    private boolean isReadyToLaunch() {
        return flywheels.isAtLaunchVelocity() && hood.isAtLaunchPosition();
    }
}

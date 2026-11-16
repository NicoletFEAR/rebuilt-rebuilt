package frc.robot.subsystems.launcher;

import frc.lib.motor.MotorIO;
import frc.lib.sensor.absencoder.AbsEncoderIO;
import frc.lib.statemachine.State;
import frc.lib.statemachine.StateMachine;
import frc.lib.units.Angle;
import frc.lib.units.AngularVelocity;
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
     * @param leftFlywheelMotor interface used to communicate with the left flywheel motor
     * @param rightFlywheelMotor interface used to communicate with the right flywheel motor;
     *     configured to follow the left flywheel motor
     * @param indexMotor interface used to communicate with the indexer motor
     * @param hoodMotor interface used to communicate with the hood motor
     * @param hoodCanCoder interface used to communicate with the hood absolute encoder
     */
    public Launcher(
            MotorIO leftFlywheelMotor,
            MotorIO rightFlywheelMotor,
            MotorIO indexMotor,
            MotorIO hoodMotor,
            AbsEncoderIO hoodCanCoder) {
        super("Launcher", LauncherState.OFF);
        flywheels = new Flywheels(leftFlywheelMotor, new MotorIO[] {rightFlywheelMotor});
        indexer = new Indexer(indexMotor);
        hood = new Hood(hoodMotor, hoodCanCoder);
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
        static record SpinUp(AngularVelocity flywheelVelocity, Angle hoodAngle)
                implements LauncherState {}

        /** Flywheels and hood are set up to launch, and the indexer feeds game pieces. */
        static record Launch(AngularVelocity flywheelVelocity, Angle hoodAngle)
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

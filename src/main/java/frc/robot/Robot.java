package frc.robot;

import com.ctre.phoenix6.SignalLogger;
import frc.robot.constants.BuildConstants;
import org.littletonrobotics.junction.LoggedRobot;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.NT4Publisher;
import org.littletonrobotics.junction.wpilog.WPILOGWriter;
import org.wpilib.hardware.hal.AllianceStationID;
import org.wpilib.simulation.DriverStationSim;
import org.wpilib.simulation.RoboRioSim;

/**
 * Main robot class, extending AdvantageKit's {@link LoggedRobot}.
 *
 * <p>Responsible for initializing logging (with build metadata from the generated BuildConstants
 * class), setting up the simulated driver station when running in simulation, and constructing the
 * {@link RobotContainer} that wires together the subsystems.
 */
public final class Robot extends LoggedRobot {
    private final RobotContainer robotContainer;

    /**
     * Creates the robot: initializes logging, configures the simulation driver station if running in
     * simulation, and constructs the robot container.
     */
    public Robot() {
        initLogging();

        if (isSimulation()) {
            setUpSimulation();
        }

        robotContainer = new RobotContainer();
    }

    /** Records build metadata and system tags with AdvantageKit's logger. */
    private void initLogging() {
        Logger.recordMetadata("ProjectGroup", BuildConstants.PROJECT_GROUP);
        Logger.recordMetadata("ProjectName", BuildConstants.PROJECT_NAME);
        Logger.recordMetadata("ProjectVersion", BuildConstants.PROJECT_VERSION);
        Logger.recordMetadata("GitShaShort", BuildConstants.GIT_SHA_SHORT);
        Logger.recordMetadata("GitSha", BuildConstants.GIT_SHA);
        Logger.recordMetadata("GitDate", BuildConstants.GIT_DATE);
        Logger.recordMetadata("GitBranch", BuildConstants.GIT_BRANCH);
        Logger.recordMetadata("BuildDate", BuildConstants.BUILD_DATE);
        Logger.recordMetadata("BuildUnixTime", Long.toString(BuildConstants.BUILD_UNIX_TIME));
        Logger.recordMetadata("Status", BuildConstants.STATUS);

        if (isReal()) {
            Logger.addDataReceiver(new WPILOGWriter());
        }

        // Disable Hoot logger
        SignalLogger.enableAutoLogging(false);
        Logger.addDataReceiver(new NT4Publisher());
        Logger.start();
    }

    /** Sets the simulated team number and driver station alliance for simulation runs. */
    private void setUpSimulation() {
        RoboRioSim.setTeamNumber(4786);
        DriverStationSim.setAllianceStationId(AllianceStationID.BLUE_1);
        DriverStationSim.notifyNewData();
    }

    /** Runs the robot container's periodic logic. */
    @Override
    public void robotPeriodic() {
        robotContainer.periodic();
    }

    /** Runs while the robot is disabled; intentionally empty. */
    @Override
    public void disabledPeriodic() {}

    /** Runs while the robot is in teleop; subsystem logic is driven from {@link #robotPeriodic()}. */
    @Override
    public void teleopPeriodic() {}

    /** Runs while in simulation; intentionally empty. */
    @Override
    public void simulationPeriodic() {}
}

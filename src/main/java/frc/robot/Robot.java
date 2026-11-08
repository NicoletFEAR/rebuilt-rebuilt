package frc.robot;

import frc.robot.constants.BuildConstants;
import org.littletonrobotics.junction.LoggedRobot;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.NT4Publisher;
import org.littletonrobotics.junction.wpilog.WPILOGWriter;
import org.wpilib.hardware.hal.AllianceStationID;
import org.wpilib.simulation.DriverStationSim;
import org.wpilib.simulation.RoboRioSim;

public class Robot extends LoggedRobot {
    private final RobotContainer robotContainer;

    public Robot() {
        initLogging();

        if (isSimulation()) {
            setUpSimulation();
        }

        robotContainer = new RobotContainer();
    }

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

        Logger.addDataReceiver(new NT4Publisher());
        Logger.start();
    }

    private void setUpSimulation() {
        RoboRioSim.setTeamNumber(4786);
        DriverStationSim.setAllianceStationId(AllianceStationID.BLUE_1);
        DriverStationSim.notifyNewData();
    }

    @Override
    public void robotPeriodic() {
        robotContainer.periodic();
    }
}

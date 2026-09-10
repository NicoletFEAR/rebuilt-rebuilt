package frc.robot;

import edu.wpi.first.hal.AllianceStationID;
import edu.wpi.first.math.MathShared;
import edu.wpi.first.math.MathSharedStore;
import edu.wpi.first.math.MathUsageId;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.constants.BuildConstants;
import org.littletonrobotics.junction.LoggedRobot;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.NT4Publisher;
import org.littletonrobotics.junction.wpilog.WPILOGWriter;

public class Robot extends LoggedRobot {
    @SuppressWarnings("unused")
    private final RobotContainer robotContainer;

    public Robot() {
        initLogging();
        silenceMeaninglessErrors();
        setUpSimulation();

        robotContainer = new RobotContainer();
    }

    private void initLogging() {
        Logger.recordMetadata("ProjectName", BuildConstants.MAVEN_NAME);
        Logger.recordMetadata("BuildDate", BuildConstants.BUILD_DATE);
        Logger.recordMetadata("GitSHA", BuildConstants.GIT_SHA);
        Logger.recordMetadata("GitDate", BuildConstants.GIT_DATE);
        Logger.recordMetadata("GitBranch", BuildConstants.GIT_BRANCH);

        if (isReal()) {
            Logger.addDataReceiver(new WPILOGWriter());
        }

        Logger.addDataReceiver(new NT4Publisher());
        Logger.start();
    }

    private void silenceMeaninglessErrors() {
        DriverStation.silenceJoystickConnectionWarning(true);
        MathShared mathShared = MathSharedStore.getMathShared();

        MathSharedStore.setMathShared(
                new MathShared() {
                    @Override
                    public void reportError(String error, StackTraceElement[] stackTrace) {
                        if (error.startsWith("x and y components of Rotation2d are zero")) {
                            return;
                        }
                        mathShared.reportError(error, stackTrace);
                    }

                    @Override
                    public void reportUsage(MathUsageId id, int count) {
                        mathShared.reportUsage(id, count);
                    }

                    @Override
                    public double getTimestamp() {
                        return mathShared.getTimestamp();
                    }
                });
    }

    private void setUpSimulation() {
        DriverStationSim.setAllianceStationId(AllianceStationID.Blue1);
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
    }
}

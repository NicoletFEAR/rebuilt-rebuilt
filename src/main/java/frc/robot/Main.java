package frc.robot;

import org.wpilib.framework.RobotBase;

/** Entry point of the robot program. */
public final class Main {
    private Main() {}

    /** Starts the robot by constructing a {@link Robot} and passing it to {@link RobotBase}. */
    public static void main(String... args) {
        RobotBase.startRobot(Robot::new);
    }
}

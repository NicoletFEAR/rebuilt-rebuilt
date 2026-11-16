package frc.robot;

import org.wpilib.framework.RobotBase;

/** Entry point of the robot program. */
public final class Main {

    /** Starts the robot by constructing a {@link Robot} and passing it to {@link RobotBase}. */
    public static void main(String... args) {
        RobotBase.startRobot(Robot::new);
    }

    private Main() {
        // Keep this constructor empty.
    }
}

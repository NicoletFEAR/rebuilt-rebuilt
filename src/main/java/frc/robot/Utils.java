package frc.robot;

import org.wpilib.math.geometry.Rotation2d;

// I don't like having a Utils class, but there is nowhere else to put this method
// Eventually, it will be refactored somewhere else so that this class can be
// deleted. Don't add anything here unless there is literally nowhere else to
// put it.

public final class Utils {
    public static Rotation2d zeroSafeRotation2d(double x, double y) {
        return x == 0 && y == 0 ? Rotation2d.ZERO : new Rotation2d(x, y);
    }

    private Utils() {}
}

package frc.robot;

import org.wpilib.math.geometry.Rotation2d;

// I don't like having a Utils class, but there is nowhere else to put this method
// Eventually, it will be refactored somewhere else so that this class can be
// deleted. Don't add anything here unless there is literally nowhere else to
// put it.

/** Utility methods for robot code. */
public final class Utils {

    /**
     * Creates a {@link Rotation2d} from the given vector components, returning {@link
     * Rotation2d#ZERO} when both components are zero.
     *
     * @param x the x component of the vector
     * @param y the y component of the vector
     * @return a rotation of (x, y), or {@link Rotation2d#ZERO} if {@code x} and {@code y} are both
     *     zero
     */
    public static Rotation2d zeroSafeRotation2d(double x, double y) {
        return x == 0 && y == 0 ? Rotation2d.ZERO : new Rotation2d(x, y);
    }

    private Utils() {}
}

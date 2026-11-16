package frc.lib.module.position;

import frc.lib.motor.MotorIO;
import frc.lib.motor.MotorIOInputsAutoLogged;
import org.littletonrobotics.junction.Logger;

/**
 * A linear position module that controls a leader motor and one or more follower motors.
 *
 * <p>This module extends {@link LinearPositionModule} and uses one motor as the leader while
 * configuring the provided follower motors to follow it. Follower motor inputs are also updated and
 * logged periodically.
 *
 * <p>Each follower must have an {@code alignment} value configured in its motor configuration. The
 * alignment determines how the follower should operate relative to the leader.
 */
public final class MultiLinearPositionModule extends LinearPositionModule {

    /** Number of follower motors controlled by this module. */
    private final int numberOfFollowers;

    /** Motor IO interfaces for the follower motors. */
    private final MotorIO[] followerIOs;

    /** Logged inputs for each follower motor. */
    private final MotorIOInputsAutoLogged[] followerInputs;

    /**
     * Creates a multi-motor linear position module.
     *
     * <p>The provided follower motors are configured to follow the leader motor. Each follower must
     * have an {@code alignment} value defined in its configuration.
     *
     * @param name name used to identify and log this module
     * @param leader motor IO interface for the leader motor
     * @param followers motor IO interfaces for the follower motors
     * @param rotorToMechanismRatio ratio between the motor rotor and the mechanism
     * @throws IllegalArgumentException if a follower does not have an {@code alignment} value
     *     configured
     */
    public MultiLinearPositionModule(
            String name, MotorIO leader, MotorIO[] followers, double rotorToMechanismRatio) {
        super(name, leader, rotorToMechanismRatio);
        numberOfFollowers = followers.length;
        followerIOs = followers;
        followerInputs = new MotorIOInputsAutoLogged[numberOfFollowers];

        for (int i = 0; i < numberOfFollowers; i++) {
            followerIOs[i].follow(
                    leader,
                    followerIOs[i]
                            .getConfig()
                            .alignment()
                            .orElseThrow(
                                    () ->
                                            new IllegalArgumentException(
                                                    "Every follower config must have the `alignment` field set")));
            followerInputs[i] = new MotorIOInputsAutoLogged();
        }
    }

    /**
     * Updates and logs the inputs for the leader and all follower motors.
     *
     * <p>The leader motor inputs are updated by the parent implementation. This method additionally
     * updates each follower's inputs and logs them.
     */
    @Override
    public void updateInputs() {
        super.updateInputs();

        for (int i = 0; i < numberOfFollowers; i++) {
            followerIOs[i].updateInputs(followerInputs[i]);
            Logger.processInputs(
                    name + "/Follower" + (numberOfFollowers == 1 ? "" : " " + (i + 1)), followerInputs[i]);
        }
    }
}

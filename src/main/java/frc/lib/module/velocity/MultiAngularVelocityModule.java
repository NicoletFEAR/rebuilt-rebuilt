package frc.lib.module.velocity;

import frc.lib.motor.MotorIO;
import frc.lib.motor.MotorIOInputsAutoLogged;
import org.littletonrobotics.junction.Logger;

/**
 * An angular velocity module that controls a leader motor and one or more
 * follower motors.
 * 
 * <p>
 * This module extends {@link AngularVelocityModule} and uses one motor as the
 * leader while configuring the provided follower motors to follow it.
 * Follower motor inputs are also updated and logged periodically.
 * 
 * <p>
 * Each follower must have an {@code alignment} value configured in its motor
 * configuration. The alignment determines how the follower operates relative
 * to the leader.
 */
public class MultiAngularVelocityModule extends AngularVelocityModule {

    /** Number of follower motors controlled by this module. */
    private final int numberOfFollowers;

    /** Motor IO interfaces for the follower motors. */
    private final MotorIO[] followerIOs;

    /** Logged inputs for each follower motor. */
    private final MotorIOInputsAutoLogged[] followerInputs;

    /**
     * Creates a multi-motor angular velocity module.
     * 
     * <p>
     * The provided follower motors are configured to follow the leader motor.
     * Each follower must have an {@code alignment} value defined in its motor
     * configuration.
     * 
     * @param name                  name used to identify and log this module
     * @param leader                motor IO interface for the leader motor
     * @param followers             motor IO interfaces for the follower motors
     * @param rotorToMechanismRatio ratio between the motor rotor and the mechanism
     * 
     * @throws IllegalArgumentException if a follower does not have an
     *                                  {@code alignment} value configured
     */
    public MultiAngularVelocityModule(
            String name, MotorIO leader, MotorIO[] followers, double rotorToMechanismRatio) {
        super(name, leader, rotorToMechanismRatio);
        numberOfFollowers = followers.length;
        followerIOs = followers;
        followerInputs = new MotorIOInputsAutoLogged[numberOfFollowers];

        for (int i = 0; i < numberOfFollowers; i++) {
            followerIOs[i].follow(
                    leader.getConfig().id(),
                    followerIOs[i]
                            .getConfig()
                            .alignment()
                            .orElseThrow(
                                    () -> new IllegalArgumentException(
                                            "Every follower config must have the `alignment` field set")));
            followerInputs[i] = new MotorIOInputsAutoLogged();
        }
    }

    /**
     * Updates and logs the inputs for the leader and all follower motors.
     * 
     * <p>
     * The leader motor inputs are updated by the parent implementation. This
     * method additionally updates each follower's inputs and logs them using the
     * module's configured rotor-to-mechanism ratio.
     */
    @Override
    public void periodic() {
        super.periodic();

        for (int i = 0; i < numberOfFollowers; i++) {
            followerIOs[i].updateInputs(followerInputs[i]);
            Logger.processInputs(
                    name + "/follower" + (numberOfFollowers == 1 ? "" : i),
                    followerInputs[i].applyRotorToMechanismRatio(rotorToMechanismRatio));
        }
    }
}

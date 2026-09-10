package frc.lib.module.position;

import frc.lib.motor.MotorIO;
import frc.lib.motor.MotorIOInputsAutoLogged;
import org.littletonrobotics.junction.Logger;

public class MultiAngularPositionModule extends AngularPositionModule {
    private final int numberOfFollowers;
    private final MotorIO[] followerIOs;
    private final MotorIOInputsAutoLogged[] followerInputs;

    public MultiAngularPositionModule(
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
                                    () ->
                                            new IllegalArgumentException(
                                                    "Every follower config must have the `alignment` field set")));
            followerInputs[i] = new MotorIOInputsAutoLogged();
        }
    }

    @Override
    public void periodic() {
        super.periodic();

        for (int i = 0; i < numberOfFollowers; i++) {
            followerIOs[i].updateInputs(followerInputs[i]);
            Logger.processInputs(
                    name + "/Follower" + (numberOfFollowers == 1 ? "" : i),
                    followerInputs[i].applyRotorToMechanismRatio(rotorToMechanismRatio));
        }
    }
}

package frc.lib.motor;

import static edu.wpi.first.units.Units.KilogramSquareMeters;
import static edu.wpi.first.units.Units.Volts;

import com.ctre.phoenix6.sim.TalonFXSimState;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;

public class SimMotor extends RealMotor {
    private final DCMotorSim simMotor;
    private double lastUpdateTimestamp = 0.0;

    public SimMotor(MotorConfig config) {
        super(config);

        DCMotor dcMotor = config.type().getDcMotor();
        simMotor =
                new DCMotorSim(
                        LinearSystemId.createDCMotorSystem(
                                dcMotor, config.momentOfInertia().in(KilogramSquareMeters), 1.0),
                        dcMotor);
    }

    @Override
    public void updateInputs(MotorIOInputs inputs) {
        TalonFXSimState motorSim = motor.getSimState();
        motorSim.setSupplyVoltage(RobotController.getBatteryVoltage());
        simMotor.setInputVoltage(motorSim.getMotorVoltageMeasure().in(Volts));

        double timestamp = Timer.getFPGATimestamp();
        simMotor.update(timestamp - lastUpdateTimestamp);
        lastUpdateTimestamp = timestamp;

        motorSim.setRawRotorPosition(simMotor.getAngularPosition());
        motorSim.setRotorVelocity(simMotor.getAngularVelocity());

        signals.refreshAll();
        inputs.connected = signals.isAllGood();
        inputs.position = positionSignal.getValue();
        inputs.velocity = velocitySignal.getValue();
        inputs.voltage = voltageSignal.getValue();
        inputs.statorCurrent = statorCurrentSignal.getValue();
        inputs.supplyCurrent = supplyCurrentSignal.getValue();
        inputs.temperature = temperatureSignal.getValue();
    }
}

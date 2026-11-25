package frc.lib.motor;

import static frc.lib.units.AngleUnit.Radians;
import static frc.lib.units.AngularAccelerationUnit.RadiansPerSecondSquared;
import static frc.lib.units.AngularVelocityUnit.RadiansPerSecond;
import static frc.lib.units.CurrentUnit.Amps;
import static frc.lib.units.TemperatureUnit.Kelvin;
import static frc.lib.units.VoltageUnit.Volts;

import com.ctre.phoenix6.signals.MotorAlignmentValue;
import frc.lib.units.Angle;
import frc.lib.units.AngularAcceleration;
import frc.lib.units.AngularVelocity;
import frc.lib.units.Current;
import frc.lib.units.Temperature;
import frc.lib.units.Voltage;
import org.littletonrobotics.junction.AutoLog;
import org.wpilib.framework.RobotBase;

/**
 * Provides an interface for communicating with a motor controller.
 *
 * <p>{@code MotorIO} abstracts the hardware-specific implementation of a motor controller. This
 * allows the rest of the robot code to interact with motors without depending on a specific motor
 * controller implementation.
 *
 * <p>Implementations of this class are responsible for updating motor inputs, configuring follower
 * motors, setting motor control modes, and applying position, velocity, and voltage commands.
 */
public abstract class MotorIO {

    /** Configuration associated with this motor. */
    private final MotorConfig config;

    /**
     * Creates a motor IO interface using the provided motor configuration.
     *
     * @param config configuration for the motor
     */
    public MotorIO(MotorConfig config) {
        this.config = config;
    }

    /**
     * Get the configuration for this motor.
     *
     * @return the motor's configuration
     */
    public MotorConfig getConfig() {
        return config;
    }

    /**
     * Creates a real TalonFX or a simulated TalonFX depending on whether the robot is being simulated
     * or not.
     *
     * @param config configuration for the motor
     * @return the correct motor implementation
     */
    public static MotorIO buildMotor(MotorConfig config) {
        return RobotBase.isReal() ? new TalonFxMotor(config) : new SimTalonFxMotor(config);
    }

    /**
     * Creates an array of TalonFX motors based on the provided configurations.
     *
     * <p>Useful for creating a bunch of follower motors, which are stored in arrays
     * in the multi-modules. The i-th motor in the returned array will be configured
     * with the i-th configuration from the configs array.
     *
     * @param configs the configurations for the motors
     * @return an array of motors corresponding to the configurations
     */
    public static MotorIO[] buildMotors(MotorConfig[] configs) {
        MotorIO[] result = new MotorIO[configs.length];

        for (int i = 0; i < configs.length; i++) {
            result[i] = buildMotor(configs[i]);
        }

        return result;
    }

    /**
     * Stores the inputs and status information reported by a motor.
     *
     * <p>This class is annotated with {@link AutoLog}, allowing AdvantageKit to automatically
     * generate an {@code MotorIOInputsAutoLogged} class for logging these inputs.
     */
    @AutoLog
    public static class MotorIOInputs {

        /** Whether the motor controller is currently connected. */
        public boolean connected = false;

        /** Current motor rotor position. */
        public Angle position = Radians.of(0.0);

        /** Current motor rotor angular velocity. */
        public AngularVelocity velocity = RadiansPerSecond.of(0.0);

        /** Current motor rotor angular acceleration. */
        public AngularAcceleration acceleration = RadiansPerSecondSquared.of(0.0);

        /** Current voltage being applied to the motor. */
        public Voltage voltage = Volts.of(0.0);

        /** Current being drawn by the motor stator. */
        public Current statorCurrent = Amps.of(0.0);

        /** Current being supplied to the motor controller. */
        public Current supplyCurrent = Amps.of(0.0);

        /** Current motor temperature. */
        public Temperature temperature = Kelvin.of(294.0);
    }

    /**
     * Updates the provided inputs with the motor's current status.
     *
     * <p>This is done instead of simply returning a new inputs object so that we can reuse inputs
     * objects, and don't have to waste time each loop period allocating a bunch of new inputs
     * objects.
     *
     * @param inputs object that should be populated with the motor's current inputs
     */
    public abstract void updateInputs(MotorIOInputs inputs);

    /**
     * Configures this motor to follow another motor.
     *
     * @param leader the motor to follow
     * @param alignment alignment of this motor relative to the leader
     */
    public abstract void follow(MotorIO leader, MotorAlignmentValue alignment);

    /**
     * Sets the desired angular position of the motor rotor. This should be used when the desired
     * position is a known setpoint so that the motor can run a motion profile effectively.
     *
     * @param position desired rotor position
     */
    public void setPositionSetpoint(Angle position) {
        setDynamicPosition(position);
    }

    /**
     * Sets the desired angular position of the motor rotor. This should be used when the desired
     * position changes over time (like in an auto-aiming launcher hood) to avoid the delay of a
     * motion profile.
     *
     * @param position desired rotor position
     */
    public abstract void setDynamicPosition(Angle position);

    /**
     * Sets the desired angular velocity of the motor rotor. This should be used when the desired
     * velocity is a known setpoint so that the motor can run a motion profile effectively.
     *
     * @param velocity desired rotor angular velocity
     */
    public void setVelocitySetpoint(AngularVelocity velocity) {
        setDynamicVelocity(velocity);
    }

    /**
     * Sets the desired angular velocity of the motor rotor. This should be used when the desired
     * velocity changes over time (like with a swerve module drive motor) to avoid the delay of a
     * motion profile.
     *
     * @param velocity desired rotor angular velocity
     */
    public abstract void setDynamicVelocity(AngularVelocity velocity);

    /**
     * Sets the voltage output of the motor.
     *
     * @param voltage voltage to apply to the motor
     */
    public abstract void setVoltage(Voltage voltage);
}

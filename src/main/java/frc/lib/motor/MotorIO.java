package frc.lib.motor;

import static edu.wpi.first.units.Units.Amps;
import static edu.wpi.first.units.Units.Celsius;
import static edu.wpi.first.units.Units.Radians;
import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.Volts;

import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.units.measure.Temperature;
import edu.wpi.first.units.measure.Voltage;
import frc.lib.CanId;
import lombok.Getter;
import org.littletonrobotics.junction.AutoLog;

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
    @Getter private final MotorConfig config;

    /**
     * Creates a motor IO interface using the provided motor configuration.
     *
     * @param config configuration for the motor
     */
    public MotorIO(MotorConfig config) {
        this.config = config;
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

        /** Current voltage being applied to the motor. */
        public Voltage voltage = Volts.of(0.0);

        /** Current being drawn by the motor stator. */
        public Current statorCurrent = Amps.of(0.0);

        /** Current being supplied to the motor controller. */
        public Current supplyCurrent = Amps.of(0.0);

        /** Current motor temperature. */
        public Temperature temperature = Celsius.of(21.0);

        /**
         * Converts rotor position and velocity into mechanism position and velocity using the provided
         * rotor-to-mechanism ratio.
         *
         * <p>Other motor inputs, such as voltage, current, and temperature, are not affected by the
         * ratio and are copied directly to the returned inputs.
         *
         * @param rotorToMechanismRatio ratio between the motor rotor and the mechanism
         * @return a new set of inputs containing mechanism-relative position and velocity values
         */
        public MotorIOInputsAutoLogged applyRotorToMechanismRatio(double rotorToMechanismRatio) {
            MotorIOInputsAutoLogged result = new MotorIOInputsAutoLogged();
            result.connected = connected;
            result.position = position.div(rotorToMechanismRatio);
            result.velocity = velocity.div(rotorToMechanismRatio);
            result.voltage = voltage;
            result.statorCurrent = statorCurrent;
            result.supplyCurrent = supplyCurrent;
            result.temperature = temperature;
            return result;
        }
    }

    /**
     * Updates the provided inputs with the motor's current status.
     *
     * @param inputs object that should be populated with the motor's current inputs
     */
    public abstract void updateInputs(MotorIOInputs inputs);

    /**
     * Configures this motor to follow another motor.
     *
     * @param leader CAN ID of the motor to follow
     * @param alignment alignment of this motor relative to the leader
     */
    public abstract void follow(CanId leader, MotorAlignmentValue alignment);

    /**
     * Sets the neutral mode of the motor.
     *
     * @param mode neutral mode to apply when the motor is not being commanded
     */
    public abstract void setNeutralMode(NeutralModeValue mode);

    /**
     * Sets the desired angular position of the motor rotor. This should be used when the desired
     * position is a known setpoint so that the motor can run a motion profile effectively.
     *
     * @param position desired rotor position
     */
    public abstract void setPositionSetpoint(Angle position);

    /**
     * Sets the desired angular position of the motor rotor. This should be used when the desired
     * position changes over time (like in an auto-aiming launcher hood) to avoid the delay of a
     * motion profile.
     *
     * @param position desired rotor position
     */
    public abstract void setDynamicPosition(Angle position);

    /**
     * Sets the desired angular velocity of the motor rotor.
     *
     * @param velocity desired rotor angular velocity
     */
    public abstract void setVelocitySetpoint(AngularVelocity velocity);

    /**
     * Sets the voltage output of the motor.
     *
     * @param voltage voltage to apply to the motor
     */
    public abstract void setVoltage(Voltage voltage);
}

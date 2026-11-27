package com.nicoletfear.lib.statemachine;

import static com.nicoletfear.lib.units.TimeUnit.Seconds;

import com.nicoletfear.lib.units.Timestamp;
import org.wpilib.system.Timer;

/**
 * Base class for state machines that track a current state, the previous state, and the time at
 * which the current state was entered.
 *
 * <p>Subclasses typically define a nested sealed interface or enum implementing {@link State},
 * implement a {@code periodic()} method that decides on the next state and executes the behavior
 * for the current state, and call {@link #logState()} to log the state machine's status.
 *
 * @param <S> the type of state this machine holds
 */
public abstract class StateMachine<S extends State> {

    /** Name used to identify and log this state machine. */
    protected String name;

    /** The state this machine is currently in. */
    protected S state;

    /** Time at which the current state was entered. */
    private Timestamp enteredTimestamp;

    /** The state this machine was in before the current state. */
    private S previousState;

    /**
     * Creates a state machine in the given initial state.
     *
     * <p>The previous state is initialized to the initial state, and the entered timestamp is set to
     * the current time.
     *
     * @param name name used to identify and log this state machine
     * @param initialState the state to start in
     */
    public StateMachine(String name, S initialState) {
        this.name = name;
        enteredTimestamp = Seconds.timestamp(Timer.getTimestamp());
        previousState = initialState;
        state = initialState;
    }

    /** Logs the current state, the time at which it was entered, and the previous state. */
    public void logState() {
        state.log(name + "/State");
        enteredTimestamp.log(name + "/Time Entered");
        previousState.log(name + "/Previous State");
    }

    /**
     * Transitions to the given state.
     *
     * <p>The entered timestamp is set to the current time and the given state becomes both the
     * current state and, on the next transition, the previous state.
     *
     * @param state the state to transition to
     */
    public void setState(S state) {
        if (!state.equals(this.state)) {
            enteredTimestamp = Seconds.timestamp(Timer.getTimestamp());
            previousState = this.state;
            this.state = state;
        }
    }
}

package frc.lib.statemachine;

import static frc.lib.units.TimeUnit.Seconds;

import org.wpilib.system.Timer;

import frc.lib.units.Timestamp;

public abstract class StateMachine<S extends State> {
    protected String name;
    protected S state;
    private Timestamp enteredTimestamp;
    private S previousState;

    public StateMachine(String name, S initialState) {
        this.name = name;
        enteredTimestamp = Seconds.timestamp(Timer.getTimestamp());
        previousState = initialState;
        state = initialState;
    }

    public void logState() {
        state.log(name + "/State");
        enteredTimestamp.log(name + "/Time Entered");
        previousState.log(name + "/Previous State");
    }

    public void setState(S state) {
        enteredTimestamp = Seconds.timestamp(Timer.getTimestamp());
        previousState = this.state;
        this.state = state;
    }
}

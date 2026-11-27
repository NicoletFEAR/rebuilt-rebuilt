package com.nicoletfear.lib.statemachine;

import static com.nicoletfear.lib.units.TimeUnit.Seconds;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.nicoletfear.lib.units.Timestamp;
import java.lang.reflect.Field;
import org.junit.jupiter.api.Test;
import org.wpilib.system.Timer;

class StateMachineTest {
    private enum TestState implements State {
        FIRST,
        SECOND,
        THIRD
    }

    private static final class TestMachine extends StateMachine<TestState> {
        TestMachine(TestState initialState) {
            super("Test Machine", initialState);
        }
    }

    private static Object privateField(StateMachine<?> machine, String name) throws Exception {
        Field field = StateMachine.class.getDeclaredField(name);
        field.setAccessible(true);
        return field.get(machine);
    }

    @Test
    void startsInInitialStateWithInitialStateAsPreviousState() throws Exception {
        TestMachine machine = new TestMachine(TestState.FIRST);

        assertEquals("Test Machine", machine.name);
        assertSame(TestState.FIRST, machine.state);
        assertSame(TestState.FIRST, privateField(machine, "previousState"));
    }

    @Test
    void setStateChangesCurrentState() {
        TestMachine machine = new TestMachine(TestState.FIRST);

        machine.setState(TestState.SECOND);

        assertSame(TestState.SECOND, machine.state);
    }

    @Test
    void setStateMovesCurrentStateToPreviousState() throws Exception {
        TestMachine machine = new TestMachine(TestState.FIRST);

        machine.setState(TestState.SECOND);
        assertSame(TestState.FIRST, privateField(machine, "previousState"));

        machine.setState(TestState.THIRD);
        assertSame(TestState.SECOND, privateField(machine, "previousState"));
        assertSame(TestState.THIRD, machine.state);
    }

    @Test
    void setStateUpdatesEnteredTimestamp() throws Exception {
        TestMachine machine = new TestMachine(TestState.FIRST);
        Timestamp initialTimestamp = (Timestamp) privateField(machine, "enteredTimestamp");

        Timestamp before = Seconds.timestamp(Timer.getTimestamp());
        machine.setState(TestState.SECOND);
        Timestamp after = Seconds.timestamp(Timer.getTimestamp());

        Timestamp entered = (Timestamp) privateField(machine, "enteredTimestamp");

        assertNotSame(initialTimestamp, entered);
        assertTrue(entered.after(before) || entered.since(before).in(Seconds) >= 0.0);
        assertTrue(entered.before(after) || entered.since(after).in(Seconds) <= 0.0);
    }
}

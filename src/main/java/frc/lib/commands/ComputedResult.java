package frc.lib.commands;

public class ComputedResult<T> {
    public T value;

    public void set(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }
}

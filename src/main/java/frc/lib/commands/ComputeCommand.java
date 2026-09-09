package frc.lib.commands;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.Command;

public class ComputeCommand<T> extends Command {
    private final Supplier<T> computation;
    private final ComputedResult<T> result;
    private boolean finished;

    public ComputeCommand(Supplier<T> computation, ComputedResult<T> result) {
        this.computation = computation;
        this.result = result;
        finished = false;
    }

    @Override
    public void initialize() {
        this.result.set(computation.get());
        finished = true;
    }

    @Override
    public boolean isFinished() {
        return finished;
    }
}

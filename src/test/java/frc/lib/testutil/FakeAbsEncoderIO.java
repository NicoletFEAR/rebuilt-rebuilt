package frc.lib.testutil;

import frc.lib.sensor.absencoder.AbsEncoderIO;
import java.util.function.Consumer;

/** An {@link AbsEncoderIO} implementation that records calls and returns test-controlled data. */
public class FakeAbsEncoderIO implements AbsEncoderIO {
    /** Populates the inputs object during {@link #updateInputs(AbsEncoderIOInputs)}. */
    public Consumer<AbsEncoderIOInputs> inputUpdater = inputs -> {};

    /** Number of times {@link #updateInputs(AbsEncoderIOInputs)} has been called. */
    public int updateInputsCalls;

    /** Inputs object most recently passed to {@link #updateInputs(AbsEncoderIOInputs)}. */
    public AbsEncoderIOInputs lastInputs;

    /** Number of times {@link #setZeroPoint()} has been called. */
    public int setZeroPointCalls;

    @Override
    public void updateInputs(AbsEncoderIOInputs inputs) {
        updateInputsCalls++;
        lastInputs = inputs;
        inputUpdater.accept(inputs);
    }

    @Override
    public void setZeroPoint() {
        setZeroPointCalls++;
    }
}

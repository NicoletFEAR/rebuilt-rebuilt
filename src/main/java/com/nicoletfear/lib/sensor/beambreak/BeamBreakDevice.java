package com.nicoletfear.lib.sensor.beambreak;

import com.nicoletfear.lib.CanId;

/** The information necessary to specify a beam break's implementation. */
public sealed interface BeamBreakDevice permits BeamBreakDevice.CanRange, BeamBreakDevice.DigitalInput {
    /** A CANrange beam break, with its CAN ID. */
    public record CanRange(CanId id) implements BeamBreakDevice {}

    /**
     * A digital-input-based beam break, with its Smart IO ID, which must be on the interval [0, 5].
     */
    public record DigitalInput(int id) implements BeamBreakDevice {}
}

package frc.lib.led;

import frc.lib.CanId;

/**
 * Stores the configuration values used to configure an LED controller.
 *
 * <p>This record contains the LED controller's ID, as well as the number of LEDs connected to it.
 * Note that this count should not include built-in LEDs, like the 8 that the CANdle has.
 *
 * @param id ID of the LED controller
 * @param ledCount number of connected LEDs
 */
public record LedConfig(CanId id, int ledCount) {}

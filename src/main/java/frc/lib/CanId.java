package frc.lib;

import com.ctre.phoenix6.CANBus;

/**
 * Stores the CAN bus and device ID used to identify a CAN device.
 * 
 * <p>
 * This record groups the CAN bus and device ID together so they can be passed
 * around as a single value when configuring and creating CAN devices.
 * 
 * @param bus CAN bus that the device is connected to
 * @param id  CAN device ID
 */
public record CanId(CANBus bus, int id) {
}

package frc.lib;

import com.ctre.phoenix6.CANBus;

public record CanId(CANBus bus, int id) {}

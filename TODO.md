# Fixes
* Add current limits, inversion, soft limits to MotorConfig
* Add motor type to MotorConfig
* Add MotionMagic parameters to MotorConfig
* Fix absolute encoders
* Test deadbands to make sure they're fine
* Make the modules with absolute encoders set the motors' relative positions to the absolute encoder's position at start-up
* Refactor Rotation2d safe creation out of Utils once we have a folder for field geometry
* Introduce constant for status signal update period
* Implement support for CANdle with no connected LEDs

# Structure
* New Outputs record for each subsystem to guarantee every subsystem state does something with every part of the subsystem
* Separate periodic into 4 phases: input refresh, then state decisions, then execution, then final logging
* Add wanted state / actual state dichotomy
* See if there's some way to lower the number of module classes we have
* Add unit tests
* Add null safety
* Create AllianceRelativeRotation2d and replace our alliance-relative Rotation2ds with it

# Features
* Auto-aim
* Add replay IO
* Get actual setpoints in here
* Add a class to manage voltage and track which battery is being used through Elastic dropdown
* Add a class to monitor and log CAN bus information
* Add a clock class and use that to get time rather than Timer.getTimestamp()
* Mechanism visualization
* LQR? Kalman filters?
* Add gyroscope
* Add a class to manage the gamepads, which will hopefully make button bindings quicker to modify
* Set up alerts system in case anything goes wrong (like disconnected controllers)
* Alerts when attempting to set a voltage higher than 12.0
* Alerts when a configuration doesn't apply
* Error injection for simulation (with sim for sensors)
* CANdle animations and simulation
* Camera stuff
* Add CTRE swerve IO
* Procedurally generated autos (ideally with custom pathfinder that keeps the Limelight(s) looking at AprilTags at all times for optimal accuracy)
* Auto-checklists with data tracking to determine normal values and detect anomalies, also use these normal values for more accurate simulation (perhaps concurrently simulate and track deviations from simulated values mid-match if processing power allows)
* Game piece simulation

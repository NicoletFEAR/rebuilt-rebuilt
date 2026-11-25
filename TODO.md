# Fixes
* Confirm javadocs for VisionIO are accurate with Big S
* Convert IO interfaces to abstract classes if they have associated config objects
* Add current limits, inversion, soft limits to MotorConfig
* Add motor type to MotorConfig
* Add MotionMagic parameters to MotorConfig
* Fix absolute encoders
* Make the modules with absolute encoders set the motors' relative positions to the absolute encoder's position at start-up
* Refactor Rotation2d safe creation out of Utils once we have a folder for field geometry
* Introduce constant for status signal update period
* Implement support for CANdle with no connected LEDs
* Implement RemoteCANcoder for absolute encoder modules
* Create AllianceRelativeRotation2d and replace our alliance-relative Rotation2ds with it
* Change package to com.nicoletfear instead of frc?

# Features
* Add unit tests, coverage checks, and mutation testing
* Auto-aim
* Add replay IO
* Get actual setpoints in here
* Add a class to manage voltage and track which battery is being used through Elastic dropdown
* Add a class to monitor and log CAN bus information
* Add a clock class and use that to get time rather than Timer.getTimestamp()
* Mechanism visualization
* LQR? Kalman filters? These probably won't be useful, but I still need to do more research to make sure
* Add gyroscope
* Add a class to manage the gamepads, which will hopefully make button bindings quicker to modify
* Set up alerts system in case anything goes wrong (like disconnected controllers)
* Alerts when attempting to set a voltage higher than 12.0
* Alerts when a configuration doesn't apply
* Error injection for simulation (with sim for sensors)
* CANdle animations and simulation
* Camera stuff
* Method to automatically download logs from the robot somehow
* Add CTRE swerve IO
* Procedurally generated autos (ideally with custom pathfinder that keeps the Limelight(s) looking at AprilTags at all times for optimal accuracy)
* Auto-checklists with data tracking to determine normal values and detect anomalies
* Game piece simulation

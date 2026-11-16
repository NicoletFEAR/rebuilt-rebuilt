* Replace CanRangeConfig with BeamBreakConfig and TofConfig
* Module mech-relative logging and create subclass of LoggableInputs with a log method to log to outputs, then implement for everything that currently we just log as inputs
* Separate logging inputs from outputs conceptually
* Separate periodic into 4 phases: input refresh, then state decisions, then execution, then final logging
* Make Sim dependent on vendor again
* Add current limits, inversion, soft limits to MotorConfig
* Change gear ratio from manual to configured
* Update CAN buses to SystemCore
* Add wanted state / actual state dichotomy
* See if there's some way to lower the number of module classes we have
* Fix absolute encoders
* Test deadbands to make sure they're fine
* Add replay IO
* Add unit tests
* Add null safety
* Make all the IOs vendor-agnostic
* Make PID and feedforward values have units
* Add a class to manage voltage and track which battery is being used through Elastic dropdown
* Add a class to monitor and log CAN bus information
* Add a clock class and use that to get time rather than Timer.getTimestamp()
* Make sure every subsystem state does something with every part of the subsystem
* Create SwerveDriveTemplate class so that RobotContainer doesn't have to do so much instantiation for the drivebase and then make SwerveModule private
* In general, get sub-subsystems internal
* Edit subsystems so that they use setDynamicPosition instead of setPositionSetpoint when appropriate
* Make the modules with absolute encoders set the motors' relative positions to the absolute encoder's position at start-up
* Create AllianceRelativeRotation2d and replace our alliance-relative Rotation2ds with it
* Refactor Rotation2d safe creation out of Utils once we have a folder for field geometry
* Add MotionMagic parameters to MotorConfig
* Mechanism visualization
* LQR? Kalman filters?
* Add gyroscope
* Add a class to manage the gamepads, which will hopefully make button bindings quicker to modify
* Set up alerts system in case anything goes wrong (like disconnected controllers)
* Alerts when attempting to set a voltage higher than 12.0
* Alerts when a configuration doesn't apply
* Error injection for simulation
* LedIO
* Camera stuff
* Add CTRE swerve IO
* Procedurally generated autos (ideally with custom pathfinder that keeps the Limelight(s) looking at AprilTags at all times for optimal accuracy)
* Auto-checklists with data tracking to determine normal values and detect anomalies, also use these normal values for more accurate simulation (perhaps concurrently simulate and track deviations from simulated values mid-match if processing power allows)
* Game piece simulation

* Make all the IOs vendor-agnostic
* Make all sim IO things vendor-agnostic
* Finish replacing state enums with sealed interfaces
* Automatic units library generation
* Make PID and feedforward values have units
* Add a class to manage voltage and track which battery is being used through Elastic dropdown
* Add a class to manage the CAN buses and keep usage below 90%
* Add a clock class and use that to get time rather than Timer.getTimestamp()
* Make sure every subsystem state does something with every part of the subsystem
* Create SwerveDriveTemplate class so that RobotContainer doesn't have to do so much instantiation for the drivebase and then make SwerveModule private
* Edit subsystems so that they use setDynamicPosition instead of setPositionSetpoint when appropriate
* Make the modules with absolute encoders set the motors' relative positions to the absolute encoder's position at start-up
* Create AllianceRelativeRotation2d and replace our alliance-relative Rotation2ds with it
* Refactor Rotation2d safe creation out of Utils once we have a folder for field geometry
* Add MotionMagic parameters to MotorConfig
* LQR? Kalman filters?
* Add gyroscope
* Add a class to manage the gamepads, which will hopefully make button bindings quicker to modify
* Set up alerts system in case anything goes wrong (like disconnected controllers)
* Alerts when attempting to set a voltage higher than 12.0
* Error injection for simulation
* LedIO
* Add CTRE swerve IO
* Procedurally generated autos (ideally with custom pathfinder that keeps the Limelight(s) looking at AprilTags at all times for optimal accuracy)
* Auto-checklists with data tracking to determine normal values and detect anomalies, also use these normal values for more accurate simulation (perhaps concurrently simulate and track deviations from simulated values mid-match if processing power allows)
* Game piece simulation
* Make positional control for sim motor actually simulate rather than instantly teleporting to desired position

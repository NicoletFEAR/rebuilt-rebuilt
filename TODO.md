* Replace CTRE motor simulation with generic motor simulation
* Make everything actually vendor-agnostic
* Make all sim IO things vendor-agnostic
* ADTs for other state machines
* Units for feedforward values
* Simulation voltage subsystem, and track which battery we're using at all times
* CAN bus subsystem to keep bus usage below 90%
* Clock subsystem and integration into simulation
* Get everything we have so far working in simulation
* Make sure every subsystem state does something with every part of the subsystem
* Create SwerveDriveTemplate class so that RobotContainer doesn't have to do so much instantiation for the drivebase
* Edit subsystems so that they use setDynamicPosition instead of setPositionSetpoint when appropriate
* Make the modules with absolute encoders set the motors' relative positions to the absolute encoder's position at start-up
* Create AllianceRelativeRotation2d and replace our alliance-relative Rotation2ds with it
* Add MotionMagic parameters to MotorConfig
* Make motor simulation actually simulate motors
* LQR? Kalman filters?
* Add gyroscope
* Universal controller class
* Set up alerts system in case anything goes wrong (like disconnected controllers)
* Error injection for simulation
* LedIO
* Add CTRE swerve IO
* Procedurally generated autos (ideally with custom pathfinder optimized for limelight-based accuracy)
* Auto-checklists with data tracking to determine normal values and detect anomalies, also use these normal values for more accurate simulation (perhaps concurrently simulate and track deviations from simulated values)
* Game piece simulation

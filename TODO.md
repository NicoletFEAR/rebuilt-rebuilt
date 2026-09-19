* Create SwerveDriveTemplate class so that RobotContainer doesn't have to do so much instantiation for the drivebase
* Replace CTRE motor simulation with generic motor simulation
* Make the modules with absolute encoders set the motors' relative positions to the absolute encoder's position at start-up
* Create new type of module that can be controlled with position or velocity
* Create AllianceRelativeRotation2d and replace our alliance-relative Rotation2ds with it
* Add gyroscope
* Edit subsystems so that they use setDynamicPosition instead of setPositionSetpoint when appropriate
* Make sure every subsystem state does something with every part of the subsystem
* Request system
* LedIO

// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.subsystems.GamePieceCam;
import frc.robot.subsystems.SwerveSubsystem;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class RotToPiece extends PIDCommand {
  SwerveSubsystem drive ;
  GamePieceCam camera;
  /** rotate the robot to face a game piece
   *   o 
   * TODO: implement a swerveSS command to rotate the robot
   */
  public RotToPiece(SwerveSubsystem drive, GamePieceCam camera) {
    super(
        // The controller that the command will use
        new PIDController(.3/22., 0, 0),    // P = .3 * 1./(FOV/2)
        // This should return the measurement
        () -> camera.getYaw(),
        // This should return the setpoint (can also be a constant)
        () -> 0,
        // This uses the output
        output -> {
          // Use the output here
          drive.driveMe(output);
          //drive.driveit(0.,0.,output);
        });
    // Use addRequirements() here to declare subsystem dependencies.
    
    this.drive=drive;
    this.camera = camera;
    addRequirements(drive, camera);
    // Configure additional PID options by calling `getController` here.
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return Math.abs(camera.getYaw())<5.;
  }
}

// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Gripper;
import frc.robot.subsystems.SwerveSubsystem;

public class GoToGamePiece extends CommandBase {
  SwerveSubsystem drive;
  Gripper gripper;
  private Pose2d startPose;
  /** grab a game piece.
   * <p> presuming the robot is facing a game piece:
   *  <ol>
   *     <li> drive toward it (not too fast)
   *     <li> stop when game piece is sensed (so it can be grabbed)
   */
  public GoToGamePiece(SwerveSubsystem drive, Gripper gripper) {
    addRequirements(drive, gripper);
    this.drive = drive;
    this.gripper = gripper;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    startPose = drive.getPose();
    drive.driveit(.3, 0, 0., false);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drive.stopModules();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    //SmartDashboard.putNumber("Get Piece", Units.metersToFeet(drive.distTravelled(startPose)));
    return gripper.grabbable()
    // TODO for safety, limit distance?
    || drive.distTravelled(startPose) > Units.feetToMeters(3.);
  }
}

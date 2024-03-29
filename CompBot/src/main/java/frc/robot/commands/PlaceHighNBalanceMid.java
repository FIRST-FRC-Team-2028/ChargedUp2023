// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import javax.swing.text.html.parser.DTD;

import com.ctre.phoenix.sensors.WPI_Pigeon2;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants.ArmConstants;
import frc.robot.Constants.FieldConstants;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.GamePieceCam;
import frc.robot.subsystems.Gripper;
import frc.robot.subsystems.SwerveSubsystem;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class PlaceHighNBalanceMid extends SequentialCommandGroup {
  /** Place game piece:
       *    1 extend arm to desired positiona, but don't wait longer than 3 seconds
       *    2 open gripper
       *    3 back away from grid
       *    4 retract arms
       *  
       *  Step 1 is a race between the ArmRun command and a Wait command. 
       *  Steps 3 and 4 should be accomplished in parallel, but 4 should wait a second before it starts
       */
  public PlaceHighNBalanceMid(Arm arm, SwerveSubsystem drive, Gripper gripper, PWM lights, GamePieceCam gamePieceCam, WPI_Pigeon2 gyro) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(Commands.race(  // first one done ends both
                    new ArmRun(arm, ArmConstants.cubeDepth1,ArmConstants.cubeDepth1R+50, true)  // step 1
                    ,new WaitCommand(1.5)   // cant wait forever to get in position
                )
                ,new InstantCommand(() -> arm.makeMeDone()),  // ensure step 1 is ended
                Commands.race(
                    new ArmRun(arm, ArmConstants.cubeDepth2,ArmConstants.cubeDepth2R, true)  // step 1
                    ,new WaitCommand(2)
                )   // cant wait forever to get in position
                ,new GripperOpenClose(gripper, true, lights)  //  step 2
                ,new WaitCommand(.5)
                ,new InstantCommand(() -> arm.makeMeDone())  // ensure step 1 is ended
                ,Commands.parallel(     // drive and stow arm in parallel
                  new StoponDockMiddle(drive, gyro),
                  new WaitCommand(1).andThen(new GripperOpenClose(gripper, false, lights)), //  step 2.5
                  new WaitCommand(1).andThen(new ArmRun(arm, ArmConstants.retracto0,ArmConstants.retracto0R, true))
                )
    );
  }
}

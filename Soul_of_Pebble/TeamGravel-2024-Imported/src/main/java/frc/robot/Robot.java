package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Elevator;
//import frc.robot.subsystems.Transfer;
import frc.robot.subsystems.Intake;

public class Robot extends TimedRobot {
    private CommandXboxController driverController;
    //private CommandXboxController operatorController;

    private Drivetrain drivetrain;
    private Elevator elevator;
    private Intake intake;
    //private Transfer transfer;
    private SendableChooser<Command> autoChooser;


    @Override
    public void robotInit() {
        //Set driver controllers
        driverController = new CommandXboxController(0);
        //operatorController = new CommandXboxController(1);

        drivetrain = new Drivetrain(driverController);
        elevator = new Elevator();
        intake = new Intake();
        //transfer = new Transfer();
        CameraServer.startAutomaticCapture();
        autoChooser = new SendableChooser<>();

//CREATES AUTO OPTIONS AND ASSOCIATED COMMANDS
        autoChooser.addOption("Left 2 Cube Auto", new SequentialCommandGroup(
            elevator.setPosition(Elevator.Constants.LOW),
            Commands.waitSeconds(0.5),
            drivetrain.driveAuto(0, 0.5, 0).withTimeout(1.425),
            drivetrain.driveAuto(0,0,-0.66).withTimeout(0.45),
            drivetrain.driveAuto(0,0.275,0).withTimeout(1.5),

            new ParallelCommandGroup(
            intake.variableOuttake(.65).withTimeout(1),
            Commands.waitSeconds(1),
            drivetrain.driveAuto(0,-0.25, 0).withTimeout(1.3)),

            intake.stopRollers(),
            elevator.setPosition(Elevator.Constants.STOW),
            //End of first cube sequence
            drivetrain.driveAuto(0,0,0.66).withTimeout(0.5),

            new ParallelCommandGroup(
            drivetrain.driveAuto(0, 0.366, 0).withTimeout(1.1),
            intake.runRollersEqual().withTimeout(1),
            Commands.waitSeconds(1)),

            intake.stopRollers(),
            elevator.setPosition(Elevator.Constants.MEDIUM),
            Commands.waitSeconds(.5),
            drivetrain.driveAuto(0,-0.4,0).withTimeout(1.5),
            drivetrain.driveAuto(0,0,-0.66).withTimeout(0.5),
            drivetrain.driveAuto(0,0.25,0).withTimeout(1.5),
            drivetrain.driveAuto(0,0,0).withTimeout(0.1),
            
            new ParallelCommandGroup(
            intake.variableOuttake(.65).withTimeout(1),
            Commands.waitSeconds(1),
            drivetrain.driveAuto(0,-0.25,0).withTimeout(.75)),

            intake.stopRollers(),
            elevator.setPosition(Elevator.Constants.STOW)));

        autoChooser.addOption("Left 1 Cube Auto", new SequentialCommandGroup(
            //Note: Current chassis moves about 57 inches per power-second
            elevator.setPosition(Elevator.Constants.LOW),
            //While this robot has a cube, the elevator should always be above the ground
            drivetrain.driveAuto(0, 0.25, 0).withTimeout(3.65),
            drivetrain.driveAuto(0,0,-0.66).withTimeout(0.45),
            drivetrain.driveAuto(0,0.2,0).withTimeout(2.5),
            intake.reverseRollers().withTimeout(2),
            Commands.waitSeconds(1),
            intake.stopRollers(),
            drivetrain.driveAuto(0,-0.2, 0).withTimeout(2.5),
            drivetrain.driveAuto(0,0.0,0).withTimeout(.1),
            elevator.setPosition(Elevator.Constants.STOW)));

        autoChooser.addOption("Right 1 Cube Auto", new SequentialCommandGroup(
            elevator.setPosition(Elevator.Constants.LOW),
            drivetrain.driveAuto(0, 0.25, 0).withTimeout(3.65),
            drivetrain.driveAuto(0,0,-0.66).withTimeout(0.45),
            drivetrain.driveAuto(0,0.2,0).withTimeout(2.5),
            intake.reverseRollers().withTimeout(2),
            Commands.waitSeconds(1),
            intake.stopRollers(),
            drivetrain.driveAuto(0,-0.2, 0).withTimeout(2.5),
            drivetrain.driveAuto(0,0.0,0).withTimeout(.1),
            elevator.setPosition(Elevator.Constants.STOW)));
        
        autoChooser.addOption("Right 2 Cube Auto", new SequentialCommandGroup(

        elevator.setPosition(Elevator.Constants.LOW),
        drivetrain.driveAuto(0, 0.5, 0).withTimeout(1.825),
        drivetrain.driveAuto(0,0,0.66).withTimeout(0.45),
        drivetrain.driveAuto(0,0.275,0).withTimeout(1.5),

        new ParallelCommandGroup(
        intake.variableOuttake(.7).withTimeout(1),
        Commands.waitSeconds(1),
        drivetrain.driveAuto(0,-0.25, 0).withTimeout(1.5)),

        intake.stopRollers(),


        //TODO This is the problem spot where it keeps stopping
        elevator.setPosition(Elevator.Constants.STOW),
        //End of first cube sequence
        drivetrain.driveAuto(0,0,-0.66).withTimeout(0.5),

        new ParallelCommandGroup(
        drivetrain.driveAuto(0, 0.3, 0).withTimeout(1.25),
        intake.runRollersEqual().withTimeout(1),
        Commands.waitSeconds(1)),

        intake.stopRollers(),
        elevator.setPosition(Elevator.Constants.MEDIUM),
        drivetrain.driveAuto(0,-0.3,0).withTimeout(.8),
        drivetrain.driveAuto(0,0,0.66).withTimeout(0.5),
        drivetrain.driveAuto(0,0.25,0).withTimeout(1.5),
        drivetrain.driveAuto(0,0,0).withTimeout(0.1),
        
        new ParallelCommandGroup(
        intake.variableOuttake(.7).withTimeout(1),
        Commands.waitSeconds(1),
        drivetrain.driveAuto(0,-0.25,0).withTimeout(.75)),

        intake.stopRollers(),
        elevator.setPosition(Elevator.Constants.STOW)));

        SmartDashboard.putData("Auto Selector", autoChooser);
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
    }

    @Override
    public void teleopInit() {
        /* - Controller Bindings - */

        // Elevator controls
        //Presets
        driverController.x().onTrue(elevator.setPosition(Elevator.Constants.STOW));  
        driverController.a().onTrue(elevator.setPosition(Elevator.Constants.LOW));
        driverController.b().onTrue(elevator.setPosition(Elevator.Constants.MEDIUM));
        driverController.y().onTrue(elevator.setPosition(Elevator.Constants.HIGH));
        //Manual controls & button to set zero to current position
        driverController.povUp().onTrue(elevator.setVoltage(7));
        driverController.povDown().onTrue(elevator.setVoltage(-7));
        driverController.povUp().onFalse(elevator.setVoltage(1));
        driverController.povDown().onFalse(elevator.setVoltage(1));
        driverController.start().onTrue(elevator.zeroElevator());

        // Intake controls
        driverController.rightBumper().onTrue(intake.runRollersEqual());
        driverController.rightBumper().onFalse(intake.stopRollers());
        driverController.leftBumper().onTrue(intake.reverseRollers());
        driverController.leftBumper().onFalse(intake.stopRollers());
        driverController.rightTrigger(0.25).onTrue(intake.runRollersOffset());
        driverController.rightTrigger(0.25).onFalse(intake.stopRollers());
        driverController.leftTrigger(0.25).onTrue(intake.runRollersOffset2());
        driverController.leftTrigger(0.25).onFalse(intake.stopRollers());
       
    }

    @Override
    public void teleopPeriodic() {
        drivetrain.teleopPeriodic();
    }

    @Override
    public void teleopExit() {
        drivetrain.stop();
    }

    @Override
     public void autonomousInit() {
        CommandScheduler.getInstance().schedule(autoChooser.getSelected());



        //MANUAL AUTO SELECTOR, UNCOMMENT ONLY DESIRED AUTO BEFORE DEPLOYING

    //LEFT 2 CUBE AUTO

         /*new SequentialCommandGroup(
            elevator.setPosition(Elevator.Constants.LOW),
            drivetrain.driveAuto(0, 0.5, 0).withTimeout(1.825),
            drivetrain.driveAuto(0,0,-0.66).withTimeout(0.45),
            drivetrain.driveAuto(0,0.25,0).withTimeout(2),
            intake.reverseRollers().withTimeout(2),
            Commands.waitSeconds(1),
            intake.stopRollers(),
            drivetrain.driveAuto(0,-0.25, 0).withTimeout(2),
            drivetrain.driveAuto(0,0.0,0),
            elevator.setPosition(Elevator.Constants.STOW),
            //End of first cube sequence
            drivetrain.driveAuto(0,0,0.66).withTimeout(0.45),
            drivetrain.driveAuto(0, 0.25, 0).withTimeout(1),
            intake.runRollersEqual().withTimeout(1),
            Commands.waitSeconds(1),
            intake.stopRollers(),
            elevator.setPosition(Elevator.Constants.MEDIUM),
            drivetrain.driveAuto(0,-0.25,0).withTimeout(1),
            drivetrain.driveAuto(0,0,-0.66).withTimeout(0.45),
            drivetrain.driveAuto(0,0.25,0).withTimeout(2),
            drivetrain.driveAuto(0,0,0),
            intake.runRollersEqual().withTimeout(1),
            Commands.waitSeconds(1),
            intake.stopRollers(),
            drivetrain.driveAuto(0,-0.2,0).withTimeout(1),
            elevator.setPosition(Elevator.Constants.STOW));*/

    //RIGHT 2 CUBE AUTO

/*new SequentialCommandGroup(
    elevator.setPosition(Elevator.Constants.LOW),
    drivetrain.driveAuto(0, 0.5, 0).withTimeout(1.825),
    drivetrain.driveAuto(0,0,0.66).withTimeout(0.45),
    drivetrain.driveAuto(0,0.2,0).withTimeout(2.5),
    intake.reverseRollers().withTimeout(2),
    Commands.waitSeconds(1),
    intake.stopRollers(),
    drivetrain.driveAuto(0,-0.2, 0).withTimeout(2.5),
    drivetrain.driveAuto(0,0.0,0),
    elevator.setPosition(Elevator.Constants.STOW),
    drivetrain.driveAuto(0,0,-0.66).withTimeout(0.45),
    drivetrain.driveAuto(0, 0.35, 0).withTimeout(1),
    intake.runRollersEqual().withTimeout(1),
    Commands.waitSeconds(1),
    intake.stopRollers(),
    elevator.setPosition(Elevator.Constants.MEDIUM),
    drivetrain.driveAuto(0,-0.35,0).withTimeout(1),
    drivetrain.driveAuto(0,0,0.66).withTimeout(0.45),
    drivetrain.driveAuto(0,0.2,0).withTimeout(2.5),
    drivetrain.driveAuto(0,0,0),
    intake.runRollersEqual().withTimeout(1),
    Commands.waitSeconds(1),
    intake.stopRollers(),
    drivetrain.driveAuto(0,-0.2,0).withTimeout(1),
    elevator.setPosition(Elevator.Constants.STOW));*/


    //LEFT 1 CUBE AUTO

/*new SequentialCommandGroup(
            elevator.setPosition(Elevator.Constants.LOW),
            drivetrain.driveAuto(0, 0.5, 0).withTimeout(1.825),
            drivetrain.driveAuto(0,0,-0.66).withTimeout(0.45),
            drivetrain.driveAuto(0,0.2,0).withTimeout(2.5),
            intake.reverseRollers().withTimeout(2),
            Commands.waitSeconds(1),
            intake.stopRollers(),
            drivetrain.driveAuto(0,-0.2, 0).withTimeout(2.5),
            drivetrain.driveAuto(0,0.0,0),
            elevator.setPosition(Elevator.Constants.STOW));*/
  


    //RIGHT 1 CUBE AUTO

/*new SequentialCommandGroup(
        elevator.setPosition(Elevator.Constants.LOW),
        drivetrain.driveAuto(0, 0.5, 0).withTimeout(1.825),
        drivetrain.driveAuto(0,0,0.66).withTimeout(0.45),
        drivetrain.driveAuto(0,0.2,0).withTimeout(2.5),
        intake.reverseRollers().withTimeout(2),
        Commands.waitSeconds(1),
        intake.stopRollers(),
        drivetrain.driveAuto(0,-0.2, 0).withTimeout(1.5),
        drivetrain.driveAuto(0,0.0,0),
        elevator.setPosition(Elevator.Constants.STOW));*/
    

    } 

    //@Override
    public void autonomousExit() {
        CommandScheduler.getInstance().schedule(drivetrain.stop());
    }
}

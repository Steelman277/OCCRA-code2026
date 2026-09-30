package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Launcher;
import frc.robot.subsystems.Transfer;

public class Robot extends TimedRobot {
    private CommandXboxController driverController;
    private CommandXboxController operatorController;

    private Drivetrain drivetrain;
    private Launcher launcher;
    // private Intake intake;
    private Transfer transfer;

    @Override
    public void robotInit() {
        //TODO set driver controllers, may remove operator controller if only one is required
        driverController = new CommandXboxController(0);
        operatorController = new CommandXboxController(1);

        drivetrain = new Drivetrain(driverController);
        launcher = new Launcher();
        transfer = new Transfer();
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
    }

    @Override
    public void teleopInit() {
        /* - Controller Bindings - */

        // Launching sequence
        //TODO change actuation to intake and outtake
        driverController.leftBumper().onTrue(launcher.reset());
        driverController.rightBumper().onTrue(launcher.launch()); 

        // Start intaking sequence
        driverController.b().onTrue(transfer.runRollers());
        driverController.b().onFalse(transfer.stopRollers());

        // Start intaking sequence
        driverController.a().onTrue(transfer.reverseRollers());
        driverController.a().onFalse(transfer.stopRollers());

        driverController.y().onTrue(transfer.eject());
        driverController.y().onTrue(transfer.stopRollers());
        
        // Cancel intaking sequence
        driverController.x().onTrue(transfer.stopRollers());
        driverController.x().onTrue(transfer.stopRollers());
    }

    @Override
    public void teleopPeriodic() {
        drivetrain.teleopPeriodic();
    }

    @Override
    public void teleopExit() {
        drivetrain.stop();
    }
    // TODO edit Autonomous sequential command
    @Override
    public void autonomousInit() {
        CommandScheduler.getInstance().schedule(launcher.reset());
        CommandScheduler.getInstance().schedule(
            new SequentialCommandGroup(
                drivetrain.driveAuto(0, -0.5, 0).withTimeout(0.4),
                Commands.waitSeconds(0.8),
                launcher.launch(),
                Commands.waitSeconds(0.4),
                drivetrain.driveAuto(0, -0.5, 0).withTimeout(0.45)
            )
        );
    }

    @Override
    public void autonomousExit() {
        drivetrain.stop();
    }
}

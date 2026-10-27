package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;

/** Cumulative drivetrain, intake, transporter, uptake, and launcher robot project. */
public final class Robot extends TimedRobot {
  private final Drivetrain drivetrain = new Drivetrain();
  private final Transporter transporter = new Transporter();
  private final Intake intake = new Intake();
  private final Uptake uptake = new Uptake();
  private final Launcher launcher = new Launcher();
  private final XboxController driver = new XboxController(Constants.DRIVER_PORT);

  @Override public void teleopPeriodic() {
    boolean connected = DriverStation.isJoystickConnected(Constants.DRIVER_PORT)
        && DriverStation.getJoystickIsXbox(Constants.DRIVER_PORT);
    boolean outtaking = connected && driver.getYButton();
    // Transport runs automatically when intake position and sensor interlocks allow.
    if (connected) {
      DriveInputs inputs = DriveInputs.fromXbox(driver.getLeftY(), driver.getLeftX(), driver.getRightX());
      drivetrain.drive(inputs.forward(), inputs.right(), inputs.clockwise(), driver.getLeftBumperButton());
      intake.periodic(driver.getAButtonPressed(), driver.getBButtonPressed(),
          driver.getXButtonPressed(), driver.getStartButtonPressed(), outtaking);
    } else {
      drivetrain.stop(); intake.stop();
    }

    boolean fullShot = connected && driver.getRightTriggerAxis() > Constants.Launcher.TRIGGER_THRESHOLD;
    boolean lowShot = connected && driver.getLeftBumperButton() && !fullShot;
    boolean launching = !outtaking && (fullShot || lowShot);
    boolean ready = launcher.periodic(launching, lowShot);
    transporter.periodic(connected, intake.isDeployed(), launching, ready, outtaking);
    uptake.periodic(connected, intake.isDeployed(), transporter.launcherHealthy(), transporter.launcherTripped(), launching, ready, outtaking);
  }
  @Override public void disabledInit() { launcher.stop(); uptake.stop(); transporter.stop(); drivetrain.stop(); intake.stop(); }
  private final Timer autoTimer = new Timer();
  @Override public void autonomousInit() { 
    launcher.stop(); uptake.stop(); transporter.stop(); drivetrain.stop(); intake.stop();  
    autoTimer.restart();
  }
    double time;
  @Override public void autonomousPeriodic() { 
    intake.stop(); transporter.periodic(false, false); launcher.stop(); uptake.stop(); 
    time = time + 0.02;
    if (time < 3.0) {drivetrain.drive(-0.3,0,0,true);}
    else {drivetrain.stop();}
    if (time < 0.1) intake.periodic(true,false,false,false,false);
  }
  @Override public void teleopInit() { launcher.stop(); uptake.stop(); transporter.stop(); drivetrain.stop(); intake.stop(); }
  @Override public void teleopExit() { launcher.stop(); uptake.stop(); transporter.stop(); drivetrain.stop(); intake.stop(); }
  @Override public void testInit() { launcher.stop(); uptake.stop(); transporter.stop(); drivetrain.stop(); intake.stop(); }
  @Override public void testPeriodic() { drivetrain.stop(); intake.stop(); transporter.periodic(false, false); launcher.stop(); uptake.stop(); }
}















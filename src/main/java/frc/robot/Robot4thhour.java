package frc.robot;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.motorcontrol.PWMTalonSRX;
import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj.Timer;
public class Robot4thhour extends TimedRobot {
  private final PWMTalonSRX frontLeft = new PWMTalonSRX(0);
  private final PWMTalonSRX frontRight = new PWMTalonSRX(1);
  private final PWMTalonSRX backLeft = new PWMTalonSRX(2);
  private final PWMTalonSRX backRight = new PWMTalonSRX(3);
  private final MecanumDrive drive = new MecanumDrive(frontLeft, backLeft, frontRight, backRight);
  private final XboxController driverController = new XboxController(0);
  // Intake motors on PWM ports 4 and 5
  private final PWMTalonSRX intakeLeft = new PWMTalonSRX(4);
  private final PWMTalonSRX intakeRight = new PWMTalonSRX(5);
  private final double intakeSpeed = 0.5;
  // Launcher motors
  private final PWMVictorSPX launcher = new PWMVictorSPX(6);
  private final PWMVictorSPX launcher2 = new PWMVictorSPX(7);
  private final double launcherspeed = .50 ;
  private final double launcherspeed2 = .65 ; 
  // Autonomous chooser
  private SendableChooser<String> autoChooser;
  @Override
  public void robotInit() {
    CameraServer.startAutomaticCapture(0);
    frontRight.setInverted(true);
    backRight.setInverted(true);
    // Create the chooser
    autoChooser = new SendableChooser<>();
    // Add autonomous options
    autoChooser.setDefaultOption("Middle 4 ball", "middle4ball");
    autoChooser.addOption("Left 4 ball", "left4ball");
    autoChooser.addOption("Right 4 ball", "right4ball");
    // Publish chooser to NetworkTables / SmartDashboard
    SmartDashboard.putData("Auto Mode", autoChooser);
  }
    public void autonomousInit() {
    String selectedAuto = autoChooser.getSelected();
    if (selectedAuto == null) {
      selectedAuto = "middle4ball";
    }
    switch (selectedAuto) {
      case "middle4ball":
        // Middle 4 ball autonomous
        launcher.set(-launcherspeed2);
        launcher2.set(-launcherspeed2);
        intakeLeft.set(-intakeSpeed);
        intakeRight.set(-intakeSpeed);
         Timer.delay(3);
        launcher.set(0);
        launcher2.set(0);
        intakeLeft.set(0);
        intakeRight.set(0);
      drive.driveCartesian(0, 0, .5);
                Timer.delay(.1);
      drive.driveCartesian(0, 0, .5);
          Timer.delay(.1);
      drive.driveCartesian(0, 0, .5);
                Timer.delay(.1);
      drive.driveCartesian(0, 0, .5);
       Timer.delay(.1);
      drive.driveCartesian(0, 0, .5);
       Timer.delay(.1);
      drive.driveCartesian(0, 0, .5);
      Timer.delay(.1);
      drive.driveCartesian(0, 0, .5);
       Timer.delay(.1);
      drive.driveCartesian(0, 0, .5);
      Timer.delay(.1);
      drive.driveCartesian(0, 0, .5);
      Timer.delay(.5);
       intakeLeft.set(-intakeSpeed);
         intakeRight.set(-intakeSpeed); 
         drive.driveCartesian(.4, 0, 0);
          Timer.delay(.1);
         drive.driveCartesian(.4, 0, 0);
         Timer.delay(.1);
         drive.driveCartesian(.4, 0, 0);
          Timer.delay(.1);
         drive.driveCartesian(.4, 0, 0);
         Timer.delay(.1);
         drive.driveCartesian(.4, 0, 0);
                Timer.delay(.1);
         drive.driveCartesian(.4, 0, 0);
          Timer.delay(.1);
         drive.driveCartesian(.4, 0, 0);
         Timer.delay(.1);
         drive.driveCartesian(.4, 0, 0);
          Timer.delay(.1);
         drive.driveCartesian(.4, 0, 0);
         Timer.delay(.1);
         drive.driveCartesian(.4, 0, 0);
          Timer.delay(3);
         intakeLeft.set(0);
         intakeRight.set(0);
         Timer.delay(.5);
         drive.driveCartesian(0, 0, .5);
                Timer.delay(.1);
      drive.driveCartesian(0, 0, .5);
          Timer.delay(.1);
      drive.driveCartesian(0, 0, .5);
                Timer.delay(.1);
      drive.driveCartesian(0, 0, .5);
       Timer.delay(.1);
      drive.driveCartesian(0, 0, .5);
       Timer.delay(.1);
      drive.driveCartesian(0, 0, .5);
      Timer.delay(.1);
      drive.driveCartesian(0, 0, .5);
      Timer.delay(.1);
      drive.driveCartesian(0, 0, .5);
      Timer.delay(.5);
         drive.driveCartesian(.6, 0, 0);
         Timer.delay(.1);
         drive.driveCartesian(.6, 0, 0);
          Timer.delay(.1);
         drive.driveCartesian(.6, 0, 0);
         Timer.delay(.1);
         drive.driveCartesian(.6, 0, 0);
         Timer.delay(.1);
         drive.driveCartesian(.6, 0, 0);
        break;
      case "left4ball":
        // Left 4 ball autonomous
         //autoChooser.addOption("Left 4 ball",new SequentialCommandGroup());     TODO I had to comment this out to make it compile, if an auto is missing this is why
        break;
      case "right4ball":
        // Right 4 ball autonomous
       launcher.set(-launcherspeed2);
       launcher2.set(-launcherspeed2);
       intakeLeft.set(-intakeSpeed);
       intakeRight.set(-intakeSpeed);
        Timer.delay(3);
       launcher.set(0);
       launcher2.set(0);
       intakeLeft.set(0);
       intakeRight.set(0);
       drive.driveCartesian(0, 0, 20);
        Timer.delay(1);
       drive.driveCartesian(10, 0, 0);
       intakeLeft.set(-intakeSpeed);
       intakeRight.set(-intakeSpeed);
        Timer.delay(5);
       intakeLeft.set(0);
       intakeRight.set(0);
       drive.driveCartesian(0, 0, 20);
        Timer.delay(2);
       drive.driveCartesian(10, 0, 0);
        Timer.delay(4);
        break;
      default:
        // Safe fallback
        drive.driveCartesian(0, 0, 0);
        break;
        }
       }
  @Override
  public void teleopPeriodic() {
    double forwardSpeed = -driverController.getLeftY();
    double strafeSpeed = driverController.getLeftX();
    double rotationSpeed = driverController.getRightX();
    drive.driveCartesian(forwardSpeed, strafeSpeed, rotationSpeed);
    if (driverController.getRightTriggerAxis() > 0.1) {
      launcher.set(launcherspeed * driverController.getRightTriggerAxis());
      launcher2.set(launcherspeed * driverController.getRightTriggerAxis());
    }else if (driverController.getRightBumperButton()){
      launcher.set(-launcherspeed);
      launcher2.set(-launcherspeed);
    }else if (driverController.getAButton()){
      launcher.set(-launcherspeed2);
      launcher2.set(-launcherspeed2);
    }else {
      launcher.set(0);
      launcher2.set(0);
    }
    if (driverController.getLeftTriggerAxis() > 0.1) {
      intakeLeft.set(intakeSpeed * driverController.getLeftTriggerAxis());
      intakeRight.set(intakeSpeed * driverController.getLeftTriggerAxis());
    } else if (driverController.getLeftBumperButton()) {
      intakeLeft.set(-intakeSpeed);
      intakeRight.set(-intakeSpeed);
     } else {
      intakeLeft.set(0);
      intakeRight.set(0);
    }
  }
}

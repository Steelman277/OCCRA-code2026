package frc.robot;


import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.motorcontrol.PWMTalonSRX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class Robot extends TimedRobot {
  private final PWMTalonSRX frontLeft = new PWMTalonSRX(0);
  private final PWMTalonSRX frontRight = new PWMTalonSRX(1);
  private final PWMTalonSRX backLeft = new PWMTalonSRX(2);
  private final PWMTalonSRX backRight = new PWMTalonSRX(3);

  private final MecanumDrive drive = new MecanumDrive(frontLeft, backLeft, frontRight, backRight);
  private final XboxController driverController = new XboxController(0);

  // Intake motors on PWM ports 4 and 5
  private final PWMTalonSRX intakeLeft = new PWMTalonSRX(4);
  private final PWMTalonSRX intakeRight = new PWMTalonSRX(5);
  private final double intakeSpeed = 0.7;

  // Launcher motors
  private final PWMTalonSRX launcher = new PWMTalonSRX(6);
  private final PWMTalonSRX launcher2 = new PWMTalonSRX(7);
  private final double launcherspeed = .50 ;
  private final double launcherspeed2 = .67 ; 
  public class Launcher extends SubsystemBase{
public static class constants{
  public static double launcher = .50 ;
  public static double launcherspeed2 = .67;
}
  };

  @Override
  public void teleopPeriodic() {
    double forwardSpeed = -driverController.getLeftY();
    double strafeSpeed = driverController.getLeftX();
    double rotationSpeed = driverController.getRightX();

    drive.driveCartesian(forwardSpeed, strafeSpeed, rotationSpeed);

    if (driverController.getRightTriggerAxis() > 0.1) {
      launcher.set(launcherspeed);
      launcher2.set(launcherspeed);
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
      intakeLeft.set(intakeSpeed);
      intakeRight.set(intakeSpeed);
    } else if (driverController.getLeftBumperButton()) {
      intakeLeft.set(-intakeSpeed);
      intakeRight.set(-intakeSpeed);
     } else {
      intakeLeft.set(0);
      intakeRight.set(0);
    }
  }
}


package frc.robot;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.drive.MecanumDrive;

/** Four independent CIM motors, each controlled by a CAN Talon SRX. */
public final class Drivetrain {
  private boolean configured = true;
  private final WPI_TalonSRX frontLeft = motor(Constants.FRONT_LEFT_ID, Constants.FRONT_LEFT_INVERTED);
  private final WPI_TalonSRX rearLeft = motor(Constants.REAR_LEFT_ID, Constants.REAR_LEFT_INVERTED);
  private final WPI_TalonSRX frontRight = motor(Constants.FRONT_RIGHT_ID, Constants.FRONT_RIGHT_INVERTED);
  private final WPI_TalonSRX rearRight = motor(Constants.REAR_RIGHT_ID, Constants.REAR_RIGHT_INVERTED);
  // WPILib constructor order is FL, RL, FR, RR.
  private final MecanumDrive drive = new MecanumDrive(frontLeft, rearLeft, frontRight, rearRight);

  public Drivetrain() {
    drive.setDeadband(0.0); // Applied once to each joystick axis in DriveInputs.
    drive.setExpiration(0.1);
    drive.setSafetyEnabled(true);
  }

  private WPI_TalonSRX motor(int id, boolean inverted) {
    WPI_TalonSRX motor = new WPI_TalonSRX(id);
    ErrorCode status = motor.configFactoryDefault(100);
    if (status != ErrorCode.OK) {
      configured = false;
      DriverStation.reportError("Talon SRX CAN " + id + " configuration failed: " + status
          + ". Fix CAN/power and restart robot code.", false);
    }
    motor.setInverted(inverted);
    motor.setNeutralMode(NeutralMode.Brake);
    return motor;
  }

  /** Inputs follow WPILib: forward, right, clockwise are positive. */
  public void drive(double forward, double right, double clockwise, boolean slow) {
    if (!configured) {
      stop();
      return;
    }
    drive.setMaxOutput(slow ? Constants.SLOW_OUTPUT : Constants.MAX_OUTPUT);
    drive.driveCartesian(forward, right, clockwise);
  }

  public void stop() {
    drive.stopMotor();
  }
}






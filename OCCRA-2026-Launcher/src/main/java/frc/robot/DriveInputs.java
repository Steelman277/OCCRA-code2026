package frc.robot;

import edu.wpi.first.math.MathUtil;

/** Converts Xbox axes to WPILib 2026.2.1 MecanumDrive implementation coordinates. */
public record DriveInputs(double forward, double right, double clockwise) {
  public static DriveInputs fromXbox(double leftY, double leftX, double rightX) {
    return new DriveInputs(-axis(leftY), axis(leftX), axis(rightX));
  }

  private static double axis(double value) {
    return MathUtil.applyDeadband(value, Constants.DEADBAND);
  }
}






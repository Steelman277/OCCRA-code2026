package frc.robot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import org.junit.jupiter.api.Test;

class DriveInputsTest {
  @Test void centeredSticksDoNotMove() {
    DriveInputs input = DriveInputs.fromXbox(0.07, -0.07, 0);
    assertEquals(0, input.forward(), 1e-9);
    assertEquals(0, input.right(), 1e-9);
    assertEquals(0, input.clockwise(), 1e-9);
  }

  @Test void forwardCommandsAllWheelsForward() {
    var wheels = wheels(-1, 0, 0);
    assertEquals(1, wheels.frontLeft, 1e-9);
    assertEquals(1, wheels.frontRight, 1e-9);
    assertEquals(1, wheels.rearLeft, 1e-9);
    assertEquals(1, wheels.rearRight, 1e-9);
  }

  @Test void rightStrafeCommandsOppositeDiagonals() {
    var wheels = wheels(0, 1, 0);
    assertEquals(1, wheels.frontLeft, 1e-9);
    assertEquals(-1, wheels.frontRight, 1e-9);
    assertEquals(-1, wheels.rearLeft, 1e-9);
    assertEquals(1, wheels.rearRight, 1e-9);
  }

  @Test void rightStickTurnsClockwise() {
    var wheels = wheels(0, 0, 1);
    assertEquals(1, wheels.frontLeft, 1e-9);
    assertEquals(-1, wheels.frontRight, 1e-9);
    assertEquals(1, wheels.rearLeft, 1e-9);
    assertEquals(-1, wheels.rearRight, 1e-9);
  }

  @Test void combinedInputsStayNormalized() {
    var wheels = wheels(-1, 1, 1);
    for (double output : new double[]{wheels.frontLeft, wheels.frontRight, wheels.rearLeft, wheels.rearRight}) {
      assertTrue(Math.abs(output) <= 1.0);
    }
  }

  private MecanumDrive.WheelSpeeds wheels(double y, double x, double rotation) {
    var input = DriveInputs.fromXbox(y, x, rotation);
    return MecanumDrive.driveCartesianIK(input.forward(), input.right(), input.clockwise());
  }
}






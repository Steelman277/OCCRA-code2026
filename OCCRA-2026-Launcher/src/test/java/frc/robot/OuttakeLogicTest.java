package frc.robot;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
class OuttakeLogicTest {
  @Test void heldOverridesOccupiedOrForwardFlow() {
    assertEquals(-8, OuttakeLogic.volts(true, true, 0, -8));
    assertEquals(-8, OuttakeLogic.volts(true, true, 7.5, -8));
  }
  @Test void releaseRestoresNormalOutput() {
    assertEquals(-8, OuttakeLogic.volts(true, true, 3, -8));
    assertEquals(3, OuttakeLogic.volts(false, true, 3, -8));
    assertEquals(0, OuttakeLogic.volts(false, true, 0, -8));
  }
  @Test void interlocksAndInvalidVoltageBlockReverse() {
    assertEquals(0, OuttakeLogic.volts(true, false, 3, -8));
    for (double v : new double[]{1, -13, Double.NaN})
      assertEquals(0, OuttakeLogic.volts(true, true, 3, v));
  }
}

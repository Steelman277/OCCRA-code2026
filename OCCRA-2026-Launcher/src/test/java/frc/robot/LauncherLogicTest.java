package frc.robot;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
class LauncherLogicTest {
  @Test void switchingShotPowerRestartsDelayInBothDirections() {
    var l = new LauncherLogic(); double d = Constants.Launcher.SPINUP_SECONDS;
    assertFalse(l.update(true, true, true, 0));
    assertTrue(l.update(true, true, true, d));
    assertFalse(l.update(true, false, true, d + 1));
    assertTrue(l.update(true, false, true, 2 * d + 1));
    assertFalse(l.update(true, true, true, 2 * d + 2));
    assertTrue(l.update(true, true, true, 3 * d + 2));
  }
  @Test void changingShotCannotClearFault() {
    var l = new LauncherLogic();
    assertFalse(l.update(true, true, false, 0));
    assertFalse(l.update(true, false, true, 10));
    assertEquals(LauncherLogic.State.FAULT, l.state());
  }
  @Test void waitsForFullDelay() {
    var l = new LauncherLogic(); double d = Constants.Launcher.SPINUP_SECONDS;
    assertFalse(l.update(true, true, 10));
    assertFalse(l.update(true, true, 10 + d / 2));
    assertTrue(l.update(true, true, 10 + d));
    assertTrue(l.update(true, true, 20 + d));
  }
  @Test void releaseRestartsDelay() {
    var l = new LauncherLogic(); double d = Constants.Launcher.SPINUP_SECONDS;
    l.update(true, true, 0); assertTrue(l.update(true, true, d));
    assertFalse(l.update(false, true, d + 1));
    assertFalse(l.update(true, true, d + 2));
  }
  @Test void failureBlocksUntilReleased() {
    var l = new LauncherLogic();
    assertFalse(l.update(true, false, 0));
    assertFalse(l.update(true, true, 10));
    assertEquals(LauncherLogic.State.FAULT, l.state());
    l.update(false, true, 11);
    assertFalse(l.update(true, true, 12));
  }
  @Test void stopClearsTimer() {
    var l = new LauncherLogic(); l.update(true, true, 0);
    l.reset(); assertEquals(LauncherLogic.State.IDLE, l.state());
    assertFalse(l.update(true, true, 100));
  }
}

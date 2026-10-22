package frc.robot;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class LauncherLogicTest {
  @Test void bothWheelsMustSettleBeforeFeeding() {
    var l = new LauncherLogic();
    assertFalse(l.update(true, true, 0, 0, 0));
    assertFalse(l.update(true, true, 3000, 2000, 1));
    assertFalse(l.update(true, true, 3000, 3000, 2));
    assertFalse(l.update(true, true, 3000, 3000, 2.1));
    assertTrue(l.update(true, true, 3000, 3000, 2.21));
  }
  @Test void speedDropStopsFeedAndRequiresRequalification() {
    var l = new LauncherLogic();
    l.update(true, true, 3000, 3000, 0);
    assertTrue(l.update(true, true, 3000, 3000, .21));
    assertFalse(l.update(true, true, 2800, 3000, .3));
    assertFalse(l.update(true, true, 3000, 3000, .4));
    assertTrue(l.update(true, true, 3000, 3000, .61));
  }
  @Test void wrongDirectionAndOverspeedAreNotReady() {
    var l = new LauncherLogic();
    l.update(true, true, 3000, -3000, 0);
    assertFalse(l.update(true, true, 3000, -3000, 1));
    assertFalse(l.update(true, true, 3400, 3400, 2));
  }
  @Test void badFeedbackLatchesUntilRelease() {
    var l = new LauncherLogic();
    l.update(true, true, 3000, 3000, 0);
    assertTrue(l.update(true, true, 3000, 3000, .21));
    assertFalse(l.update(true, false, 3000, 3000, .3));
    assertFalse(l.update(true, true, 3000, 3000, 1));
    assertFalse(l.update(false, true, 3000, 3000, 2));
    assertEquals(LauncherLogic.State.IDLE, l.state());
    assertFalse(l.update(true, true, 3000, 3000, 3));
    assertTrue(l.update(true, true, 3000, 3000, 3.21));
  }
  @Test void timeoutStopsSpinup() {
    var l = new LauncherLogic();
    l.update(true, true, 100, 100, 0);
    assertFalse(l.update(true, true, 100, 100, 5));
    assertEquals(LauncherLogic.State.FAULT, l.state());
  }
  @Test void nonFiniteFeedbackCannotFeed() {
    var l = new LauncherLogic();
    assertFalse(l.update(true, true, Double.NaN, 3000, 0));
    assertEquals(LauncherLogic.State.FAULT, l.state());
  }
  @Test void releaseAndStopRemoveReady() {
    var l = new LauncherLogic(); l.update(true, true, 3000, 3000, 0);
    assertTrue(l.update(true, true, 3000, 3000, .21));
    assertFalse(l.update(false, true, 3000, 3000, .3));
    l.reset(); assertEquals(LauncherLogic.State.IDLE, l.state());
  }
}





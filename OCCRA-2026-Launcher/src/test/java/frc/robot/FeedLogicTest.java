package frc.robot;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class FeedLogicTest {
  @Test void spinupPreservesNormalSensorRules() {
    for (boolean transportTripped : new boolean[]{false, true}) {
      for (boolean launcherTripped : new boolean[]{false, true}) {
        boolean transport = TransporterLogic.shouldRun(true, true, true, transportTripped, launcherTripped);
        boolean uptake = UptakeLogic.shouldRun(true, true, true, true, launcherTripped);
        assertEquals(transport, FeedLogic.shouldRun(true, false, transport, true, true));
        assertEquals(uptake, FeedLogic.shouldRun(true, false, uptake, true, true));
      }
    }
  }
  @Test void readyOverridesOccupiedSensorStops() {
    boolean normalTransport = TransporterLogic.shouldRun(true, true, true, true, true);
    boolean normalUptake = UptakeLogic.shouldRun(true, true, true, true, true);
    assertFalse(normalTransport); assertFalse(normalUptake);
    assertTrue(FeedLogic.shouldRun(true, true, normalTransport, true, true));
    assertTrue(FeedLogic.shouldRun(true, true, normalUptake, true, true));
  }
  @Test void launchNeverOverridesIntakeOrHealth() {
    assertFalse(FeedLogic.shouldRun(true, true, true, false, true));
    assertFalse(FeedLogic.shouldRun(true, true, true, true, false));
    assertFalse(FeedLogic.shouldRun(true, false, true, false, true));
    assertFalse(FeedLogic.shouldRun(true, false, true, true, false));
  }
  @Test void releasingTriggerRestoresNormalSensorRules() {
    assertFalse(FeedLogic.shouldRun(false, true, false, true, true));
    assertTrue(FeedLogic.shouldRun(false, false, true, true, true));
  }
  @Test void timedLaunchPreservesCollectionThenOverridesOccupancy() {
    var launcher = new LauncherLogic();
    boolean ready = launcher.update(true, true, 0);
    assertTrue(FeedLogic.shouldRun(true, ready, true, true, true));
    assertFalse(FeedLogic.shouldRun(true, ready, false, true, true));
    ready = launcher.update(true, true, Constants.Launcher.SPINUP_SECONDS);
    assertTrue(FeedLogic.shouldRun(true, ready, false, true, true));
    ready = launcher.update(false, true, Constants.Launcher.SPINUP_SECONDS + 1);
    assertFalse(FeedLogic.shouldRun(false, ready, false, true, true));
  }
}

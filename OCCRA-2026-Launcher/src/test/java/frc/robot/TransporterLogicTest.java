package frc.robot;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class TransporterLogicTest {
  @Test void intakeNotDeployedBlocksEverySensorCombination() {
    for (boolean t : new boolean[]{false, true})
      for (boolean l : new boolean[]{false, true})
        assertFalse(TransporterLogic.shouldRun(true, true, false, t, l));
  }

  @Test void actualIntakeMotionGatesTransporterBeforeDuringAndAfterTravel() {
    var intake = new IntakeMotion();
    assertFalse(canRun(intake));
    intake.toggleDeployment(0);
    assertFalse(canRun(intake));
    intake.update(0.30, 22, true); intake.update(0.46, 22, true);
    assertTrue(canRun(intake));
    intake.toggleRollers();
    assertTrue(canRun(intake)); // X only pauses intake rollers.
    intake.toggleDeployment(1);
    assertFalse(canRun(intake)); // Stop in the same cycle retraction starts.
    intake.stop(); assertFalse(canRun(intake));
    intake.fail("test"); assertFalse(canRun(intake));
  }

  private boolean canRun(IntakeMotion intake) {
    return TransporterLogic.shouldRun(true, true,
        intake.state() == IntakeMotion.State.DEPLOYED, true, false);
  }
  @Test void bothOccupiedStops() {
    assertFalse(TransporterLogic.shouldRun(true, true, true, true, true));
  }
  @Test void transportOccupiedLauncherClearRuns() {
    assertTrue(TransporterLogic.shouldRun(true, true, true, true, false));
  }
  @Test void bothClearRuns() {
    assertTrue(TransporterLogic.shouldRun(true, true, true, false, false));
  }
  @Test void launcherOccupiedTransportClearRuns() {
    assertTrue(TransporterLogic.shouldRun(true, true, true, false, true));
  }
  @Test void disabledOrDisconnectedStopsEveryCombination() {
    for (boolean t : new boolean[]{false, true})
      for (boolean l : new boolean[]{false, true})
        assertFalse(TransporterLogic.shouldRun(false, true, true, t, l));
  }
  @Test void unhealthySensorsNeverPermitMotion() {
    for (boolean t : new boolean[]{false, true})
      for (boolean l : new boolean[]{false, true})
        assertFalse(TransporterLogic.shouldRun(true, false, true, t, l));
  }
  @Test void automaticallyResumesAfterSpaceOpens() {
    assertTrue(TransporterLogic.shouldRun(true, true, true, true, false));
    assertFalse(TransporterLogic.shouldRun(true, true, true, true, true));
    assertTrue(TransporterLogic.shouldRun(true, true, true, true, false));
  }
}








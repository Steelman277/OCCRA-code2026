package frc.robot;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class UptakeLogicTest {
  @Test void deployedAndClearRuns() {
    assertTrue(UptakeLogic.shouldRun(true, true, true, true, false));
  }
  @Test void trippedStopsAndClearingResumes() {
    assertFalse(UptakeLogic.shouldRun(true, true, true, true, true));
    assertTrue(UptakeLogic.shouldRun(true, true, true, true, false));
  }
  @Test void anyMissingPermissionStopsRegardlessOfDetection() {
    for (boolean tripped : new boolean[]{false, true}) {
      assertFalse(UptakeLogic.shouldRun(false, true, true, true, tripped));
      assertFalse(UptakeLogic.shouldRun(true, false, true, true, tripped));
      assertFalse(UptakeLogic.shouldRun(true, true, false, true, tripped));
      assertFalse(UptakeLogic.shouldRun(true, true, true, false, tripped));
    }
  }
  @Test void intakeLifecycleGatesUptake() {
    var intake = new IntakeMotion();
    assertFalse(canRun(intake));
    intake.toggleDeployment(0); assertFalse(canRun(intake));
    intake.update(0.30, 22, true); intake.update(0.46, 22, true);
    assertTrue(canRun(intake));
    intake.toggleRollers(); assertTrue(canRun(intake));
    intake.toggleDeployment(1); assertFalse(canRun(intake));
    intake.stop(); assertFalse(canRun(intake));
    intake.fail("test"); assertFalse(canRun(intake));
  }
  private boolean canRun(IntakeMotion intake) {
    return UptakeLogic.shouldRun(true, true,
        intake.state() == IntakeMotion.State.DEPLOYED, true, false);
  }
}





package frc.robot;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class IntakeMotionTest {
  // Simulate endpoint contact above both tuned thresholds, not a fixed commissioning current.
  private static final double CONTACT_AMPS = Math.max(
      Constants.Intake.DEPLOY_STOP_AMPS, Constants.Intake.RETRACT_STOP_AMPS) + 1.0;
  private IntakeMotion deployed() {
    var m = new IntakeMotion();
    m.toggleDeployment(0);
    m.update(0.30, CONTACT_AMPS, true);
    m.update(0.46, CONTACT_AMPS, true);
    return m;
  }

  @Test void toggleDeploysAndAutomaticallyStartsRollersAtEndpoint() {
    var m = new IntakeMotion(); m.toggleDeployment(0);
    assertEquals(IntakeMotion.State.DEPLOYING, m.state());
    assertEquals(0, m.rollerVolts());
    m.update(0.30, CONTACT_AMPS, true); m.update(0.46, CONTACT_AMPS, true);
    assertEquals(Constants.Intake.ROLLER_VOLTS, m.rollerVolts());
    m.toggleDeployment(1);
    assertEquals(IntakeMotion.State.RETRACTING, m.state());
    assertEquals(0, m.rollerVolts());
  }

  @Test void rollerTogglePausesAndRestartsOnlyWhenDeployed() {
    var m = deployed();
    m.toggleRollers(); assertEquals(0, m.rollerVolts());
    m.toggleRollers(); assertEquals(Constants.Intake.ROLLER_VOLTS, m.rollerVolts());
    m.toggleDeployment(1); m.toggleRollers(); assertEquals(0, m.rollerVolts());
  }

  @Test void nextDeploymentResetsRollerPause() {
    var m = deployed(); m.toggleRollers(); m.toggleDeployment(1);
    m.update(1.30, CONTACT_AMPS, true); m.update(1.46, CONTACT_AMPS, true);
    m.toggleDeployment(2); m.toggleRollers();
    assertEquals(0, m.rollerVolts());
    m.update(2.30, CONTACT_AMPS, true); m.update(2.46, CONTACT_AMPS, true);
    assertEquals(Constants.Intake.ROLLER_VOLTS, m.rollerVolts());
  }

  @Test void toggleIgnoredInMotionAndUnknownAndFault() {
    var m = new IntakeMotion(); m.toggleDeployment(0); m.toggleDeployment(0.1);
    assertEquals(IntakeMotion.State.DEPLOYING, m.state());
    m.stop(); m.toggleDeployment(1);
    assertEquals(IntakeMotion.State.UNKNOWN, m.state());
    m.fail("test"); m.toggleDeployment(2); m.toggleRollers();
    assertEquals(IntakeMotion.State.FAULT, m.state()); assertEquals(0, m.rollerVolts());
  }

  @Test void disableStopsRollersUntilExplicitRestart() {
    var m = deployed(); m.stop();
    assertEquals(0, m.rollerVolts());
    m.update(1, 0, true); assertEquals(0, m.rollerVolts());
    m.toggleRollers(); assertEquals(Constants.Intake.ROLLER_VOLTS, m.rollerVolts());
  }
  @Test void startsRetractedWithoutDriving() {
    var m = new IntakeMotion();
    assertEquals(IntakeMotion.State.RETRACTED, m.state());
    assertEquals(0, m.update(0, 0, true));
    m.request(false, 0);
    assertFalse(m.moving());
  }
  @Test void ignoresStartupAndBriefSpikes() {
    var m = new IntakeMotion(); m.request(true, 0);
    assertTrue(m.update(0.10, CONTACT_AMPS, true) > 0);
    m.update(0.30, CONTACT_AMPS, true);
    m.update(0.35, 0, true);
    assertTrue(m.update(0.55, CONTACT_AMPS, true) > 0);
    assertEquals(IntakeMotion.State.DEPLOYING, m.state());
  }
  @Test void sustainedCurrentStopsDeploymentAndRetraction() {
    var m = new IntakeMotion(); m.request(true, 0);
    m.update(0.30, CONTACT_AMPS, true);
    assertEquals(0, m.update(0.46, CONTACT_AMPS, true));
    assertEquals(IntakeMotion.State.DEPLOYED, m.state());
    m.request(false, 1);
    assertTrue(m.update(1.1, 0, true) < 0);
    m.update(1.30, CONTACT_AMPS, true);
    assertEquals(0, m.update(1.46, CONTACT_AMPS, true));
    assertEquals(IntakeMotion.State.RETRACTED, m.state());
  }
  @Test void timeoutLatchesUntilExplicitReset() {
    var m = new IntakeMotion(); m.request(true, 0);
    assertEquals(0, m.update(3.01, 0, true));
    assertEquals(IntakeMotion.State.FAULT, m.state());
    m.request(false, 4); assertEquals(IntakeMotion.State.FAULT, m.state());
    m.clearFault(); assertEquals(IntakeMotion.State.UNKNOWN, m.state());
    m.request(false, 5); assertTrue(m.update(5.1, 0, true) < 0);
  }
  @Test void staleAndInvalidFeedbackStopMotion() {
    for (boolean stale : new boolean[]{true, false}) {
      var m = new IntakeMotion(); m.request(true, 0);
      assertEquals(0, m.update(0.1, stale ? 0 : Double.NaN, !stale));
      assertEquals(IntakeMotion.State.FAULT, m.state());
    }
  }
  @Test void interruptionDoesNotPretendToBeRetracted() {
    var m = new IntakeMotion(); m.request(true, 0); m.stop();
    assertEquals(IntakeMotion.State.UNKNOWN, m.state());
    assertEquals(0, m.update(1, 0, true));
  }
  @Test void repeatedRequestCannotResetTimeout() {
    var m = new IntakeMotion(); m.request(true, 0); m.request(true, 2.9);
    assertEquals(0, m.update(3.01, 0, true));
    assertEquals(IntakeMotion.State.FAULT, m.state());
  }
  @Test void reversalRestartsSpikeQualification() {
    var m = new IntakeMotion(); m.request(true, 0); m.update(0.30, CONTACT_AMPS, true);
    m.request(false, 0.4);
    assertTrue(m.update(0.46, CONTACT_AMPS, true) < 0);
    assertEquals(IntakeMotion.State.RETRACTING, m.state());
  }
}






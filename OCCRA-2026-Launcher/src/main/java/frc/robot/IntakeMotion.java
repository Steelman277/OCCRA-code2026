package frc.robot;

/** Hardware-independent rack state machine. Current indicates contact, not absolute position. */
public final class IntakeMotion {
  public enum State { RETRACTED, DEPLOYING, DEPLOYED, RETRACTING, UNKNOWN, FAULT }
  // User-specified starting condition; never automatically re-assume this on mode changes.
  private State state = State.RETRACTED;
  private double started;
  private double highSince = Double.NaN;
  private String fault = "";
  private boolean rollersEnabled;

  public void toggleDeployment(double now) {
    if (state == State.RETRACTED) request(true, now);
    else if (state == State.DEPLOYED) request(false, now);
  }

  public void toggleRollers() {
    if (state == State.DEPLOYED && rollersEnabled) rollersEnabled = false;
    else if (state == State.DEPLOYED && !rollersEnabled) rollersEnabled = true;
  }

  public double rollerVolts() {
    return state == State.DEPLOYED && rollersEnabled ? Constants.Intake.ROLLER_VOLTS : 0;
  }

  public State state() { return state; }
  public String faultReason() { return fault; }
  public boolean moving() { return state == State.DEPLOYING || state == State.RETRACTING; }

  public void request(boolean deploy, double now) {
    if (state == State.FAULT) return;
    State target = deploy ? State.DEPLOYED : State.RETRACTED;
    State movement = deploy ? State.DEPLOYING : State.RETRACTING;
    if (state == target || state == movement) return;
    state = movement;
    rollersEnabled = false;
    started = now;
    highSince = Double.NaN;
  }

  public double update(double now, double current, boolean fresh) {
    if (!moving()) return 0;
    if (!fresh || !Double.isFinite(current)) {
      fail("Deploy current signal unavailable or stale");
      return 0;
    }
    if (now - started >= Constants.Intake.TRAVEL_TIMEOUT_SECONDS) {
      fail("Rack travel timed out without a confirmed current stop");
      return 0;
    }
    double threshold = state == State.DEPLOYING
        ? Constants.Intake.DEPLOY_STOP_AMPS : Constants.Intake.RETRACT_STOP_AMPS;
    if (now - started >= Constants.Intake.STARTUP_IGNORE_SECONDS && current >= threshold) {
      if (Double.isNaN(highSince)) highSince = now;
      if (now - highSince >= Constants.Intake.SPIKE_HOLD_SECONDS) {
        state = state == State.DEPLOYING ? State.DEPLOYED : State.RETRACTED;
        rollersEnabled = state == State.DEPLOYED;
        return 0;
      }
    } else {
      highSince = Double.NaN;
    }
    return state == State.DEPLOYING ? Constants.Intake.DEPLOY_VOLTS : Constants.Intake.RETRACT_VOLTS;
  }

  public void stop() {
    rollersEnabled = false;
    if (moving()) state = State.UNKNOWN;
    highSince = Double.NaN;
  }

  public void fail(String reason) { state = State.FAULT; fault = reason; }
  public void clearFault() {
    if (state == State.FAULT) { state = State.UNKNOWN; fault = ""; }
  }
}





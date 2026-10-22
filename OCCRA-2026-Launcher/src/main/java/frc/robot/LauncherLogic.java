package frc.robot;

/** Qualifies BOTH signed wheel speeds; any dropout immediately removes feed permission. */
public final class LauncherLogic {
  public enum State { IDLE, SPINNING_UP, READY, FAULT }
  private State state = State.IDLE;
  private double waitingSince;
  private double stableSince = Double.NaN;

  public State state() { return state; }
  public void reset() { state = State.IDLE; stableSince = Double.NaN; }
  public void fail() { state = State.FAULT; stableSince = Double.NaN; }

  public boolean update(boolean requested, boolean healthy, double leftRpm, double rightRpm, double now) {
    if (!requested) { reset(); return false; }
    if (!healthy || !Double.isFinite(leftRpm) || !Double.isFinite(rightRpm)) fail();
    if (state == State.FAULT) return false;
    if (state == State.IDLE) { state = State.SPINNING_UP; waitingSince = now; }
    boolean atSpeed = Math.abs(leftRpm - Constants.Launcher.TARGET_RPM) <= Constants.Launcher.TOLERANCE_RPM
        && Math.abs(rightRpm - Constants.Launcher.TARGET_RPM) <= Constants.Launcher.TOLERANCE_RPM;
    if (!atSpeed) {
      stableSince = Double.NaN;
      if (state == State.READY) { state = State.SPINNING_UP; waitingSince = now; }
    } else {
      if (Double.isNaN(stableSince)) stableSince = now;
      if (now - stableSince >= Constants.Launcher.READY_HOLD_SECONDS) state = State.READY;
    }
    if (state != State.READY && now - waitingSince >= Constants.Launcher.SPINUP_TIMEOUT_SECONDS) fail();
    return state == State.READY;
  }
}





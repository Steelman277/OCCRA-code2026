package frc.robot;

/** Open-loop launcher: feed permission is based on elapsed time, not measured speed. */
public final class LauncherLogic {
  public enum State { IDLE, SPINNING_UP, READY, FAULT }
  private State state = State.IDLE;
  private double started;
  private boolean previousLowShot;
  public State state() { return state; }
  public void reset() { state = State.IDLE; }
  public void fail() { state = State.FAULT; }
  public boolean update(boolean requested, boolean healthy, double now) {
    return update(requested, false, healthy, now);
  }
  public boolean update(boolean requested, boolean lowShot, boolean healthy, double now) {
    if (!requested) { reset(); return false; }
    if (!healthy || !Double.isFinite(now)) fail();
    if (state == State.FAULT) return false;
    // A power change needs a fresh delay; it must never clear a latched fault.
    if (state == State.IDLE || lowShot != previousLowShot) {
      started = now; state = State.SPINNING_UP;
    }
    previousLowShot = lowShot;
    if (now - started >= Constants.Launcher.SPINUP_SECONDS) state = State.READY;
    return state == State.READY;
  }
}

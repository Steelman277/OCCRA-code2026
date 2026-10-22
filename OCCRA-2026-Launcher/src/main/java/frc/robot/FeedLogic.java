package frc.robot;

/** Normal collection continues until launch speed qualifies an occupancy override. */
public final class FeedLogic {
  private FeedLogic() {}
  public static boolean shouldRun(boolean launchRequested, boolean launcherReady,
      boolean normalRun, boolean healthy, boolean intakeDeployed) {
    return healthy && intakeDeployed && (normalRun || (launchRequested && launcherReady));
  }
}





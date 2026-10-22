package frc.robot;

/** Pure sensor interlock, independent of CAN hardware. */
public final class TransporterLogic {
  private TransporterLogic() {}
  public static boolean shouldRun(boolean enabled, boolean healthy, boolean intakeDeployed,
      boolean transportTripped, boolean launcherTripped) {
    return enabled && healthy && intakeDeployed && !(transportTripped && launcherTripped);
  }
}






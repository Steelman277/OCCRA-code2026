package frc.robot;

public final class UptakeLogic {
  private UptakeLogic() {}
  public static boolean shouldRun(boolean enabled, boolean motorHealthy, boolean intakeDeployed,
      boolean launcherHealthy, boolean launcherTripped) {
    return enabled && motorHealthy && intakeDeployed && launcherHealthy && !launcherTripped;
  }
}





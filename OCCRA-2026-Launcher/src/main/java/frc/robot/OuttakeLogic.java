package frc.robot;
/** Held reverse override; preserves position and health interlocks. */
public final class OuttakeLogic {
  private OuttakeLogic() {}
  public static double volts(boolean held, boolean permitted, double normalVolts, double reverseVolts) {
    if (!held) return normalVolts;
    return permitted && Double.isFinite(reverseVolts) && reverseVolts < 0 && reverseVolts >= -12
        ? reverseVolts : 0;
  }
}

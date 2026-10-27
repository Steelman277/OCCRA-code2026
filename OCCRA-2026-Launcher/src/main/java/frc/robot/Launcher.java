package frc.robot;

import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.controls.VelocityVoltage; 
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/** Temporary voltage-only launcher. Controller protections remain active. */
public final class Launcher {
  private final TalonFX left = new TalonFX(Constants.Launcher.LEFT_ID);
  private final TalonFX right = new TalonFX(Constants.Launcher.RIGHT_ID);
  private final VoltageOut leftRequest = new VoltageOut(0).withEnableFOC(false);
  private final VoltageOut rightRequest = new VoltageOut(0).withEnableFOC(false);
  private final LauncherLogic logic = new LauncherLogic();
  private final boolean configured;
  private boolean commandFault;
  private String faultReason = "";

  public Launcher() {
    boolean a = configure(left, Constants.Launcher.LEFT_INVERTED);
    boolean b = configure(right, Constants.Launcher.RIGHT_INVERTED);
    boolean settings = Double.isFinite(Constants.Launcher.RUN_VOLTS)
        && Constants.Launcher.RUN_VOLTS > 0 && Constants.Launcher.RUN_VOLTS <= 12
        && Double.isFinite(Constants.Launcher.LOW_SHOT_VOLTS)
        && Constants.Launcher.LOW_SHOT_VOLTS > 0
        && Constants.Launcher.LOW_SHOT_VOLTS < Constants.Launcher.RUN_VOLTS
        && Double.isFinite(Constants.Launcher.SPINUP_SECONDS) && Constants.Launcher.SPINUP_SECONDS >= 0;
    configured = a && b && settings;
    if (!settings) report("Invalid launcher voltage/delay constants");
    right.getConfigurator().apply(new Slot0Configs().withKP(Constants.Launcher.kP).withKS(Constants.Launcher.kS).withKV(Constants.Launcher.kV));
    left.getConfigurator().apply(new Slot0Configs().withKP(Constants.Launcher.kP).withKS(Constants.Launcher.kS).withKV(Constants.Launcher.kV));
  }

  private boolean configure(TalonFX motor, boolean inverted) {
    var c = new TalonFXConfiguration();
    c.MotorOutput.Inverted = inverted ? InvertedValue.Clockwise_Positive : InvertedValue.CounterClockwise_Positive;
    c.MotorOutput.NeutralMode = NeutralModeValue.Coast;
    c.Feedback.SensorToMechanismRatio = Constants.Launcher.MOTOR_TURNS_PER_WHEEL_TURN;
    c.CurrentLimits.StatorCurrentLimitEnable = true;
    c.CurrentLimits.StatorCurrentLimit = Constants.Launcher.STATOR_LIMIT_AMPS;
    c.CurrentLimits.SupplyCurrentLimitEnable = true;
    c.CurrentLimits.SupplyCurrentLimit = Constants.Launcher.SUPPLY_LIMIT_AMPS;
    var status = motor.getConfigurator().apply(c);
    if (!status.isOK()) report("Launcher CAN " + motor.getDeviceID() + " configuration: " + status);
    return status.isOK();
  }

  private void report(String reason) {
    faultReason = reason;
    DriverStation.reportError(reason, false);
  }

  public boolean periodic(boolean requested, boolean lowShot) {
    boolean healthy = configured && !commandFault;
    boolean ready = logic.update(requested, lowShot, healthy, Timer.getFPGATimestamp());
    //double volts = requested && healthy
       // ? (lowShot ? Constants.Launcher.LOW_SHOT_VOLTS : Constants.Launcher.RUN_VOLTS) : 0; //!if (requested and healthy) volts = 0, else if lowShot set volts to lowshotvolts, otherwise use runvolts
    double rpm;
       if (lowShot) {
      rpm = Constants.Launcher.LOW_SHOT_RPM;
    } else rpm = Constants.Launcher.HIGH_SHOT_RPM;
       if (requested && logic.state() != LauncherLogic.State.FAULT) {
      var a = left.setControl(new VelocityVoltage(rpm / 60.0));
      var b = right.setControl(new VelocityVoltage(rpm / 60.0));
      if (!a.isOK() || !b.isOK()) {
        commandFault = true; logic.fail(); ready = false; rpm = 0;
        left.stopMotor(); right.stopMotor();
        report("Launcher voltage command failed: LEFT=" + a + ", RIGHT=" + b);
      }
    } else { left.stopMotor(); right.stopMotor(); rpm = 0; }
    SmartDashboard.putString("Launcher/ControlMode", "RPM (hopefully) with timed feed");
    SmartDashboard.putString("Launcher/State", logic.state().name());
    SmartDashboard.putString("Launcher/Shot", requested ? (lowShot ? "Low" : "Full") : "Off");
    SmartDashboard.putString("Launcher/Fault", faultReason);
    SmartDashboard.putNumber("Launcher/CommandedRPM", rpm);
    SmartDashboard.putNumber("Launcher/SpinupSeconds", Constants.Launcher.SPINUP_SECONDS);
    SmartDashboard.putBoolean("Launcher/Ready", ready); // Delay elapsed, NOT verified wheel speed.
    SmartDashboard.putBoolean("Launcher/Healthy", healthy && !commandFault);
    // Diagnostics only: no RPM or aggregate fault-field gating in this temporary mode.
    SmartDashboard.putNumber("Launcher/LeftFaultBits", left.getFaultField().getValueAsDouble());
    SmartDashboard.putNumber("Launcher/RightFaultBits", right.getFaultField().getValueAsDouble());
    return ready;
  }

  public void stop() { left.stopMotor(); right.stopMotor(); logic.reset(); }
}

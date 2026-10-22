package frc.robot;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public final class Launcher {
  private final TalonFX left = new TalonFX(Constants.Launcher.LEFT_ID);
  private final TalonFX right = new TalonFX(Constants.Launcher.RIGHT_ID);
  private final VelocityVoltage leftRequest = new VelocityVoltage(0).withEnableFOC(false);
  private final VelocityVoltage rightRequest = new VelocityVoltage(0).withEnableFOC(false);
  private final LauncherLogic logic = new LauncherLogic();
  private final boolean configured;
  private boolean commandFault;

  public Launcher() {
    boolean a = configure(left, Constants.Launcher.LEFT_INVERTED);
    boolean b = configure(right, Constants.Launcher.RIGHT_INVERTED);
    configured = a && b;
    if (!configured) DriverStation.reportError("Launcher configuration failed; fix CAN/power and restart", false);
  }

  private boolean configure(TalonFX motor, boolean inverted) {
    var c = new TalonFXConfiguration();
    c.MotorOutput.Inverted = inverted ? InvertedValue.Clockwise_Positive : InvertedValue.CounterClockwise_Positive;
    c.MotorOutput.NeutralMode = NeutralModeValue.Coast;
    c.Feedback.SensorToMechanismRatio = Constants.Launcher.MOTOR_TURNS_PER_WHEEL_TURN;
    c.Slot0.kV = Constants.Launcher.KV;
    c.Slot0.kP = Constants.Launcher.KP;
    c.CurrentLimits.StatorCurrentLimitEnable = true;
    c.CurrentLimits.StatorCurrentLimit = Constants.Launcher.STATOR_LIMIT_AMPS;
    c.CurrentLimits.SupplyCurrentLimitEnable = true;
    c.CurrentLimits.SupplyCurrentLimit = Constants.Launcher.SUPPLY_LIMIT_AMPS;
    boolean applied = motor.getConfigurator().apply(c).isOK();
    boolean velocity = motor.getVelocity().setUpdateFrequency(50).isOK();
    boolean faults = motor.getFaultField().setUpdateFrequency(50).isOK();
    return applied && velocity && faults;
  }

  private boolean fresh(StatusSignal<?> signal) {
    return signal.getStatus().isOK() && signal.getTimestamp().getLatency() <= Constants.Launcher.MAX_SIGNAL_AGE_SECONDS;
  }

  public boolean periodic(boolean requested) {
    var lv = left.getVelocity(); var rv = right.getVelocity();
    var lf = left.getFaultField(); var rf = right.getFaultField();
    double leftRpm = lv.getValueAsDouble() * 60;
    double rightRpm = rv.getValueAsDouble() * 60;
    boolean healthy = configured && !commandFault && fresh(lv) && fresh(rv) && fresh(lf) && fresh(rf)
        && lf.getValue() == 0 && rf.getValue() == 0;
    boolean ready = logic.update(requested, healthy, leftRpm, rightRpm, Timer.getFPGATimestamp());
    if (requested && logic.state() != LauncherLogic.State.FAULT) {
      var a = left.setControl(leftRequest.withVelocity(Constants.Launcher.TARGET_RPM / 60));
      var b = right.setControl(rightRequest.withVelocity(Constants.Launcher.TARGET_RPM / 60));
      if (!a.isOK() || !b.isOK()) {
        commandFault = true; logic.fail(); ready = false;
        left.stopMotor(); right.stopMotor();
        DriverStation.reportError("Launcher command failed; fix CAN/power and restart", false);
      }
    } else { left.stopMotor(); right.stopMotor(); }
    SmartDashboard.putString("Launcher/State", logic.state().name());
    SmartDashboard.putNumber("Launcher/LeftRPM", leftRpm);
    SmartDashboard.putNumber("Launcher/RightRPM", rightRpm);
    SmartDashboard.putNumber("Launcher/TargetRPM", Constants.Launcher.TARGET_RPM);
    SmartDashboard.putBoolean("Launcher/Ready", ready);
    SmartDashboard.putBoolean("Launcher/Healthy", healthy && !commandFault);
    return ready;
  }

  public void stop() { left.stopMotor(); right.stopMotor(); logic.reset(); }
}





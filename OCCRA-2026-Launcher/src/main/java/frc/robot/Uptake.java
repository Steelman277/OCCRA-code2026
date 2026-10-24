package frc.robot;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/** One Kraken X60; consumes the launch sensor snapshot owned by Transporter. */
public final class Uptake {
  private final TalonFX motor = new TalonFX(Constants.Uptake.MOTOR_ID);
  private final VoltageOut request = new VoltageOut(0);
  private final boolean configured;
  private boolean commandFault;

  public Uptake() {
    var config = new TalonFXConfiguration();
    config.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    config.MotorOutput.Inverted = Constants.Uptake.INVERTED
        ? InvertedValue.Clockwise_Positive : InvertedValue.CounterClockwise_Positive;
    config.Feedback.SensorToMechanismRatio = Constants.Uptake.MOTOR_TURNS_PER_WHEEL_TURN;
    config.CurrentLimits.StatorCurrentLimitEnable = true;
    config.CurrentLimits.StatorCurrentLimit = Constants.Uptake.STATOR_LIMIT_AMPS;
    config.CurrentLimits.SupplyCurrentLimitEnable = true;
    config.CurrentLimits.SupplyCurrentLimit = Constants.Uptake.SUPPLY_LIMIT_AMPS;
    configured = motor.getConfigurator().apply(config).isOK();
    if (!configured) DriverStation.reportError("Uptake configuration failed; fix CAN/power and restart code", false);
  }

  public void periodic(boolean enabled, boolean intakeDeployed, boolean launcherHealthy, boolean launcherTripped) {
    periodic(enabled, intakeDeployed, launcherHealthy, launcherTripped, false, false);
  }

  public void periodic(boolean enabled, boolean intakeDeployed, boolean launcherHealthy,
      boolean launcherTripped, boolean launchRequested, boolean launcherReady) {
    periodic(enabled, intakeDeployed, launcherHealthy, launcherTripped, launchRequested, launcherReady, false);
  }
  public void periodic(boolean enabled, boolean intakeDeployed, boolean launcherHealthy, boolean launcherTripped, boolean launchRequested, boolean launcherReady, boolean outtaking) {
    boolean running = UptakeLogic.shouldRun(enabled, configured && !commandFault,
        intakeDeployed, launcherHealthy, launcherTripped);
    running = FeedLogic.shouldRun(launchRequested, launcherReady, running,
        enabled && configured && !commandFault && launcherHealthy, intakeDeployed);
    double volts = OuttakeLogic.volts(outtaking, enabled && configured && !commandFault && launcherHealthy && intakeDeployed, running ? Constants.Uptake.RUN_VOLTS : 0, Constants.Uptake.OUTTAKE_VOLTS);
    running = volts != 0;
    var status = motor.setControl(request.withOutput(volts));
    if (!status.isOK()) {
      if (!commandFault) DriverStation.reportError("Uptake command failed; fix CAN/power and restart code", false);
      commandFault = true;
      stop();
      running = false;
    }
    SmartDashboard.putBoolean("Uptake/Running", running);
    SmartDashboard.putBoolean("Uptake/MotorHealthy", configured && !commandFault);
    SmartDashboard.putBoolean("Uptake/LauncherHealthy", launcherHealthy);
    SmartDashboard.putBoolean("Uptake/LauncherTripped", launcherTripped);
  }

  public void stop() { motor.stopMotor(); }
}







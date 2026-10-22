package frc.robot;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.CANrangeConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.CANrange;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public final class Transporter {
  private final TalonFX left = new TalonFX(Constants.Transporter.LEFT_MOTOR_ID);
  private final TalonFX right = new TalonFX(Constants.Transporter.RIGHT_MOTOR_ID);
  private final CANrange transport = new CANrange(Constants.Transporter.TRANSPORT_SENSOR_ID);
  private final CANrange launcher = new CANrange(Constants.Transporter.LAUNCHER_SENSOR_ID);
  private final VoltageOut leftRequest = new VoltageOut(0);
  private final VoltageOut rightRequest = new VoltageOut(0);
  private final boolean configured;
  private boolean commandFault;
  private final boolean launcherConfigured;
  private boolean launcherHealthy;
  private boolean launcherTripped = true;

  // Updated once per loop by periodic; Robot calls uptake immediately afterward.
  public boolean launcherHealthy() { return launcherHealthy; }
  public boolean launcherTripped() { return launcherTripped; }

  public Transporter() {
    boolean a = configureMotor(left, Constants.Transporter.LEFT_INVERTED);
    boolean b = configureMotor(right, Constants.Transporter.RIGHT_INVERTED);
    boolean c = configureSensor(transport, Constants.Transporter.TRANSPORT_THRESHOLD_METERS);
    boolean d = configureSensor(launcher, Constants.Transporter.LAUNCHER_THRESHOLD_METERS);
    launcherConfigured = d;
    configured = a && b && c && d;
    if (!configured) DriverStation.reportError("Transporter configuration failed; fix CAN/power and restart code", false);
  }

  private boolean configureMotor(TalonFX motor, boolean inverted) {
    var config = new TalonFXConfiguration();
    config.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    config.MotorOutput.Inverted = inverted ? InvertedValue.Clockwise_Positive : InvertedValue.CounterClockwise_Positive;
    config.Feedback.SensorToMechanismRatio = Constants.Transporter.MOTOR_TURNS_PER_WHEEL_TURN;
    config.CurrentLimits.StatorCurrentLimitEnable = true;
    config.CurrentLimits.StatorCurrentLimit = Constants.Transporter.STATOR_LIMIT_AMPS;
    config.CurrentLimits.SupplyCurrentLimitEnable = true;
    config.CurrentLimits.SupplyCurrentLimit = Constants.Transporter.SUPPLY_LIMIT_AMPS;
    return motor.getConfigurator().apply(config).isOK();
  }

  private boolean configureSensor(CANrange sensor, double threshold) {
    var config = new CANrangeConfiguration();
    config.ProximityParams.ProximityThreshold = threshold;
    config.ProximityParams.ProximityHysteresis = Constants.Transporter.HYSTERESIS_METERS;
    config.ProximityParams.MinSignalStrengthForValidMeasurement = Constants.Transporter.MIN_SIGNAL_STRENGTH;
    boolean applied = sensor.getConfigurator().apply(config).isOK();
    boolean detected = sensor.getIsDetected().setUpdateFrequency(50).isOK();
    boolean faults = sensor.getFaultField().setUpdateFrequency(50).isOK();
    return applied && detected && faults;
  }

  private boolean fresh(StatusSignal<?> signal) {
    return signal.getStatus().isOK()
        && signal.getTimestamp().getLatency() <= Constants.Transporter.MAX_SIGNAL_AGE_SECONDS;
  }

  public void periodic(boolean enabled, boolean intakeDeployed) {
    periodic(enabled, intakeDeployed, false, false);
  }

  public void periodic(boolean enabled, boolean intakeDeployed, boolean launchRequested, boolean launcherReady) {
    var t = transport.getIsDetected();
    var l = launcher.getIsDetected();
    var tf = transport.getFaultField();
    var lf = launcher.getFaultField();
    launcherHealthy = launcherConfigured && fresh(l) && fresh(lf) && lf.getValue() == 0;
    launcherTripped = l.getValue();
    boolean healthy = configured && !commandFault && fresh(t) && fresh(l)
        && fresh(tf) && fresh(lf) && tf.getValue() == 0 && lf.getValue() == 0;
    boolean running = TransporterLogic.shouldRun(enabled, healthy, intakeDeployed, t.getValue(), l.getValue());
    running = FeedLogic.shouldRun(launchRequested, launcherReady, running, healthy, intakeDeployed);
    double volts = running ? Constants.Transporter.RUN_VOLTS : 0;
    var leftStatus = left.setControl(leftRequest.withOutput(volts));
    var rightStatus = right.setControl(rightRequest.withOutput(volts));
    if (!leftStatus.isOK() || !rightStatus.isOK()) {
      if (!commandFault) DriverStation.reportError("Transporter motor command failed; restart after fixing CAN/power", false);
      commandFault = true;
      stop();
      running = false;
    }
    SmartDashboard.putBoolean("Transporter/Enabled", enabled);
    SmartDashboard.putBoolean("Transporter/IntakeDeployed", intakeDeployed);
    SmartDashboard.putBoolean("Transporter/TransportTripped", t.getValue());
    SmartDashboard.putBoolean("Transporter/LauncherTripped", l.getValue());
    SmartDashboard.putBoolean("Transporter/Healthy", healthy && !commandFault);
    SmartDashboard.putBoolean("Transporter/Running", running);
    SmartDashboard.putNumber("Transporter/TransportMeters", transport.getDistance().getValueAsDouble());
    SmartDashboard.putNumber("Transporter/LauncherMeters", launcher.getDistance().getValueAsDouble());
  }

  public void stop() { left.stopMotor(); right.stopMotor(); }
}






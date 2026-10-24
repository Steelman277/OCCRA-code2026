package frc.robot;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/** Kraken X44 rack actuator and Kraken X60 rollers on the roboRIO CAN bus. */
public final class Intake {
  private final TalonFX deploy = new TalonFX(Constants.Intake.DEPLOY_CAN_ID);
  private final TalonFX roller = new TalonFX(Constants.Intake.ROLLER_CAN_ID);
  private final IntakeMotion motion = new IntakeMotion();
  private final VoltageOut deployRequest = new VoltageOut(0);
  private final VoltageOut rollerRequest = new VoltageOut(0);
  private final boolean configured;

  public Intake() {
    boolean deployOk = configure(deploy, Constants.Intake.DEPLOY_INVERTED,
        Constants.Intake.DEPLOY_MOTOR_TURNS_PER_PINION_TURN,
        Constants.Intake.DEPLOY_STATOR_LIMIT_AMPS, Constants.Intake.DEPLOY_SUPPLY_LIMIT_AMPS);
    boolean rollerOk = configure(roller, Constants.Intake.ROLLER_INVERTED,
        Constants.Intake.ROLLER_MOTOR_TURNS_PER_ROLLER_TURN,
        Constants.Intake.ROLLER_STATOR_LIMIT_AMPS, Constants.Intake.ROLLER_SUPPLY_LIMIT_AMPS);
    boolean signalOk = deploy.getStatorCurrent().setUpdateFrequency(50).isOK();
    configured = deployOk && rollerOk && signalOk;
    if (!configured) motion.fail("Intake configuration failed; fix CAN/power and restart code");
  }

  private boolean configure(TalonFX motor, boolean inverted, double ratio, double stator, double supply) {
    var config = new TalonFXConfiguration();
    config.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    config.MotorOutput.Inverted = inverted ? InvertedValue.Clockwise_Positive : InvertedValue.CounterClockwise_Positive;
    config.Feedback.SensorToMechanismRatio = ratio;
    config.CurrentLimits.StatorCurrentLimit = stator;
    config.CurrentLimits.StatorCurrentLimitEnable = true;
    config.CurrentLimits.SupplyCurrentLimit = supply;
    config.CurrentLimits.SupplyCurrentLimitEnable = true;
    var status = motor.getConfigurator().apply(config);
    if (!status.isOK()) DriverStation.reportError("Intake CAN " + motor.getDeviceID() + ": " + status, false);
    return status.isOK();
  }


  public void periodic(boolean togglePressed, boolean retractPressed, boolean rollersPressed,
      boolean clearPressed, boolean outtakePressed) {
    double now = Timer.getFPGATimestamp();
    var current = deploy.getStatorCurrent();
    boolean fresh = current.getStatus().isOK()
        && current.getTimestamp().getLatency() <= Constants.Intake.MAX_SIGNAL_AGE_SECONDS;
    if (!configured) { stop(); publish(current.getValueAsDouble()); return; }
    if (clearPressed) { stop(); motion.clearFault(); publish(current.getValueAsDouble()); return; }
    // Retraction wins if both buttons are pressed.
    if (retractPressed) motion.request(false, now);
    else if (togglePressed) motion.toggleDeployment(now);

    if (outtakePressed) {motion.toggleRollers();roller.setControl(rollerRequest.withOutput(-6.0));}; //TODO make this work
    if (rollersPressed) motion.toggleRollers();


    double volts = motion.update(now, current.getValueAsDouble(), fresh);
    double rollerVolts = motion.rollerVolts();
    if (!deploy.setControl(deployRequest.withOutput(volts)).isOK()
        || !roller.setControl(rollerRequest.withOutput(rollerVolts)).isOK()) {
      motion.fail("Intake motor command failed");
      stop();
    }
    publish(current.getValueAsDouble());
  }

  public boolean isDeployed() { return configured && motion.state() == IntakeMotion.State.DEPLOYED; }

  public void stop() { motion.stop(); deploy.stopMotor(); roller.stopMotor(); }

  private void publish(double current) {
    SmartDashboard.putString("Intake/State", motion.state().name());
    SmartDashboard.putString("Intake/Fault", motion.faultReason());
    SmartDashboard.putNumber("Intake/DeployStatorAmps", current);
    SmartDashboard.putBoolean("Intake/RollersRunning", motion.rollerVolts() != 0);
  }
}





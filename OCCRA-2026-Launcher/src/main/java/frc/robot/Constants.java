package frc.robot;

public final class Constants {
  private Constants() {}
  // Proposed CAN assignments: set the physical devices to match before enabling.
  // HARDWARE: match the unique CAN ID assigned in Phoenix Tuner; changing code does not rename the device.
  public static final int FRONT_LEFT_ID = 1;
  // HARDWARE: match the unique CAN ID assigned in Phoenix Tuner; changing code does not rename the device.
  public static final int REAR_LEFT_ID = 2;
  // HARDWARE: match the unique CAN ID assigned in Phoenix Tuner; changing code does not rename the device.
  public static final int FRONT_RIGHT_ID = 3;
  // HARDWARE: match the unique CAN ID assigned in Phoenix Tuner; changing code does not rename the device.
  public static final int REAR_RIGHT_ID = 4;
  // DIRECTION: verify wheel motion on the robot at low output. Change only if this motor moves the wrong way.
  public static final boolean FRONT_LEFT_INVERTED = false;
  // DIRECTION: verify wheel motion on the robot at low output. Change only if this motor moves the wrong way.
  public static final boolean REAR_LEFT_INVERTED = false;
  // DIRECTION: verify wheel motion on the robot at low output. Change only if this motor moves the wrong way.
  public static final boolean FRONT_RIGHT_INVERTED = true;
  // DIRECTION: verify wheel motion on the robot at low output. Change only if this motor moves the wrong way.
  public static final boolean REAR_RIGHT_INVERTED = true;
  // CONTROLLER: Driver Station USB slot (zero-based). Must match the Xbox controller slot.
  public static final int DRIVER_PORT = 0;
  // DRIVER FEEL: fraction of stick travel ignored around center. Increase for drift; decrease for finer small commands.
  public static final double DEADBAND = 0.08;
  // DRIVE SPEED: maximum normalized wheel output (0 to 1). Increase only after direction and stopping checks.
  public static final double MAX_OUTPUT = 1.0;
  // DRIVE SPEED: output cap while left bumper is held (0 to 1); normally below MAX_OUTPUT.
  public static final double SLOW_OUTPUT = 0.5;

  // Intake settings.
  public static final class Intake {
    private Intake() {}
    // HOLD Y reverse voltage: tune between -12 and 0 V for ejection.
    public static final double OUTTAKE_VOLTS = -8.0;
    // HARDWARE: match the unique CAN ID assigned in Phoenix Tuner; changing code does not rename the device.
    public static final int DEPLOY_CAN_ID = 5;
    // HARDWARE: match the unique CAN ID assigned in Phoenix Tuner; changing code does not rename the device.
    public static final int ROLLER_CAN_ID = 6;
    // DIRECTION: verify wheel motion on the robot at low output. Change only if this motor moves the wrong way.
    public static final boolean DEPLOY_INVERTED = true;
    // DIRECTION: verify wheel motion on the robot at low output. Change only if this motor moves the wrong way.
    public static final boolean ROLLER_INVERTED = false;
    // HARDWARE RATIO: motor turns per output turn; match the actual gearing, not a speed tuning knob.
    public static final double DEPLOY_MOTOR_TURNS_PER_PINION_TURN = 5.0;
    // Assumes 12-tooth motor sprocket driving a 26-tooth roller sprocket.
    // HARDWARE RATIO: motor turns per output turn; match the actual gearing, not a speed tuning knob.
    public static final double ROLLER_MOTOR_TURNS_PER_ROLLER_TURN = 26.0 / 12.0;
    
    // Initial commissioning values, NOT measured mechanical limits.
    // RACK SPEED: positive extension voltage. More voltage increases speed/impact; retune endpoint current after changes.
    public static final double DEPLOY_VOLTS = 2.5;
    // RACK SPEED: negative retraction voltage. Tune magnitude independently; retain the retraction sign.
    public static final double RETRACT_VOLTS = -2.5;
    // ROLLER SPEED: forward voltage, not RPM. Raise gradually if pickup is weak; lower if balls bounce or jam.
    public static final double ROLLER_VOLTS = 7;
    // MOTOR CURRENT: stator amperes limit torque/heating. Choose for the mechanism; do not raise just to conceal a jam.
    public static final double DEPLOY_STATOR_LIMIT_AMPS = 25.0;
    // BATTERY CURRENT: supply amperes limit battery load. Too low may slow acceleration; coordinate with robot power limits.
    public static final double DEPLOY_SUPPLY_LIMIT_AMPS = 20.0;
    // MOTOR CURRENT: stator amperes limit torque/heating. Choose for the mechanism; do not raise just to conceal a jam.
    public static final double ROLLER_STATOR_LIMIT_AMPS = 40.0;
    // BATTERY CURRENT: supply amperes limit battery load. Too low may slow acceleration; coordinate with robot power limits.
    public static final double ROLLER_SUPPLY_LIMIT_AMPS = 30.0;
    // ENDPOINT: extension stator-current threshold in amperes. Above normal running current, below DEPLOY_STATOR_LIMIT_AMPS.
    public static final double DEPLOY_STOP_AMPS = 24.0;
    // ENDPOINT: retraction stator-current threshold in amperes. Measure separately from extension; below the stator limit.
    public static final double RETRACT_STOP_AMPS = 24.0;
    // ENDPOINT TIMING: ignore initial acceleration current this long. Too short false-trips; too long delays stopping at a nearby stop.
    public static final double STARTUP_IGNORE_SECONDS = 0.25;
    // ENDPOINT TIMING: current must remain above threshold this long. Longer rejects brief spikes but pushes against stops longer.
    public static final double SPIKE_HOLD_SECONDS = 0.15;
    // TRAVEL FAULT: maximum motion time. Set just beyond measured slowest normal travel; never use to mask binding.
    public static final double TRAVEL_TIMEOUT_SECONDS = 3.0;
    // CAN HEALTH: maximum accepted telemetry age. Diagnose wiring/update rates before changing; larger allows older feedback.
    public static final double MAX_SIGNAL_AGE_SECONDS = 0.10;
  }

  // Transporter settings.
  public static final class Transporter {
    private Transporter() {}
    // HOLD Y reverse voltage: tune between -12 and 0 V for ejection.
    public static final double OUTTAKE_VOLTS = -8.0;
    // Proposed assignments, leaving IDs 1-6 for drivetrain and intake.
    // HARDWARE: match the unique CAN ID assigned in Phoenix Tuner; changing code does not rename the device.
    public static final int LEFT_MOTOR_ID = 7;
    // HARDWARE: match the unique CAN ID assigned in Phoenix Tuner; changing code does not rename the device.
    public static final int RIGHT_MOTOR_ID = 8;
    // HARDWARE: match the unique CAN ID assigned in Phoenix Tuner; changing code does not rename the device.
    public static final int TRANSPORT_SENSOR_ID = 9;
    // HARDWARE: match the unique CAN ID assigned in Phoenix Tuner; changing code does not rename the device.
    public static final int LAUNCHER_SENSOR_ID = 10;
    
    // HARDWARE RATIO: motor turns per output turn; match the actual gearing, not a speed tuning knob.
    public static final double MOTOR_TURNS_PER_WHEEL_TURN = 2.0;
    // DIRECTION: verify wheel motion on the robot at low output. Change only if this motor moves the wrong way.
    public static final boolean LEFT_INVERTED = true;
    // DIRECTION: verify wheel motion on the robot at low output. Change only if this motor moves the wrong way.
    public static final boolean RIGHT_INVERTED = false;
    // Commissioning settings: verify direction, speed, and detection on the robot.
    // FEED SPEED: forward voltage, not RPM. Raise gradually for throughput; lower for jams or excessive launcher speed sag.
    public static final double RUN_VOLTS = 2.5;
    public static final double LAUNCH_VOLTS = 7.50;
    // MOTOR CURRENT: stator amperes limit torque/heating. Choose for the mechanism; do not raise just to conceal a jam.
    public static final double STATOR_LIMIT_AMPS = 25.0;
    // BATTERY CURRENT: supply amperes limit battery load. Too low may slow acceleration; coordinate with robot power limits.
    public static final double SUPPLY_LIMIT_AMPS = 20.0;
    // BALL DETECTION: center sensor distance threshold in meters. Tune using actual empty-path and ball measurements.
    public static final double TRANSPORT_THRESHOLD_METERS = 1.5;
    // BALL DETECTION: launcher sensor distance threshold in meters. Shared with uptake/launch feed where present.
    public static final double LAUNCHER_THRESHOLD_METERS = 0.1;
    // BALL DETECTION: trip below threshold minus this value; clear above threshold plus it. Increase only to reduce edge chatter.
    public static final double HYSTERESIS_METERS = 0.01;
    // OPTICAL RETURN: minimum sensor signal strength, not a distance. Tune in Tuner using real balls and empty-path readings.
    public static final double MIN_SIGNAL_STRENGTH = 2500;
    // CAN HEALTH: maximum accepted telemetry age. Diagnose wiring/update rates before changing; larger allows older feedback.
    public static final double MAX_SIGNAL_AGE_SECONDS = 0.10;
  
  public static final double TRANSPORT_MIN_SIGNAL_STRENGTH = 5000;
  }

  // Uptake settings.
  public static final class Uptake {
    private Uptake() {}
    // HOLD Y reverse voltage: tune between -12 and 0 V for ejection.
    public static final double OUTTAKE_VOLTS = -6.0;
    // HARDWARE: match the unique CAN ID assigned in Phoenix Tuner; changing code does not rename the device.
    public static final int MOTOR_ID = 11;
    // HARDWARE RATIO: motor turns per output turn; match the actual gearing, not a speed tuning knob.
    public static final double MOTOR_TURNS_PER_WHEEL_TURN = 2.0;
    // DIRECTION: verify wheel motion on the robot at low output. Change only if this motor moves the wrong way.
    public static final boolean INVERTED = true;
    // Initial commissioning values; tune direction and speed on the mechanism.
    // FEED SPEED: forward voltage, not RPM. Raise gradually for throughput; lower for jams or excessive launcher speed sag.
    public static final double RUN_VOLTS = 3.0;
    // MOTOR CURRENT: stator amperes limit torque/heating. Choose for the mechanism; do not raise just to conceal a jam.
    public static final double STATOR_LIMIT_AMPS = 30.0;
    // BATTERY CURRENT: supply amperes limit battery load. Too low may slow acceleration; coordinate with robot power limits.
    public static final double SUPPLY_LIMIT_AMPS = 20.0;
  }

  // Launcher settings.
  public static final class Launcher {
    private Launcher() {}
    // Full shot voltage on right trigger; tune positive and at most 12 V.
<<<<<<< Updated upstream
    public static final double arcMultiplier = 1.0;
=======
    public static final double arcMultiplier = 1.2;

    public static final double LOW_SHOT_RPM = 3000.0;
    public static final double HIGH_SHOT_RPM = 4000.0; //TODO tune these values
    public static final double kP = 0.1;
    public static final double kS = 0.36;
    public static final double kV = 0.116;

>>>>>>> Stashed changes
    public static final double RUN_VOLTS = 6.0 * arcMultiplier;
    // Low shot voltage on left bumper; positive and below RUN_VOLTS.
    public static final double LOW_SHOT_VOLTS = 4.4 * arcMultiplier;
    // Time before feeding; does not verify wheel speed. Restarts on shot power change.
    public static final double SPINUP_SECONDS = 0.750;
    // HARDWARE: match the unique CAN ID assigned in Phoenix Tuner; changing code does not rename the device.
    public static final int LEFT_ID = 12;
    // HARDWARE: match the unique CAN ID assigned in Phoenix Tuner; changing code does not rename the device.
    public static final int RIGHT_ID = 13;
    // DIRECTION: verify wheel motion on the robot at low output. Change only if this motor moves the wrong way.
    public static final boolean LEFT_INVERTED = true;
    // DIRECTION: verify wheel motion on the robot at low output. Change only if this motor moves the wrong way.
    public static final boolean RIGHT_INVERTED = false;
    // HARDWARE RATIO: motor turns per output turn; match the actual gearing, not a speed tuning knob.
    public static final double MOTOR_TURNS_PER_WHEEL_TURN = 1.0;
    // DRIVER FEEL: right trigger fraction (0 to 1) above which launch is requested; not a speed command.
    public static final double TRIGGER_THRESHOLD = 0.15;

    // MOTOR CURRENT: stator amperes limit torque/heating. Choose for the mechanism; do not raise just to conceal a jam.
    public static final double STATOR_LIMIT_AMPS = 60;
    // BATTERY CURRENT: supply amperes limit battery load. Too low may slow acceleration; coordinate with robot power limits.
    public static final double SUPPLY_LIMIT_AMPS = 40;
  }
}








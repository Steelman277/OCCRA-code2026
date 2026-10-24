# OCCRA tuning guide

All adjustable robot settings are in **src/main/java/frc/robot/Constants.java**. This guide applies to this project and only lists mechanisms included in it. The values below are current starting settings, not calibrated results. Comments are immediately above the matching fields in Constants.java.


## How to make a tuning change

1. Confirm the project you intend to deploy. Each OCCRA project is an independent copy: changing one does not update the others. Carry proven settings into later projects deliberately.
2. Record the original value and the symptom. Start with correct CAN IDs, motor directions, gearing and sensor placement before adjusting speed or control gains.
3. Disable before editing. Change one related setting at a time, then build and deploy while disabled. Constants are compiled into the program; changing the file alone does not update the robot.
4. Test briefly with mechanisms clear and access to disable. Begin with no balls for direction checks; use actual balls for optical detection and feed tuning.
5. Compare measured results with the baseline. Record the final value, battery condition and result. Do not replace a reliable setting merely because a larger number is possible.
6. Run `./gradlew.bat build`, then `./Sync-ToGitHub.ps1` after editing. Some timing/RPM unit tests use the original commissioning values; intentional changes may require updating those test inputs and expectations, not removing the checks.

CAN IDs, inversions and gear ratios describe hardware. Output caps, voltages and launcher target speed affect performance. Current limits, timeouts and telemetry freshness limits protect or qualify operation; investigate the underlying symptom before relaxing them.


## Settings reference

| Setting in Constants.java | Current value or expression | Meaning and when to change |
| --- | --- | --- |
| `Constants.FRONT_LEFT_ID` | `1` | HARDWARE: match the unique CAN ID assigned in Phoenix Tuner; changing code does not rename the device. |
| `Constants.REAR_LEFT_ID` | `2` | HARDWARE: match the unique CAN ID assigned in Phoenix Tuner; changing code does not rename the device. |
| `Constants.FRONT_RIGHT_ID` | `3` | HARDWARE: match the unique CAN ID assigned in Phoenix Tuner; changing code does not rename the device. |
| `Constants.REAR_RIGHT_ID` | `4` | HARDWARE: match the unique CAN ID assigned in Phoenix Tuner; changing code does not rename the device. |
| `Constants.FRONT_LEFT_INVERTED` | `false` | DIRECTION: verify wheel motion on the robot at low output. Change only if this motor moves the wrong way. |
| `Constants.REAR_LEFT_INVERTED` | `false` | DIRECTION: verify wheel motion on the robot at low output. Change only if this motor moves the wrong way. |
| `Constants.FRONT_RIGHT_INVERTED` | `true` | DIRECTION: verify wheel motion on the robot at low output. Change only if this motor moves the wrong way. |
| `Constants.REAR_RIGHT_INVERTED` | `true` | DIRECTION: verify wheel motion on the robot at low output. Change only if this motor moves the wrong way. |
| `Constants.DRIVER_PORT` | `0` | CONTROLLER: Driver Station USB slot (zero-based). Must match the Xbox controller slot. |
| `Constants.DEADBAND` | `0.08` | DRIVER FEEL: fraction of stick travel ignored around center. Increase for drift; decrease for finer small commands. |
| `Constants.MAX_OUTPUT` | `0.50` | DRIVE SPEED: maximum normalized wheel output (0 to 1). Increase only after direction and stopping checks. |
| `Constants.SLOW_OUTPUT` | `0.25` | DRIVE SPEED: output cap while left bumper is held (0 to 1); normally below MAX_OUTPUT. |
| `Constants.Intake.DEPLOY_CAN_ID` | `5` | HARDWARE: match the unique CAN ID assigned in Phoenix Tuner; changing code does not rename the device. |
| `Constants.Intake.ROLLER_CAN_ID` | `6` | HARDWARE: match the unique CAN ID assigned in Phoenix Tuner; changing code does not rename the device. |
| `Constants.Intake.DEPLOY_INVERTED` | `false` | DIRECTION: verify wheel motion on the robot at low output. Change only if this motor moves the wrong way. |
| `Constants.Intake.ROLLER_INVERTED` | `false` | DIRECTION: verify wheel motion on the robot at low output. Change only if this motor moves the wrong way. |
| `Constants.Intake.DEPLOY_MOTOR_TURNS_PER_PINION_TURN` | `5.0` | HARDWARE RATIO: motor turns per output turn; match the actual gearing, not a speed tuning knob. |
| `Constants.Intake.ROLLER_MOTOR_TURNS_PER_ROLLER_TURN` | `26.0 / 12.0` | HARDWARE RATIO: motor turns per output turn; match the actual gearing, not a speed tuning knob. |
| `Constants.Intake.DEPLOY_VOLTS` | `1.5` | RACK SPEED: positive extension voltage. More voltage increases speed/impact; retune endpoint current after changes. |
| `Constants.Intake.RETRACT_VOLTS` | `-1.5` | RACK SPEED: negative retraction voltage. Tune magnitude independently; retain the retraction sign. |
| `Constants.Intake.ROLLER_VOLTS` | `3.0` | ROLLER SPEED: forward voltage, not RPM. Raise gradually if pickup is weak; lower if balls bounce or jam. |
| `Constants.Intake.DEPLOY_STATOR_LIMIT_AMPS` | `25.0` | MOTOR CURRENT: stator amperes limit torque/heating. Choose for the mechanism; do not raise just to conceal a jam. |
| `Constants.Intake.DEPLOY_SUPPLY_LIMIT_AMPS` | `20.0` | BATTERY CURRENT: supply amperes limit battery load. Too low may slow acceleration; coordinate with robot power limits. |
| `Constants.Intake.ROLLER_STATOR_LIMIT_AMPS` | `40.0` | MOTOR CURRENT: stator amperes limit torque/heating. Choose for the mechanism; do not raise just to conceal a jam. |
| `Constants.Intake.ROLLER_SUPPLY_LIMIT_AMPS` | `30.0` | BATTERY CURRENT: supply amperes limit battery load. Too low may slow acceleration; coordinate with robot power limits. |
| `Constants.Intake.DEPLOY_STOP_AMPS` | `18.0` | ENDPOINT: extension stator-current threshold in amperes. Above normal running current, below DEPLOY_STATOR_LIMIT_AMPS. |
| `Constants.Intake.RETRACT_STOP_AMPS` | `18.0` | ENDPOINT: retraction stator-current threshold in amperes. Measure separately from extension; below the stator limit. |
| `Constants.Intake.STARTUP_IGNORE_SECONDS` | `0.25` | ENDPOINT TIMING: ignore initial acceleration current this long. Too short false-trips; too long delays stopping at a nearby stop. |
| `Constants.Intake.SPIKE_HOLD_SECONDS` | `0.15` | ENDPOINT TIMING: current must remain above threshold this long. Longer rejects brief spikes but pushes against stops longer. |
| `Constants.Intake.TRAVEL_TIMEOUT_SECONDS` | `3.0` | TRAVEL FAULT: maximum motion time. Set just beyond measured slowest normal travel; never use to mask binding. |
| `Constants.Intake.MAX_SIGNAL_AGE_SECONDS` | `0.10` | CAN HEALTH: maximum accepted telemetry age. Diagnose wiring/update rates before changing; larger allows older feedback. |
| `Constants.Transporter.LEFT_MOTOR_ID` | `7` | HARDWARE: match the unique CAN ID assigned in Phoenix Tuner; changing code does not rename the device. |
| `Constants.Transporter.RIGHT_MOTOR_ID` | `8` | HARDWARE: match the unique CAN ID assigned in Phoenix Tuner; changing code does not rename the device. |
| `Constants.Transporter.TRANSPORT_SENSOR_ID` | `9` | HARDWARE: match the unique CAN ID assigned in Phoenix Tuner; changing code does not rename the device. |
| `Constants.Transporter.LAUNCHER_SENSOR_ID` | `10` | HARDWARE: match the unique CAN ID assigned in Phoenix Tuner; changing code does not rename the device. |
| `Constants.Transporter.MOTOR_TURNS_PER_WHEEL_TURN` | `2.0` | HARDWARE RATIO: motor turns per output turn; match the actual gearing, not a speed tuning knob. |
| `Constants.Transporter.LEFT_INVERTED` | `false` | DIRECTION: verify wheel motion on the robot at low output. Change only if this motor moves the wrong way. |
| `Constants.Transporter.RIGHT_INVERTED` | `true` | DIRECTION: verify wheel motion on the robot at low output. Change only if this motor moves the wrong way. |
| `Constants.Transporter.RUN_VOLTS` | `2.0` | FEED SPEED: forward voltage, not RPM. Raise gradually for throughput; lower for jams or excessive launcher speed sag. |
| `Constants.Transporter.STATOR_LIMIT_AMPS` | `25.0` | MOTOR CURRENT: stator amperes limit torque/heating. Choose for the mechanism; do not raise just to conceal a jam. |
| `Constants.Transporter.SUPPLY_LIMIT_AMPS` | `20.0` | BATTERY CURRENT: supply amperes limit battery load. Too low may slow acceleration; coordinate with robot power limits. |
| `Constants.Transporter.TRANSPORT_THRESHOLD_METERS` | `0.15` | BALL DETECTION: center sensor distance threshold in meters. Tune using actual empty-path and ball measurements. |
| `Constants.Transporter.LAUNCHER_THRESHOLD_METERS` | `0.15` | BALL DETECTION: launcher sensor distance threshold in meters. Shared with uptake/launch feed where present. |
| `Constants.Transporter.HYSTERESIS_METERS` | `0.01` | BALL DETECTION: trip below threshold minus this value; clear above threshold plus it. Increase only to reduce edge chatter. |
| `Constants.Transporter.MIN_SIGNAL_STRENGTH` | `2500` | OPTICAL RETURN: minimum sensor signal strength, not a distance. Tune in Tuner using real balls and empty-path readings. |
| `Constants.Transporter.MAX_SIGNAL_AGE_SECONDS` | `0.10` | CAN HEALTH: maximum accepted telemetry age. Diagnose wiring/update rates before changing; larger allows older feedback. |
| `Constants.Uptake.MOTOR_ID` | `11` | HARDWARE: match the unique CAN ID assigned in Phoenix Tuner; changing code does not rename the device. |
| `Constants.Uptake.MOTOR_TURNS_PER_WHEEL_TURN` | `2.0` | HARDWARE RATIO: motor turns per output turn; match the actual gearing, not a speed tuning knob. |
| `Constants.Uptake.INVERTED` | `false` | DIRECTION: verify wheel motion on the robot at low output. Change only if this motor moves the wrong way. |
| `Constants.Uptake.RUN_VOLTS` | `2.0` | FEED SPEED: forward voltage, not RPM. Raise gradually for throughput; lower for jams or excessive launcher speed sag. |
| `Constants.Uptake.STATOR_LIMIT_AMPS` | `30.0` | MOTOR CURRENT: stator amperes limit torque/heating. Choose for the mechanism; do not raise just to conceal a jam. |
| `Constants.Uptake.SUPPLY_LIMIT_AMPS` | `20.0` | BATTERY CURRENT: supply amperes limit battery load. Too low may slow acceleration; coordinate with robot power limits. |
| `Constants.Launcher.LEFT_ID` | `12` | HARDWARE: match the unique CAN ID assigned in Phoenix Tuner; changing code does not rename the device. |
| `Constants.Launcher.RIGHT_ID` | `13` | HARDWARE: match the unique CAN ID assigned in Phoenix Tuner; changing code does not rename the device. |
| `Constants.Launcher.LEFT_INVERTED` | `false` | DIRECTION: verify wheel motion on the robot at low output. Change only if this motor moves the wrong way. |
| `Constants.Launcher.RIGHT_INVERTED` | `true` | DIRECTION: verify wheel motion on the robot at low output. Change only if this motor moves the wrong way. |
| `Constants.Launcher.MOTOR_TURNS_PER_WHEEL_TURN` | `1.0` | HARDWARE RATIO: motor turns per output turn; match the actual gearing, not a speed tuning knob. |
| `Constants.Launcher.TRIGGER_THRESHOLD` | `0.15` | DRIVER FEEL: right trigger fraction (0 to 1) above which launch is requested; not a speed command. |
| `Constants.Launcher.STATOR_LIMIT_AMPS` | `60` | MOTOR CURRENT: stator amperes limit torque/heating. Choose for the mechanism; do not raise just to conceal a jam. |
| `Constants.Launcher.SUPPLY_LIMIT_AMPS` | `40` | BATTERY CURRENT: supply amperes limit battery load. Too low may slow acceleration; coordinate with robot power limits. |


## Drivetrain order

Support the chassis with wheels clear. Verify forward, right strafe and right turn in slow mode. Fix individual inversion settings for wrong wheel direction; do not change mecanum input signs to compensate for wiring. Verify the mecanum roller layout. Adjust DEADBAND only for stick drift or feel. Tune SLOW_OUTPUT and then MAX_OUTPUT for controllability; these are output caps, not measured ground-speed targets.

## Intake current stops

Watch `Intake/DeployStatorAmps`, `Intake/State` and `Intake/Fault`. Begin with the rack physically retracted; code assumes this at startup. Do not restart code with the rack out. X toggles intake rollers, not rack motion; disable to stop the whole robot.

1. Verify deployment direction and free mechanical travel at low voltage. Measure extension and retraction separately: startup peak, normal moving current, endpoint contact current and travel time.
2. Set DEPLOY_VOLTS and RETRACT_VOLTS to move reliably without slamming the stops. Recheck current thresholds whenever these voltages or the mechanism change.
3. Set each STOP_AMPS above the highest normal moving current and below DEPLOY_STATOR_LIMIT_AMPS, while still reachable at the endpoint. Example only: if moving current peaks at 10 A and contact reaches 21 A, an 18 A threshold may separate them. Measure before adopting it. If the current limiter prevents reaching the threshold, the rack will time out.
4. STARTUP_IGNORE_SECONDS should cover the brief acceleration transient. SPIKE_HOLD_SECONDS must reject brief load spikes while limiting how long the motor pushes at contact. Longer values delay endpoint recognition.
5. Set TRAVEL_TIMEOUT_SECONDS just beyond the slowest normal travel under expected conditions. A timeout means no qualified endpoint was found; inspect binding, threshold, voltage and CAN feedback before increasing it.
6. Tune ROLLER_VOLTS with balls. Increase gradually if pickup is weak; decrease if balls bounce or jam. A new deployment restarts the rollers even if X previously paused them.

**Current-only contact cannot distinguish an endpoint from a jam.** If current ranges overlap or the rack can be damaged before detection, changing the threshold alone cannot establish reliable position; additional position/limit sensing or mechanical changes are needed.

Early stopping: inspect binding first, then consider threshold too low, startup exclusion too short, or debounce too short. Failure to stop: check threshold too high, current limit too low, insufficient motor voltage, or stale telemetry. Do not raise the current limit simply to force a spike.

## CANrange detection and transporter

Use Phoenix Tuner X to compare distance and signal strength with an empty path and with real balls at several positions. Watch `Transporter/TransportMeters`, `LauncherMeters`, `TransportTripped`, `LauncherTripped` and `Healthy` on the dashboard. Tune the two sensor thresholds independently.

At a 0.15 m threshold and 0.01 m hysteresis, detection turns on below 0.14 m and clears above 0.16 m. Choose a threshold that separates ball readings from background readings. Increase hysteresis only enough to avoid flicker at the edge. Check mounting and field of view if the sensor sees robot structure. MIN_SIGNAL_STRENGTH is optical return quality, not a distance setting; lowering it may accept weak/noisy returns, while raising it may reject a real ball. Empty open air can legitimately have low return.

Tune Transporter.RUN_VOLTS for reliable ball flow without bouncing or squeezing balls. Current limits affect available torque and battery loading. The right motor is mirrored: verify both sides move balls in the same transport direction.

Transporter operates automatically only with intake fully deployed. During normal collection it stops when both sensors are tripped. Sensor health problems block operation. Do not increase MAX_SIGNAL_AGE_SECONDS to hide CAN wiring failures.

## Uptake feed speed

Tune Uptake.RUN_VOLTS for smooth movement toward the launcher. It is a voltage command, not RPM. The 2:1 ratio is motor turns per wheel turn. Verify inversion before raising speed. During normal collection uptake runs only with intake deployed and launcher CANrange clear. Tune the shared launcher threshold in Constants.Transporter; there is no second launcher sensor setting in Uptake.

With a launcher installed, excessive uptake/transporter speed can load the launcher faster than it recovers. Reduce feed voltage before compensating with a loose launcher readiness tolerance. Recheck for jams using real balls.

## Temporary raw voltage launcher

Hold right trigger to apply **6 V** to both mirrored launcher motors. Hold left bumper for a **4 V low-power shot**, tuned with `Constants.Launcher.LOW_SHOT_VOLTS` (positive and below RUN_VOLTS). Right trigger takes priority when both controls are held. Left bumper also retains drivetrain slow mode. Adjust `Constants.Launcher.RUN_VOLTS` for output (positive, at most 12 V); retain motor inversion to mirror physical rotation. This is voltage control, not RPM regulation. Battery voltage, current limiting and ball loading affect actual speed.

`Constants.Launcher.SPINUP_SECONDS = 2.0` is the continuous shot-hold delay before launch feeding is permitted. During the delay transporter and uptake keep normal sensor rules. After the delay the launch override feeds through occupied sensor stops, with intake-deployed and feeder-health interlocks unchanged. Release both shot controls to stop launcher commands and restore normal collection. Every new press, change between low/full power, or mode restart begins a new delay. Wheels coast down.

There is no PID, target RPM, RPM qualification, speed-sag pause, or speed-recovery timeout in this temporary version. `Launcher/Ready` means delay elapsed, NOT measured full speed. Verify both motors physically accelerate before loading balls; increase the delay if necessary. The launcher cannot detect a stalled or slow wheel from speed in this mode, and can permit feeding even if a controller's internal protection has inhibited its motor.

Current limits, mirrored inversion and coast mode remain configured. Failed configuration or rejected voltage commands latch inhibition until repair and restart; RioLog now reports the CTRE status and affected motor. The previous RPM freshness and aggregate fault-field checks no longer latch software faults; motor internal protections remain active. This change does not establish the cause of the original fault. Inspect `Launcher/LeftFaultBits` and `RightFaultBits` in Tuner if symptoms persist.

Dashboard: `Launcher/ControlMode`, `State`, `Fault`, `CommandedVolts`, `SpinupSeconds`, `Ready`, `Healthy`, `LeftFaultBits`, `RightFaultBits`. Old RPM/target widgets are no longer published by this version and should not be used to judge readiness.

Start with no balls and verify correct mirrored direction and spin-up. Then test staging during the delay, feeding after delay, trigger release, intake retraction, disable and controller disconnect. All constants remain in Constants.java. Earlier project folders are unchanged.



## Tuning record

| Date and project | Setting | Old value | New value | Measured symptom and result | Keep or revert |
| --- | --- | --- | --- | --- | --- |
| | | | | | |

After each successful change, save the measured result here and sync this project. Update the guide's reference values if you change the constants. Hardware verification is still needed even when software tests pass.



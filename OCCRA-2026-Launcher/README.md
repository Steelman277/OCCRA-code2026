# OCCRA 2026 drivetrain + intake + transporter + uptake + launcher

Cumulative project copied from the complete uptake project, with mirrored launcher control and speed-qualified feeding added. Includes the four-CIM mecanum drivetrain, intake deployment and rollers, transporter motors and sensors, and the uptake motor. Includes two Kraken X60 launcher motors at 1:1. The earlier drivetrain, intake, transporter, and uptake projects remain independent and unchanged. Deploying this project replaces the running roboRIO program; the robot runs one project at a time.

## Hardware

| Device | Proposed CAN ID | Mechanism |
| --- | --- | --- |
| Front-left / rear-left Talon SRX + CIM | 1 / 2 | Mecanum left wheels |
| Front-right / rear-right Talon SRX + CIM | 3 / 4 | Mecanum right wheels |
| Intake Kraken X44 | 5 | Rack, 5:1 |
| Intake Kraken X60 | 6 | Rollers, 12:26 |
| Left Kraken X44 (integrated Talon FX) | 7 | Two mechanically linked left wheels, 2:1 reduction |
| Right Kraken X44 (integrated Talon FX) | 8 | Mirrored two right wheels, 2:1 reduction |
| Transport CANrange | 9 | Center of transporter |
| Launcher CANrange | 10 | Further downstream toward launcher; shared by transporter and uptake |
| Uptake Kraken X60 (integrated Talon FX) | 11 | Single wheel, 2:1 reduction |
| Left launcher Kraken X60 | 12 | 1:1 |
| Right launcher Kraken X60 | 13 | 1:1, inverted relative to left |

One motor drives each pair of wheels, two motors total. The ratio is interpreted as two motor rotations per wheel rotation. The right motor starts inverted relative to the left so both sides carry balls toward the launcher; verify physical direction before loading balls. These IDs reserve 1–6 for the existing mechanisms but must be assigned to the devices in Phoenix Tuner X.

## Normal collection controls (both shot controls released)

Connect an Xbox controller in Driver Station USB slot 0. In Teleoperated mode, the transporter runs automatically with the intake fully deployed when the sensors allow it. RETRACTED, DEPLOYING, RETRACTING, UNKNOWN, and FAULT block the transporter. Intake updates first, so starting retraction stops transport in the same loop. Losing the controller, disabling, autonomous, or test mode stops all five mechanisms. A toggles intake deployment, X toggles intake rollers, B explicitly retracts, and Start stops intake/clears its motion fault. X affects intake rollers only.

| Transport tripped | Launcher tripped | Output while intake deployed and sensors healthy |
| --- | --- | --- |
| No | No | Run (assumed behavior for unspecified case) |
| No | Yes | Run (assumed behavior for unspecified case) |
| Yes | No | Run |
| Yes | Yes | Stop |

The transporter automatically resumes when either sensor clears while the intake remains fully deployed. Both sides always receive the same forward voltage magnitude with mirrored inversion. Holding right trigger selects the launch sequence described below and overrides the normal occupancy stops only after both launcher motors are ready.

## Configuration and checkout

Edit `src/main/java/frc/robot/Constants.java (Transporter section)`. Initial commissioning settings are 2 V forward, 25 A stator limit and 20 A supply limit per motor. The 2:1 ratio configures mechanism rotations; this test uses open-loop voltage, not an RPM target.

Each CANrange initially has a 0.15 m proximity threshold and 0.01 m hysteresis. Detection switches on below 0.14 m and clears above 0.16 m. These are placeholders, not measurements of this robot. The minimum signal strength starts at CTRE's 2500 default. Tune each sensor's threshold and signal strength for its mounting and the actual balls using Phoenix Tuner X and dashboard distance readings. Firmware-generated `IsDetected` is the meaning of “tripped.” Hysteresis reduces threshold chatter.

The code requests detection and fault signals at 50 Hz, stops on bad/stale CAN signals (older than 0.10 s) or reported sensor faults, and resumes after healthy feedback returns. Configuration and motor-command failures prevent running until the issue is fixed and code restarted. A valid CAN signal does not prove an object was detected correctly: check sensor alignment, field of view and signal strength. Low-return open air can legitimately mean clear; the code does not treat low optical return alone as a disconnected sensor.

1. With robot disabled, configure unique IDs and compatible 2026 firmware. Confirm CAN wiring and termination.
2. Keep wheels and rack clear, deploy intake with A in Teleop; the transporter starts automatically when the sensors allow. Check that both sides carry a ball toward the launcher. Disable to stop; correct individual inversion constants as needed.
3. Observe `Transporter/TransportMeters`, `LauncherMeters`, `TransportTripped`, `LauncherTripped`, `Healthy`, and `Running` in SmartDashboard. Confirm each sensor reliably distinguishes a ball from an empty path; tune thresholds before relying on automatic stops.
4. Verify retracted and deploying intake block transport and starting retraction stops it immediately. With intake deployed, check all four table rows. Both tripped must stop both sides, and clearing the launcher sensor while transport remains tripped must resume.
5. Check intake retraction, controller disconnect, disable, and CANrange disconnect stop motion. Reconnect the sensor and verify healthy feedback recovery before continuing.

## Build, deploy, and share

Use WPILib 2026 / Java 17. GradleRIO is pinned to 2026.2.1 and the included CTRE Phoenix 6 vendordep is 26.3.0. Phoenix 5 controls the drivetrain Talon SRXs. Set the actual roboRIO team number in `.wpilib/wpilib_preferences.json` (zero is an unset placeholder).

- Build and test: `./gradlew.bat build` (Windows), or `bash gradlew build`.
- Deploy while disabled: WPILib **Deploy Robot Code**, or `./gradlew.bat deploy -PteamNumber=YOUR_NUMBER`.
- Source: `robot-projects/2026-occra-launcher` in the robot-ai-agents workspace.
- Local sharing folder: `C:/Users/SedamD/Documents/GitHub/OCCRA-2026-Launcher`.
- Run `./Sync-ToGitHub.ps1` to copy and hash-verify source, tests, documentation, wrapper and vendor dependencies. No builds, caches, credentials or unrelated projects are copied. The script does not commit or push.

Unit tests cover drivetrain input directions, intake motion and roller toggles, the complete transporter truth table, disable/controller disconnect, unhealthy feedback gating, and stop/resume transitions. Actual motor direction, signal freshness on the bus, optical tuning and ball handling require the hardware checkout above.

References: [CTRE CANrange tuning](https://v6.docs.ctr-electronics.com/en/stable/docs/hardware-reference/canrange/tuning-canrange.html), [CANrange configuration API](https://api.ctr-electronics.com/phoenix6/stable/java/com/ctre/phoenix6/configs/CANrangeConfiguration.html).


## Intake setup

Start with the rack physically IN. CAN 5 is the Kraken X44 rack motor at 5:1; CAN 6 is the Kraken X60 roller motor with 12-tooth motor sprocket driving 26-tooth roller sprocket. Constants.java (Intake section) contains the copied commissioning settings: +/-1.5 V rack motion, 18 A endpoint current sustained for 0.15 seconds after 0.25 seconds startup exclusion, 3-second timeout, and 3 V rollers. Tune on the mechanism. A current spike can also be a jam and does not independently prove physical deployment. Do not restart code with the rack out: the startup assumption is retracted.

Rollers automatically start when deployment completes and stop on retract; X toggles rollers only while deployed. Interrupted travel becomes UNKNOWN; use B to retract for recovery. Disabling or controller loss stops all five mechanisms. Watch Intake/State, Intake/Fault, and Transporter/IntakeDeployed. Earlier projects are unchanged.


## Drivetrain controls and checkout

Left stick: forward/back and strafe. Right stick X: rotation. Hold left bumper for 25% slow mode; normal maximum output is 50%. Constants.java contains the CAN IDs, motor inversion settings, 8% deadband and shared driver port. Drivetrain.java and DriveInputs.java are copied unchanged from the intake project. Motor safety remains enabled.

Support the chassis with wheels clear. Check forward, right strafe and right turn at low speed, then verify controller disconnect and disable stop every mechanism. Right-side motors initially use inverted output; confirm actual gearbox and wiring direction. Mecanum roller axes must form an X viewed from above. WPILib 2026.2.1 is pinned; its actual mecanum sign convention is covered by the input tests. Rerun tests before upgrading.



## Normal uptake behavior and checkout (both shot controls released)

The uptake automatically runs in Teleop while the intake is fully DEPLOYED and the launcher CANrange is healthy and clear. Both uptake and transporter operate automatically; neither requires a bumper press. X only pauses intake rollers; it does not pause uptake or transport. Starting intake retraction immediately stops uptake in the same control loop.

| Intake state | Launcher sensor | Uptake |
| --- | --- | --- |
| DEPLOYED | Clear and healthy | Run |
| DEPLOYED | Tripped | Stop |
| Any other state | Either | Stop |
| Any | Stale, disconnected, faulted, or configuration failed | Stop |

Uptake resumes automatically when the launcher clears and the intake remains deployed. Disable, controller loss, autonomous and test mode stop it. Motor configuration or command errors latch inhibition until code is restarted after fixing the issue. Transporter owns the single launcher CANrange object and refreshes the snapshot before uptake uses it; there is no second sensor object or conflicting configuration. Uptake does not depend on the center transporter sensor or transport motor configuration.

`Constants.java (Uptake section)` proposes CAN ID 11, two motor turns per wheel turn, non-inverted output, 2 V running output, 30 A stator limit and 20 A supply limit. These are commissioning defaults, not measured mechanism requirements. This is open-loop voltage control, not RPM control. The launcher trip threshold remains in `Constants.java (Transporter section)`; tune it for actual mounting and balls as described above.

1. Assign the uptake Kraken CAN ID 11 in Phoenix Tuner X and verify compatible firmware. Start with the intake physically retracted.
2. Keep drive sticks centered and mechanisms clear. With launcher sensor clear, deploy intake using A; uptake must start only when the deployed endpoint is detected. It can start without holding right bumper.
3. Briefly verify uptake moves balls toward the launcher; disable and adjust INVERTED if direction is wrong. Tune RUN_VOLTS to the required transport speed.
4. Trip the launcher sensor: uptake must stop even if the center transporter sensor is clear. Clear launcher detection: uptake must resume while deployed.
5. Retract with A: uptake must stop immediately. Verify controller loss, disable and disconnected launcher CANrange also stop uptake. Verify earlier drive and intake controls still work.

The added tests cover uptake permissions, occupied/clear transitions, unhealthy feedback inhibition, and the intake lifecycle. Build success does not replace physical motor direction, optical detection, or mechanism checkout.


## Temporary raw voltage launcher

Hold right trigger to apply **6 V** to both mirrored launcher motors. Hold left bumper for a **4 V low-power shot**, tuned with `Constants.Launcher.LOW_SHOT_VOLTS` (positive and below RUN_VOLTS). Right trigger takes priority when both controls are held. Left bumper also retains drivetrain slow mode. Adjust `Constants.Launcher.RUN_VOLTS` for output (positive, at most 12 V); retain motor inversion to mirror physical rotation. This is voltage control, not RPM regulation. Battery voltage, current limiting and ball loading affect actual speed.

`Constants.Launcher.SPINUP_SECONDS = 2.0` is the continuous shot-hold delay before launch feeding is permitted. During the delay transporter and uptake keep normal sensor rules. After the delay the launch override feeds through occupied sensor stops, with intake-deployed and feeder-health interlocks unchanged. Release both shot controls to stop launcher commands and restore normal collection. Every new press, change between low/full power, or mode restart begins a new delay. Wheels coast down.

There is no PID, target RPM, RPM qualification, speed-sag pause, or speed-recovery timeout in this temporary version. `Launcher/Ready` means delay elapsed, NOT measured full speed. Verify both motors physically accelerate before loading balls; increase the delay if necessary. The launcher cannot detect a stalled or slow wheel from speed in this mode, and can permit feeding even if a controller's internal protection has inhibited its motor.

Current limits, mirrored inversion and coast mode remain configured. Failed configuration or rejected voltage commands latch inhibition until repair and restart; RioLog now reports the CTRE status and affected motor. The previous RPM freshness and aggregate fault-field checks no longer latch software faults; motor internal protections remain active. This change does not establish the cause of the original fault. Inspect `Launcher/LeftFaultBits` and `RightFaultBits` in Tuner if symptoms persist.

Dashboard: `Launcher/ControlMode`, `State`, `Fault`, `CommandedVolts`, `SpinupSeconds`, `Ready`, `Healthy`, `LeftFaultBits`, `RightFaultBits`. Old RPM/target widgets are no longer published by this version and should not be used to judge readiness.

Start with no balls and verify correct mirrored direction and spin-up. Then test staging during the delay, feeding after delay, trigger release, intake retraction, disable and controller disconnect. All constants remain in Constants.java. Earlier project folders are unchanged.



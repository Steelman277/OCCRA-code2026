package frc.robot.subsystems;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Launcher extends SubsystemBase {
    static class Constants {
        static final double LAUNCH_ANGLE = 0.350;
        static final double RESET_ANGLE = 0.880;
    }

    private final PWMVictorSPX launcher;
    private final DutyCycleEncoder launcherEncoder;
    private final PIDController launcherPID = new PIDController(1.8, 0, 0.002);

    public Launcher() {
        launcher = new PWMVictorSPX(6);
        launcher.setInverted(true);

        launcherEncoder = new DutyCycleEncoder(2);
        launcherEncoder.setPositionOffset(0);
        
        launcherPID.setSetpoint(Constants.RESET_ANGLE);
    }

    public Command launch() {
        return runOnce(() -> launcherPID.setSetpoint(Constants.LAUNCH_ANGLE));
    }

    public Command reset() {
        return runOnce(() -> launcherPID.setSetpoint(Constants.RESET_ANGLE));
    }

    @Override
    public void periodic() {
        double calculated = launcherPID.calculate(launcherEncoder.getAbsolutePosition());
        if (DriverStation.isEnabled()) {
            if (launcherPID.getSetpoint() == Constants.RESET_ANGLE) calculated *= 0.35;
            if (launcherEncoder.getAbsolutePosition() - launcherPID.getSetpoint() < -0.4) launcher.set(-0.2); 
            else launcher.set(calculated);
        }

        // launcher.set(0.08);

        SmartDashboard.putNumber("Launcher position", launcherEncoder.getAbsolutePosition());
        SmartDashboard.putNumber("Launcher target", launcherPID.getSetpoint());
        SmartDashboard.putNumber("Launcher PID force", calculated);
        SmartDashboard.putBoolean("Toggled", launcherPID.getSetpoint() == Constants.LAUNCH_ANGLE);
    }
}

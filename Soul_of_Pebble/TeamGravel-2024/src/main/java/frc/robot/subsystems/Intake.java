package frc.robot.subsystems;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {
    // TODO put actual values here
    static class Constants {
        static final double ROLLER_SPEED = 0.75;

        static final double INTAKE_ANGLE = 0;
        static final double STOW_ANGLE = 45;
        static final double CLIMB_ANGLE = 90;
    }

    public enum Position {
        INTAKE(Constants.INTAKE_ANGLE),
        STOW(Constants.STOW_ANGLE),
        CLIMB(Constants.CLIMB_ANGLE);

        public final double angle;

        Position(double angle) { this.angle = angle; }
    }

    private final PWMVictorSPX rollers;

    private final PWMVictorSPX pivotLeft;
    private final PWMVictorSPX pivotRight;
    private final PIDController leftPID = new PIDController(0.1, 0, 0.001);
    private final PIDController rightPID = new PIDController(leftPID.getP(), 0, leftPID.getD());
    private final DutyCycleEncoder leftEncoder;
    private final DutyCycleEncoder rightEncoder;

    public Intake() {
        rollers = new PWMVictorSPX(9);

        pivotLeft = new PWMVictorSPX(8);
        pivotRight = new PWMVictorSPX(7);

        leftEncoder = new DutyCycleEncoder(0);
        leftEncoder.setPositionOffset(0);
        
        rightEncoder = new DutyCycleEncoder(1);
        rightEncoder.setPositionOffset(0);
    }

    public Command runRollers() {
        return runOnce(() -> rollers.set(Constants.ROLLER_SPEED));
    }

    public Command stopRollers() {
        return runOnce(() -> rollers.set(0.0));
    }

    public Command moveTo(Position position) {
        return runOnce(() -> {
            leftPID.setSetpoint(position.angle);
            rightPID.setSetpoint(position.angle);
        });
    }

    @Override
    public void periodic() {
        double outputLeft = leftPID.calculate(leftEncoder.getAbsolutePosition());
        double outputRight = rightPID.calculate(rightEncoder.getAbsolutePosition());
        pivotLeft.set(outputLeft);
        pivotRight.set(outputRight);
    }
}

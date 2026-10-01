/*package frc.robot.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;



THIS IS THE FRAMEWORK FOR AN ENTIRE UNUSED SUBSYSTEM

// this is a commit



public class Transfer extends SubsystemBase {
    static class Constants {
        static final double ROLLER_SPEED = 1.0;
        static final double REVERSE_SPEED = -0.2;
        static final double EJECT_SPEED = -1.0;
    }

    private final PWMVictorSPX leftRollers;
    private final PWMVictorSPX rightRollers;

    public Transfer() {
        leftRollers = new PWMVictorSPX(0);
        rightRollers = new PWMVictorSPX(1);
        leftRollers.setInverted(true);
        rightRollers.setInverted(false);
    }

    public Command runRollers() {
        return runOnce(() -> {
            leftRollers.set(Constants.ROLLER_SPEED);
            rightRollers.set(Constants.ROLLER_SPEED);
        });
    }

    public Command reverseRollers() {
        return runOnce(() -> {
            leftRollers.set(Constants.REVERSE_SPEED);
            rightRollers.set(Constants.REVERSE_SPEED);
        });
    }

    public Command eject() {
        return runOnce(() -> {
            leftRollers.set(Constants.REVERSE_SPEED);
            rightRollers.set(Constants.REVERSE_SPEED);
        });
    }

    public Command stopRollers() {
        return runOnce(() -> {
            leftRollers.set(0.0);
            rightRollers.set(0.0);
        });
    }
}
*/
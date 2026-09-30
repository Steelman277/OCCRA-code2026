package frc.robot.subsystems;

//import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
//import frc.robot.subsystems.Transfer.Constants;

public class Intake extends SubsystemBase {
    // TODO put actual values here
    static class Constants {
        static final double ROLLER_SPEED = 0.75;
    }

    //public enum Position {
        //INTAKE(Constants.INTAKE_ANGLE),
        //STOW(Constants.STOW_ANGLE),
        //CLIMB(Constants.CLIMB_ANGLE);

        //public final double angle;

        //Position(double angle) { this.angle = angle; }
     //}

    private final PWMVictorSPX rollerOne;
    private final PWMVictorSPX rollerTwo;

    //private final PIDController leftPID = new PIDController(0.1, 0, 0.001);
    //private final PIDController rightPID = new PIDController(leftPID.getP(), 0, leftPID.getD());
    //private final DutyCycleEncoder leftEncoder;
    //private final DutyCycleEncoder rightEncoder;

    public Intake() {
        rollerOne = new PWMVictorSPX(6);
        rollerTwo = new PWMVictorSPX(7);
        rollerTwo.setInverted(true);
    }
    public Command runRollersEqual() {
        return runOnce(() -> {
            rollerOne.setVoltage(5);
            rollerTwo.setVoltage(5);
            //used for general or autonomous intaking
        });
    }

    public Command runRollersOffset() {
        return runOnce(() -> {
            rollerOne.setVoltage(5);
            rollerTwo.setVoltage(1.5);
            //used for taking in cubes that are diagonal relative to the intake
        });
    }
    public Command runRollersOffset2() {
        return runOnce(() -> {
            rollerOne.setVoltage(1.5);
            rollerTwo.setVoltage(5);
            //used for taking in cubes that are diagonal relative to the intake
        });
    }

    public Command spinCubeCW() {
        return runOnce(() -> {
            rollerOne.setVoltage(3);
            rollerTwo.setVoltage(-1.5);
        });
    }
//sets one wheel fast forward and one slow backward to hopefully spin cubes within the intake
    public Command spinCubeCCW() {
        return runOnce(() -> {
            rollerOne.setVoltage(-1.5);
            rollerTwo.setVoltage(3);
        });
    }

    public Command variableOuttake(double OuttakeSpeed) {
        return runOnce (() -> {
            rollerOne.setVoltage(OuttakeSpeed * -8);
            rollerTwo.setVoltage(OuttakeSpeed * -8);
            //Sets outtake speed to however much the right trigger is pressed with a multiplier to convert to voltage
        });
    }

    public Command reverseRollers() { 
        return runOnce(() -> {
            rollerOne.setVoltage(-4.5);
            rollerTwo.setVoltage(-4.5);
            //general outtake
        });

    }
    public Command stopRollers() {
        return runOnce(() -> {
            rollerOne.setVoltage(.75);
            rollerTwo.setVoltage(.75);
            //stops rollers, but applies small voltage to help hold cubes in without burning motors too bad
        });
    }

    /*public Command bigYeet() {
        return runOnce(() -> {
            rollerOne.setVoltage(-9);
            rollerTwo.setVoltage(-9);
        });
    }*/

   // @Override
    //public void periodic() {
        //double outputLeft = leftPID.calculate(leftEncoder.getAbsolutePosition());
        //double outputRight = rightPID.calculate(rightEncoder.getAbsolutePosition());
        //pivotLeft.set(outputLeft);
        //pivotRight.set(outputRight);
   // }
}

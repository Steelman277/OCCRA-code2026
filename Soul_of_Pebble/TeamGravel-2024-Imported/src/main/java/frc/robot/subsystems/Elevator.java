package frc.robot.subsystems;
import edu.wpi.first.math.controller.PIDController;
//import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
//import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Elevator extends SubsystemBase {
    public static class Constants {
        public static double STOW = 0.0;
        public static double LOW = 0.4;
        public static double MEDIUM = 0.72;
        public static double HIGH = 1.02;
        //sets preset positions for elevator


    }
    private double encoderOffset;
    private final PWMVictorSPX elevator;
    private final PWMVictorSPX elevatorFollower;
    private final PIDController pid;
    private final Encoder elevatorEncoder;
    private boolean usePID = true;

    public Elevator() {
        elevator = new PWMVictorSPX(4);
        elevator.addFollower(elevatorFollower = new PWMVictorSPX(5));
        elevator.setInverted(true);
        elevatorFollower.setInverted(true);
        pid = new PIDController(36,0, 0 /*, new trapezoidProfileConstraints()*/);
        elevatorEncoder = new Encoder(0,1, false, Encoder.EncodingType.k2X);
        
        elevator.setVoltage(0);
    }

    @Override
    public void periodic() {
        double position = elevatorEncoder.getDistance()/-28000.0 - encoderOffset;
        double speed =  pid.calculate(position);
        if (usePID == true) elevator.setVoltage(speed + 1);
        SmartDashboard.putNumber("ElevatorPosition", position);
        SmartDashboard.putNumber("ElevatorSpeed", speed);
        //creates PID variables and smart dashboard displays
    }

    public Command setPosition(double position) {
        return runOnce(() -> {
            usePID = true;
            pid.setSetpoint(position);
            //runs PID to the desired position
        });
    }
    public Command zeroElevator() {
        return runOnce(() -> encoderOffset = elevatorEncoder.getDistance()/-28000.0);
    
    }
    public Command setVoltage(double voltage) {
        return runOnce(() -> {
            elevator.setVoltage(voltage);
            usePID = false;
            
        });
    }


};

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

public class Drivetrain extends SubsystemBase {
    public final MecanumDrive mecanumDrive;
    public final CommandXboxController controller;

    private final PWMVictorSPX frontLeft;
    private final PWMVictorSPX backLeft;
    private final PWMVictorSPX frontRight;
    private final PWMVictorSPX backRight;

    public Drivetrain(CommandXboxController controller) {
        this.controller = controller;

        frontLeft = new PWMVictorSPX(3);
        frontRight = new PWMVictorSPX(2);
        backLeft = new PWMVictorSPX(1);
        backRight = new PWMVictorSPX(0);

        frontLeft.setInverted(false);
        frontRight.setInverted(true);
        backLeft.setInverted(false);
        backRight.setInverted(true);

        mecanumDrive = new MecanumDrive(frontLeft::set, backLeft::set, frontRight::set, backRight::set);
    }

    public Command driveAuto(double x, double y, double rot) {
        return run(() -> mecanumDrive.driveCartesian(-y, x, rot));
    }

    public void teleopPeriodic() {
        double speed = (controller.getHID().getRightTriggerAxis() > 0.5) ? 0.3 : 1.0;
        mecanumDrive.driveCartesian(controller.getLeftY() * speed, -controller.getLeftX() * speed, controller.getRightX() * speed);
    }

    public Command stop() {
        return runOnce(() -> mecanumDrive.stopMotor());
    }
}

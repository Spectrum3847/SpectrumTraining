package frc.robot.intake;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;

public class IntakeCommands {
    private static Intake intake = Robot.intake;

    public static void setupDefaultCommand() {
        intake.setDefaultCommand(intake.runVelocity(0).withName("Intake.default"));
    }

    public static Command runFull() {
        return intake.runVelocity(intake.config.fullSpeed);
    }

    public static Command eject() {
        return intake.runVelocity(intake.config.ejectSpeed);
    }
}

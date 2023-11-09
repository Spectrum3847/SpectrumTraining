package frc.robot.intake.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.intake.Intake;

public class IntakeCommands {
    private static Intake intake = Robot.intake;
    
    public static Command runFull() {
        return intake.runVelocity(intake.config.fullSpeed);
    }

    public static Command eject() {
        return intake.runVelocity(intake.config.ejectSpeed);
    }
}

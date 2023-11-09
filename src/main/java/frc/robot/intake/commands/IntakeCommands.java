package frc.robot.intake.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.intake.Intake;

public class IntakeCommands {
    private Intake intake = Robot.intake;
    
    public Command runIntake() {
        return intake.runVelocity(intake.config.fullSpeed);
    }

    public Command ejectIntake() {
        return intake.runVelocity(intake.config.ejectSpeed);
    }
}

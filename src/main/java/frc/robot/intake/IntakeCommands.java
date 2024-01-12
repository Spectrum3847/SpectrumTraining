package frc.robot.intake;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;

public class IntakeCommands {
    private static Intake intake = Robot.intake;

    public static void setupDefaultCommand() {
        intake.setDefaultCommand(intake.run(() -> intake.stop()).withName("Intake.default"));
    }

    public static Command intake() {
        return intake.runVelocity(intake.config.intake).withName("Intake.intake");
    }

    public static Command slowIntake() {
        return intake.runVelocity(intake.config.slowIntake).withName("Intake.slowIntake");
    }

    public static Command eject() {
        return intake.runVelocity(intake.config.eject).withName("Intake.eject");
    }
}

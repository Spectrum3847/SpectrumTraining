package frc.robot.swerve.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.pilot.commands.PilotCommands;
import java.util.function.DoubleSupplier;

public class SwerveCommands {

    public static void setupDefaultCommand() {
        Robot.swerve.setDefaultCommand(PilotCommands.pilotDrive());
    }

    public static Command Xbrake() {
        return Xbrake.run().withName("Swerve: Xbrake");
    }

    public static Command Drive(
            DoubleSupplier velocityX, DoubleSupplier velocityY, DoubleSupplier rotationalRate) {
        return Drive.run(velocityX, velocityY, rotationalRate)
                .withName("Swerve: Drive")
                .ignoringDisable(true);
    }
}

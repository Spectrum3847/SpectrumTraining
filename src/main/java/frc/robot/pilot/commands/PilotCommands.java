package frc.robot.pilot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.swerve.commands.SwerveCommands;

/** This class should have any command calls that directly call the PilotGamepad */
public class PilotCommands {
    /** Set default command to turn off the rumble */
    public static void setupDefaultCommand() {
        Robot.pilot.setDefaultCommand(rumble(0, 99999).repeatedly().withName("Pilot.default"));
    }

    /** Command that can be used to rumble the pilot controller */
    public static Command rumble(double intensity, double durationSeconds) {
        return Robot.pilot.rumbleCommand(intensity, durationSeconds);
    }

    public static Command pilotDrive() {
        return SwerveCommands.Drive(
                        () -> Robot.pilot.getDriveFwdPositive(),
                        () -> Robot.pilot.getDriveLeftPositive(),
                        () -> Robot.pilot.getDriveCCWPositive(),
                        true,
                        true)
                .withName("Swerve.PilotDrive");
    }
}

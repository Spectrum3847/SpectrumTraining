package frc.robot.pilot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;

public class PilotCommands {
    /** Set default command to turn off the rumble */
    public static void setupDefaultCommand() {
        Robot.pilot.setDefaultCommand(
                rumble("", 0, 99999).repeatedly().withName("DisablePilotRumble"));
    }

    /** Command that can be used to rumble the pilot controller */
    public static Command rumble(String name, double intensity, double durationSeconds) {
        return Robot.pilot.rumble(name, intensity, durationSeconds);
    }
}

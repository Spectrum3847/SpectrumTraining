package frc.robot.pilot.commands;

import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.leds.LEDsConfig.Section;
import frc.robot.leds.commands.LEDsCommands;
import frc.robot.training.commands.TrainingCommands;

public class PilotCommands {
    /** Set default command to turn off the rumble */
    public static void setupDefaultCommand() {
        Robot.pilot.setDefaultCommand(rumble(0, 99999).repeatedly().withName("Pilot.default"));
    }

    /** Command that can be used to rumble the pilot controller */
    public static Command rumble(double intensity, double durationSeconds) {
        return Robot.pilot.rumbleCommand(intensity, durationSeconds);
    }

    public static Command printOnceCommand() {
        return TrainingCommands.printOnceCommand("Print Once: A Button Pressed")
                .withName("Training.A Button");
    }

    public static Command periodicCommand() {
        return TrainingCommands.printPeriodicCommand("Print Periodic: B Button Pressed")
                .withName("Training.periodicCommand + LEDs.purple");
    }

    public static Command periodicTimeoutCommand() {
        return TrainingCommands.printPeriodicCommand(
                        "Print Periodic with Timeout: Y Button Pressed and No Bumpers Pressed")
                .withTimeout(1)
                .withName("Training.periodicTimeoutCommand");
    }

    public static Command sequentialGroupCommand() {
        return TrainingCommands.sequentialPrintCommand(
                        "Print Instant: Y Button Pressed and Left Bumper Pressed",
                        "Print Periodic: Y Button Pressed and Left Bumper Pressed")
                .withName("Training.Y + Left Bumpers");
    }

    public static Command parrellelGroupCommand() {
        return TrainingCommands.parallelPrintCommand(
                        "Print Periodic 1: Y Button Pressed",
                        "Print Periodic 2: and Right Bumper Pressed")
                .withName("Training.Y + Right Bumper");
    }

    public static Command solidPurpleLED() {
        return LEDsCommands.solid(Color.kPurple, 2).withName("LEDs.solidPurpleLED");
    }

    public static Command strobeOrangeLED() {
        return LEDsCommands.strobe(Section.FULL, Color.kOrange, 0.5, 3)
                .withName("LEDs.strobeOrangeLED");
    }
}

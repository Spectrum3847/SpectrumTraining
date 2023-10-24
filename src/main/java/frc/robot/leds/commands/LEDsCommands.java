package frc.robot.leds.commands;

import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Robot;
import frc.robot.leds.LEDsConfig;
import frc.robot.leds.LEDsConfig.Section;
import frc.robot.leds.LEDsPatternes;

public class LEDsCommands {

    public static void setupDefaultCommand() {
        Robot.leds.resetDefault();
    }

    public static Command defaultCommand() {
        return rainbow(0).withName("LEDs.default");
    }

    public static Command runLEDPattern(Runnable r, int priority) {
        return Commands.run(() -> Robot.leds.setPatternIfHigherPriority(r, priority))
                .ignoringDisable(true)
                .finallyDo((b) -> Robot.leds.resetDefault())
                .withName("LEDs.runLEDCommand");
    }

    public static Command solid(Color color, int priority) {
        return solid(Section.FULL, color, priority);
    }

    public static Command solid(Section section, Color color, int priority) {
        return runLEDPattern(() -> LEDsPatternes.solid(section, color), priority)
                .withName("LEDs.solid");
    }

    public static Command solid(double percent, Color color, int priority) {
        return runLEDPattern(() -> LEDsPatternes.solid(percent, color), priority)
                .withName("LEDs.solid");
    }

    public static Command strobe(Color color, int priority) {
        return strobe(Section.FULL, color, 0.5, priority);
    }

    public static Command strobe(Section section, Color color, double duration, int priority) {
        return runLEDPattern(() -> LEDsPatternes.strobe(section, color, duration), priority)
                .withName("LEDs.strobe");
    }

    public static Command breath(Color c1, Color c2, int priority) {
        return breath(Section.FULL, c1, c2, 1, priority);
    }

    public static Command breath(
            Section section, Color c1, Color c2, double duration, int priority) {
        return runLEDPattern(() -> LEDsPatternes.breath(section, c1, c2, duration), priority)
                .withName("LEDs.breath");
    }

    public static Command rainbow(int priority) {
        return rainbow(Section.FULL, LEDsConfig.length, 1, priority);
    }

    public static Command rainbow(
            Section section, double cycleLength, double duration, int priority) {
        return runLEDPattern(() -> LEDsPatternes.rainbow(section, cycleLength, duration), priority)
                .withName("LEDs.rainbow");
    }

    public static Command wave(Color c1, Color c2, int priority) {
        return wave(Section.FULL, c1, c2, LEDsConfig.length, 1, priority);
    }

    public static Command wave(
            Section section,
            Color c1,
            Color c2,
            double cycleLength,
            double duration,
            int priority) {
        return runLEDPattern(
                        () -> LEDsPatternes.wave(section, c1, c2, cycleLength, duration), priority)
                .withName("LEDs.wave");
    }
}

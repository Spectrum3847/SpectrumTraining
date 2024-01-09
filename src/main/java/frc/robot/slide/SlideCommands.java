package frc.robot.slide;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;

public class SlideCommands {
    private static Slide slide = Robot.slide;

    public static void setupDefaultCommand() {
        slide.setDefaultCommand(
                slide.runStop()
                        .withTimeout(0.25)
                        .andThen(slide.holdPosition())
                        .withName("Slide.default"));
    }

    public static Command fullExtend() {
        return slide.runPosition(slide.config.fullExtend).withName("Slide.fullExtend");
    }

    public static Command home() {
        return slide.runPosition(slide.config.home).withName("Slide.home");
    }
}

package frc.robot.slide;

import frc.robot.Robot;

public class SlideCommands {
    private static Slide slide = Robot.slide;

    public static void setupDefaultCommand() {
        slide.setDefaultCommand(slide.runStop().withName("Slide.default"));
    }
    
}

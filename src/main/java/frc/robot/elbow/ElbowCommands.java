package frc.robot.elbow;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;

public class ElbowCommands {
    private static Elbow elbow = Robot.elbow;

    public static void setupDefaultCommand() {
        elbow.setDefaultCommand(
                elbow.runStop()
                        .withTimeout(0.25)
                        .andThen(elbow.holdPosition())
                        .withName("Elbow.default"));
    }

    public static Command fullExtend() {
        return elbow.runPosition(elbow.config.fullExtend).withName("Elbow.fullExtend");
    }

    public static Command home() {
        return elbow.runPosition(elbow.config.home).withName("Elbow.home");
    }
}

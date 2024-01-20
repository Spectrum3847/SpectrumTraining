package frc.robot.shoulder;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;

public class ShoulderCommands {
    private static Shoulder shoulder = Robot.shoulder;

    public static void setupDefaultCommand() {
        shoulder.setDefaultCommand(shoulder.runHoldShoulder().withName("Shoulder.default"));
    }
    /* Misc Positions */

    public static Command stow() {
        return shoulder.runPosition(shoulder.config.stow).withName("Shoulder.stow");
    }

    /* Intaking Positions */

    public static Command intake() {
        return shoulder.runPosition(shoulder.config.intake).withName("Shoulder.intake");
    }

    public static Command airIntake() {
        return shoulder.runPosition(shoulder.config.airIntake).withName("Shoulder.airIntake");
    }

    public static Command shelfIntake() {
        return shoulder.runPosition(shoulder.config.shelfIntake).withName("Shoulder.shelfIntake");
    }

    /* Scoring Positions */

    public static Command home() {
        return shoulder.runPosition(shoulder.config.home).withName("Shoulder.home");
    }

    public static Command floor() {
        return shoulder.runPosition(shoulder.config.floor).withName("Shoulder.floor");
    }

    public static Command score() {
        return shoulder.runPosition(shoulder.config.scorePos).withName("Shoulder.score");
    }

    public static Command prescore() {
        return shoulder.runPosition(shoulder.config.prescore).withName("Shoulder.prescore");
    }

    public static Command coneTop() {
        return shoulder.runPosition(shoulder.config.coneTop).withName("Shoulder.coneTop");
    }

    public static Command coneMid() {
        return shoulder.runPosition(shoulder.config.coneMid).withName("Shoulder.coneMid");
    }

    public static Command cubeUp() {
        return shoulder.runPosition(shoulder.config.cubeUp).withName("Shoulder.cubeUp");
    }
}

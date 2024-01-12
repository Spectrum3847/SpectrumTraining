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

    /* Misc Positions */
    public static Command stow() {
        return elbow.runPosition(elbow.percentToRot(elbow.config.stow)).withName("Elbow.stow");
    }

    /* Intaking Positions */

    public static Command intake() {
        return elbow.runPosition(elbow.percentToRot(elbow.config.intake)).withName("Elbow.intake");
    }

    public static Command airIntake() {
        return elbow.runPosition(elbow.percentToRot(elbow.config.airIntake)).withName("Elbow.airIntake");
    }

    public static Command shelfIntake() {
        return elbow.runPosition(elbow.percentToRot(elbow.config.shelfIntake)).withName("Elbow.shelfIntake");
    }

    /* Scoring Positions */

    public static Command floor() {
        return elbow.runPosition(elbow.percentToRot(elbow.config.floor)).withName("Elbow.floor");
    }

    public static Command score() {
        return elbow.runPosition(elbow.percentToRot(elbow.config.scorePos)).withName("Elbow.scorePos");
    }

    public static Command prescore() {
        return elbow.runPosition(elbow.percentToRot(elbow.config.prescore)).withName("Elbow.prescore");
    }

    public static Command coneTop() {
        return elbow.runPosition(elbow.percentToRot(elbow.config.coneTop)).withName("Elbow.coneTop");
    }

    public static Command coneMid() {
        return elbow.runPosition(elbow.percentToRot(elbow.config.coneMid)).withName("Elbow.coneMid");
    }

    public static Command cubeUp() {
        return elbow.runPosition(elbow.percentToRot(elbow.config.cubeUp)).withName("Elbow.cubeUp");
    }

    public static Command home() {
        return elbow.runPosition(elbow.percentToRot(elbow.config.home)).withName("Elbow.home");
    }
}

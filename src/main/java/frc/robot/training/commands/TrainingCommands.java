package frc.robot.training.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Robot;
import frc.robot.RobotTelemetry;
import frc.robot.training.Training;

/**
 * Each subsystem has a SubsystemCommands file that includes methods that return commands for that
 * subsystem
 */
public class TrainingCommands {

    private static Training training = Robot.training;
    /**
     * This method will setup the default command for the subsystem This should be called from
     * RobotInit in Robot.java
     */
    public static void setupDefaultCommand() {
        training.setDefaultCommand(
                printOnceCommand("Training Default Command is running")
                        .withName("Training.default"));
    }

    public static Command fullCommand() {
        return new FullCommand();
    }

    /** Specific Commands */
    public static Command printOnceCommand() {
        return printOnceCommand("Print Once").withName("Training.Print Once");
    }

    public static Command periodicCommand() {
        return printPeriodicCommand("Print Periodic").withName("Training.periodicCommand");
    }

    public static Command periodicTimeoutCommand() {
        return printPeriodicCommand("Print Periodic with Timeout")
                .withTimeout(1)
                .withName("Training.periodicTimeoutCommand");
    }

    public static Command sequentialGroupCommand() {
        return sequentialPrintCommand("Print Instant", "Print Periodic")
                .withName("Training.Sequential Group");
    }

    public static Command parellelGroupCommand() {
        return parallelPrintCommand("Print Periodic 1", "Print Periodic 2")
                .withName("Training.Parellel Group");
    }

    /** Common Commands */
    /**
     * This command will print the text to the console once and then end immediately. If this is
     * called by a whileTrue trigger it will start over again.
     *
     * @param txt
     * @return
     */
    public static Command printInstantCommand(String txt) {
        return training.runOnce(() -> RobotTelemetry.print(txt))
                .ignoringDisable(true)
                .withName("Training.PrintInstantCommand");
    }

    /**
     * This command will print the text to the console once and continue running but not printing ()
     * -> {} is an empty lambda function to do nothing
     *
     * @param txt
     * @return
     */
    public static Command printOnceCommand(String txt) {
        return training.startEnd(() -> RobotTelemetry.print(txt), () -> {})
                .ignoringDisable(true)
                .withName("Training.PrintOunceCommand");
    }

    /**
     * This command will print the text to the console every execute loop of the robot
     *
     * @param txt
     * @return
     */
    public static Command printPeriodicCommand(String txt) {
        return training.run(() -> RobotTelemetry.print(txt))
                .ignoringDisable(true)
                .withName("Training.PrintPerioicCommand");
    }

    /**
     * This command will print one string once and then start printing a second string every
     * execute. This command creates a Sequential Command Group using the andThen method. Only the
     * name of the Sequential Command Group is used as the indvidual commands aren't actually run by
     * the scheduler. Command Groups require all of the subsystems of every command in the group.
     *
     * @param commandName
     * @param oneTimeText
     * @param periodicText
     * @return
     */
    public static Command sequentialPrintCommand(String oneTimeText, String periodicText) {
        return printInstantCommand(oneTimeText)
                .andThen(printPeriodicCommand(periodicText))
                .ignoringDisable(true)
                .withName("Training.Sequential Group");
    }

    /**
     * This command will print two strings every execute loop of the robot. They will run together
     * at the same time because they are in a Parrell Command Group. This is created using hte
     * alongWith method.
     */
    public static Command parallelPrintCommand(String periodicText1, String periodicText2) {
        return printPeriodicCommand(periodicText1)
                .alongWith(Commands.print(periodicText2).repeatedly())
                // Have to use PrintCommand and repeatedly becuase it doesn't require the Training
                // Subystem
                .ignoringDisable(true)
                .withName("Training.Parallel Group")
                .finallyDo(
                        () -> {
                            RobotTelemetry.print("Finally Do");
                        });
    }
}

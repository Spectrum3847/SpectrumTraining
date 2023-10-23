package frc.robot.training.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import frc.robot.Robot;
import frc.robot.RobotTelemetry;

/**
 * Each subsystem has a SubsystemCommands file that includes methods that return commands for that
 * subsystem
 */
public class TrainingCommands {

    /**
     * This method will setup the default command for the subsystem This should be called from
     * RobotInit in Robot.java
     */
    public static void setupDefaultCommand() {
        // subsystem.setDefaultCommand(new Command());
        Robot.training.setDefaultCommand(
                printOnceCommand("Training Default", "Training Default Command is running"));
    }

    /**
     * This command will print the text to the console once and then end immediately. If this is
     * called by a whileTrue trigger it will start over again.
     *
     * @param txt
     * @return
     */
    public static Command printInstantCommand(String commandName, String txt) {
        return new InstantCommand(() -> RobotTelemetry.print(txt), Robot.training)
                .ignoringDisable(true)
                .withName(commandName);
    }

    /**
     * This command will print the text to the console once and continue running but not printing
     *
     * @param txt
     * @return
     */
    public static Command printOnceCommand(String commandName, String txt) {
        return new StartEndCommand(() -> RobotTelemetry.print(txt), () -> {}, Robot.training)
                .ignoringDisable(true)
                .withName(commandName);
    }

    /**
     * This command will print the text to the console every execute loop of the robot
     *
     * @param txt
     * @return
     */
    public static Command printPeriodicCommand(String commandName, String txt) {
        return new RunCommand(() -> RobotTelemetry.print(txt), Robot.training)
                .ignoringDisable(true)
                .withName(commandName);
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
    public static Command sequentialPrintCommand(
            String commandName, String oneTimeText, String periodicText) {
        return printInstantCommand("Instand", oneTimeText)
                .andThen(printPeriodicCommand("Periodic", periodicText))
                .ignoringDisable(true)
                .withName(commandName + " Sequential Group");
    }

    /**
     * This command will print two strings every execute loop of the robot. They will run together
     * at the same time because they are in a Parrell Command Group. This is created using hte
     * alongWith method.
     */
    public static Command parallelPrintCommand(
            String commandName, String periodicText1, String periodicText2) {
        return printPeriodicCommand("Periodic 1", periodicText1)
                .alongWith(new PrintCommand(periodicText2).repeatedly())
                // Have to use PrintCommand and repeatedly becuase it doesn't require the Training
                // Subystem
                .ignoringDisable(true)
                .withName(commandName + " Parallel Group");
    }
}

package frc.robot.training.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.RobotTelemetry;

/** This an example Command that includes all the common methods a command should override */
public class FullCommand extends Command {
    private double startTime = 0;

    /** Creates a new FullCommand. */
    public FullCommand() {

        /* Use addRequirements() here to declare subsystem dependencies. */
        addRequirements(Robot.training);
        setName("Training.FullCommand");
        /** Shows when the constructor runs and prints the time */
        RobotTelemetry.print("FullCommand: constructor: ");
    }

    /** Called when the command is initially scheduled. */
    public void initialize() {
        /**
         * This method gets the number of seconds since the roboRIO turned on and stores it so we
         * know when the command started
         */
        startTime = Timer.getFPGATimestamp();

        /** Shows when inttialize runs and prints the time to the console */
        RobotTelemetry.print("FullCommand: initialize: " + getElapsedTime());
    }

    /** Called every time the scheduler runs while the command is scheduled. */
    public void execute() {
        /** Shows when execute runs and prints the time to the console */
        RobotTelemetry.print("FullCommand: executing: " + getElapsedTime());
    }

    /** Called once the command ends or is interrupted. */
    public void end(boolean interrupted) {

        /**
         * Shows when end runs and prints the time to the console If the command is interrupted it
         * will print that instead
         */
        if (interrupted) {
            RobotTelemetry.print("FullCommand: interrupted: " + getElapsedTime());
        } else {
            RobotTelemetry.print("FullCommand: ended: " + getElapsedTime());
        }
    }

    /** Returns true when the command should end. */
    public boolean isFinished() {
        /** Check if 3 seconds has passed and if so tell the command to end */
        if (getElapsedTime() > 3) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * This is not an inherited method but is a helper method calculate the elapased time since the
     * command started running Commands can have any additional methods needed to help perform thier
     * tasks
     *
     * @return the elapsed time in seconds
     */
    private double getElapsedTime() {
        return Timer.getFPGATimestamp() - startTime;
    }
}

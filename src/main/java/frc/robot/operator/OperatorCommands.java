package frc.robot.operator;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.Robot;
import frc.spectrumLib.util.exceptions.KillRobotException;

/** This class should have any command calls that directly call the Operator */
public class OperatorCommands {
    private static Operator operator = Robot.operator;

    /** Set default command to turn off the rumble */
    public static void setupDefaultCommand() {
        operator.setDefaultCommand(rumble(0, 99999).repeatedly().withName("Operator.default"));
    }

    /** Command that can be used to rumble the Operator controller */
    public static Command rumble(double intensity, double durationSeconds) {
        return operator.rumbleCommand(intensity, durationSeconds);
    }

    // TODO: add manual slide, shoulder, elbow //TODO: review
    public static Command manualElbow() {
        return Robot.elbow.run(() -> Robot.elbow.setPercentOutput(operator.getElbowManual()));
    }

    public static Command cancelCommands() {
        return new InstantCommand(() -> CommandScheduler.getInstance().cancelAll())
                .withName("OperatorCancelAll");
    }

    public static Command killTheRobot() {
        return new InstantCommand(() -> operatorError()).ignoringDisable(true);
    }

    public static void operatorError() {
        throw new KillRobotException("The robot was killed by operator");
    }
}

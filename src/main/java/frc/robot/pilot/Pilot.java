package frc.robot.pilot;

import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.training.commands.FullCommand;
import frc.robot.training.commands.TrainingCommands;
import frc.spectrumLib.Gamepad;

public class Pilot extends Gamepad {
    public PilotConfig config;

    /** Create a new Pilot with the default name and port. */
    public Pilot() {
        super(PilotConfig.name, PilotConfig.port);
        config = new PilotConfig();
    }

    /** Setup the Buttons for telop mode. */
    /*  A, B, X, Y, Left Bumper, Right Bumper = Buttons 1 to 6 in simualation */
    public void setupTeleopButtons() {
        // Prints Once
        gamepad.a()
                .whileTrue(
                        TrainingCommands.printOnceCommand(
                                "A Button", "Print Once: A Button Pressed"));

        // Prints every periodic loop that the button is pressed
        gamepad.b()
                .whileTrue(
                        TrainingCommands.printPeriodicCommand(
                                "B Button", "Print Periodic: B Button Pressed"));

        // Runs the FullComman Training Command, doesn't run while disabled
        gamepad.x().whileTrue(new FullCommand());

        // Prints every periodic loop for 1 second
        Trigger timeoutTrigger = gamepad.y().and(noBumpers());
        timeoutTrigger.whileTrue(
                TrainingCommands.printPeriodicCommand(
                                "Y + No Bumper",
                                "Print Periodic with Timeout: Y Button Pressed and Right Bumper Pressed")
                        .withTimeout(1));

        // Prints one line once and then prints one line every periodic loop
        Trigger sequentialTrigger = gamepad.y().and(leftBumperOnly());
        sequentialTrigger.whileTrue(
                TrainingCommands.sequentialPrintCommand(
                        "Y + Left Bumpers",
                        "Print Instant: Y Button Pressed and No Bumpers Pressed",
                        "Print Periodic: Y Button Pressed and No Bumpers Pressed"));

        // Prints two lines every periodic loop
        Trigger parrellelTrigger = gamepad.y().and(rightBumperOnly());
        parrellelTrigger.whileTrue(
                TrainingCommands.parallelPrintCommand(
                        "Y + Right Bumper",
                        "Print Periodic 1: Y Button Pressed",
                        "Print Periodic 2: and Left Bumper Pressed"));
    };

    /** Setup the Buttons for Disabled mode. */
    public void setupDisabledButtons() {
        // This is just for training, most robots will have different buttons during disabled
        setupTeleopButtons();
    };

    /** Setup the Buttons for Test mode. */
    public void setupTestButtons() {
        // This is just for training, robots may have different buttons during test
        setupTeleopButtons();
    };
}

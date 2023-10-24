package frc.robot.pilot;

import frc.robot.pilot.commands.PilotCommands;
import frc.robot.training.commands.FullCommand;
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
        gamepad.a().whileTrue(PilotCommands.printOnceCommand());
        gamepad.a().whileTrue(PilotCommands.solidPurpleLED());

        // Prints every periodic loop that the button is pressed
        // Change this to .onTrue() to continue printing even when the button is released
        gamepad.b().whileTrue(PilotCommands.periodicCommand());
        gamepad.b().whileTrue(PilotCommands.strobeOrangeLED());

        // Runs the FullComman Training Command, doesn't run while disabled
        gamepad.x().whileTrue(new FullCommand());

        // Prints every periodic loop for 1 second
        gamepad.y().and(noBumpers()).whileTrue(PilotCommands.periodicTimeoutCommand());

        // Prints one line once and then prints one line every periodic loop
        gamepad.y().and(leftBumperOnly()).whileTrue(PilotCommands.sequentialGroupCommand());

        // Prints two lines every periodic loop
        gamepad.y().and(rightBumperOnly()).whileTrue(PilotCommands.parrellelGroupCommand());
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

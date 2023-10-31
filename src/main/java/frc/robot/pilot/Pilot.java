package frc.robot.pilot;

import frc.robot.RobotCommands;
import frc.robot.leds.commands.LEDsCommands;
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
        gamepad.a().whileTrue(TrainingCommands.printOnceCommand());
        gamepad.a().whileTrue(LEDsCommands.solidPurpleLED());

        // Prints every periodic loop that the button is pressed
        // Change this to .onTrue() to continue printing even when the button is released
        gamepad.b().whileTrue(TrainingCommands.periodicCommand());
        gamepad.b().whileTrue(LEDsCommands.strobeOrangeLED());

        // Runs the FullComman Training Command, doesn't run while disabled
        gamepad.x().whileTrue(new FullCommand());

        // Prints every periodic loop for 1 second
        gamepad.y().and(noBumpers()).whileTrue(TrainingCommands.periodicTimeoutCommand());

        // Prints one line once and then prints one line every periodic loop
        gamepad.y().and(leftBumperOnly()).whileTrue(TrainingCommands.sequentialGroupCommand());

        // Prints two lines every periodic loop
        gamepad.y().and(rightBumperOnly()).whileTrue(TrainingCommands.parellelGroupCommand());

        leftXTrigger(ThresholdType.GREATER_THAN, 0).whileTrue(RobotCommands.PrintAndBreathLED());
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

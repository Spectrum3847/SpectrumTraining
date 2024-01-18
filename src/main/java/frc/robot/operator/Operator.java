package frc.robot.operator;

import frc.robot.RobotCommands;
import frc.robot.RobotTelemetry;
import frc.robot.leds.LEDsCommands;
import frc.robot.swerve.commands.SwerveCommands;
import frc.robot.training.commands.TrainingCommands;
import frc.spectrumLib.Gamepad;

public class Operator extends Gamepad {
    public class OperatorConfig {
        public static final String name = "Operator";
        public static final int port = 1;

        public static final double slideModifier = 1.0;
        public static final double shoulderModifier = 0.3;
        public static final double elbowModifier = 0.3;
    }

    public OperatorConfig config;

    /** Create a new Operator with the default name and port. */
    public Operator() {
        super(OperatorConfig.name, OperatorConfig.port);
        config = new OperatorConfig();

        RobotTelemetry.print("Operator Subsystem Initialized: ");
    }

    /** Setup the Buttons for telop mode. */
    /*  A, B, X, Y, Left Bumper, Right Bumper = Buttons 1 to 6 in simualation */
    public void setupTeleopButtons() {
        // Prints Once
        controller.a().whileTrue(TrainingCommands.printOnceCommand());
        controller.a().whileTrue(LEDsCommands.solidPurpleLED());

        // Prints every periodic loop that the button is pressed
        // Change this to .onTrue() to continue printing even when the button is released
        controller.b().whileTrue(TrainingCommands.periodicCommand());
        controller.b().whileTrue(LEDsCommands.strobeOrangeLED());

        // Runs the FullComman Training Command, doesn't run while disabled
        controller.x().whileTrue(TrainingCommands.fullCommand());

        // Prints every periodic loop for 1 second
        controller.y().and(noBumpers()).whileTrue(TrainingCommands.periodicTimeoutCommand());

        // Prints one line once and then prints one line every periodic loop
        controller.y().and(leftBumperOnly()).whileTrue(TrainingCommands.sequentialGroupCommand());

        // Prints two lines every periodic loop
        controller.y().and(rightBumperOnly()).whileTrue(TrainingCommands.parellelGroupCommand());

        leftXTrigger(ThresholdType.GREATER_THAN, 0).whileTrue(RobotCommands.PrintAndBreathLED());

        controller
                .povUp()
                .and(leftBumperOnly())
                .whileTrue(rumbleCommand(SwerveCommands.reorient(0)));
        controller
                .povLeft()
                .and(leftBumperOnly())
                .whileTrue(rumbleCommand(SwerveCommands.reorient(90)));
        controller
                .povDown()
                .and(leftBumperOnly())
                .whileTrue(rumbleCommand(SwerveCommands.reorient(180)));
        controller
                .povRight()
                .and(leftBumperOnly())
                .whileTrue(rumbleCommand(SwerveCommands.reorient(270)));
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

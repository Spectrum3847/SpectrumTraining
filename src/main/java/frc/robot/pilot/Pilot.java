package frc.robot.pilot;

import frc.robot.RobotCommands;
import frc.robot.RobotTelemetry;
import frc.robot.leds.commands.LEDsCommands;
import frc.robot.training.commands.TrainingCommands;
import frc.spectrumLib.Gamepad;
import frc.spectrumLib.util.ExpCurve;

public class Pilot extends Gamepad {
    public PilotConfig config;
    private boolean isSlowMode = false;
    private boolean isFieldOriented = false;
    private ExpCurve LeftStickCurve;
    private ExpCurve TriggersCurve;

    /** Create a new Pilot with the default name and port. */
    public Pilot() {
        super(PilotConfig.name, PilotConfig.port);
        config = new PilotConfig();

        // Curve objects that we use to configure the controller axis ojbects
        LeftStickCurve =
                new ExpCurve(
                        config.leftStickExp, 0, config.leftStickScalar, config.leftStickDeadzone);
        TriggersCurve =
                new ExpCurve(config.triggersExp, 0, config.triggersScalar, config.triggersDeadzone);

        RobotTelemetry.print("Pilot Subsystem Initialized: ");
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

    public void setSlowMode(boolean isSlowMode) {
        this.isSlowMode = isSlowMode;
    }

    public void setFieldOriented(boolean isFieldOriented) {
        this.isFieldOriented = isFieldOriented;
    }

    public boolean getFieldOriented() {
        return isFieldOriented;
    }

    // Positive is forward, up on the left stick is positive
    // Applies Expontial Curve, Deadzone, and Slow Mode toggle
    public double getDriveFwdPositive() {
        double fwdPositive = LeftStickCurve.calculate(-1 * controller.getLeftY());
        if (isSlowMode) {
            fwdPositive *= Math.abs(PilotConfig.slowModeScalor);
        }
        return fwdPositive;
    }

    // Positive is left, left on the left stick is positive
    // Applies Expontial Curve, Deadzone, and Slow Mode toggle
    public double getDriveLeftPositive() {
        double leftPositive = LeftStickCurve.calculate(controller.getLeftX());
        if (isSlowMode) {
            leftPositive *= Math.abs(PilotConfig.slowModeScalor);
        }
        return leftPositive;
    }

    // Positive is counter-clockwise, left Trigger is positive
    // Applies Expontial Curve, Deadzone, and Slow Mode toggle
    public double getDriveCCWPositive() {
        double ccwPositive = TriggersCurve.calculate(getTwist());
        if (isSlowMode) {
            ccwPositive *= Math.abs(PilotConfig.slowModeScalor);
        }
        return ccwPositive;
    }
}

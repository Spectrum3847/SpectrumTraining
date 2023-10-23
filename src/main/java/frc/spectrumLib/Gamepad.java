package frc.spectrumLib;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.RobotTelemetry;

public abstract class Gamepad extends SubsystemBase {

    public boolean configured = false;
    private boolean printed = false;
    public CommandXboxController gamepad;

    /**
     * Creates a new Gamepad.
     *
     * @param port The port the gamepad is plugged into
     * @param name The name of the gamepad
     */
    public Gamepad(String name, int port) {
        gamepad = new CommandXboxController(port);
    }

    @Override
    public void periodic() {
        configure();
    }

    // Configure the driver controller
    public void configure() {
        // Detect whether the xbox controller has been plugged in after start-up
        if (!configured) {
            boolean isConnected = gamepad.getHID().isConnected();
            if (!isConnected) {
                if (!printed) {
                    RobotTelemetry.print("##" + getName() + ": GAMEPAD NOT CONNECTED ##");
                    printed = true;
                }
                return;
            }

            // Configure button bindings once the driver controller is connected
            if (DriverStation.isTest()) {
                setupTestButtons();
            } else if (DriverStation.isDisabled()) {
                setupDisabledButtons();
            } else {
                setupTeleopButtons();
            }
            configured = true;

            RobotTelemetry.print("##" + getName() + ": gamepad is connected ##");
        }
    }

    // Reset the controller configure, should be used with
    // CommandScheduler.getInstance.clearButtons()
    // to reset buttons
    public void resetConfig() {
        configured = false;
        configure();
    }

    /** Setup modifier bumper and trigger buttons */
    public Trigger noBumpers() {
        return gamepad.rightBumper().negate().and(gamepad.leftBumper().negate());
    }

    public Trigger leftBumperOnly() {
        return gamepad.leftBumper().and(gamepad.rightBumper().negate());
    }

    public Trigger rightBumperOnly() {
        return gamepad.rightBumper().and(gamepad.leftBumper().negate());
    }

    public Trigger bothBumpers() {
        return gamepad.rightBumper().and(gamepad.leftBumper());
    }

    public Trigger noTriggers() {
        return gamepad.leftTrigger(0).negate().and(gamepad.rightTrigger(0).negate());
    }

    public Trigger leftTriggerOnly() {
        return gamepad.leftTrigger(0).and(gamepad.rightTrigger(0).negate());
    }

    public Trigger rightTriggerOnly() {
        return gamepad.rightTrigger(0).and(gamepad.leftTrigger(0).negate());
    }

    public Trigger bothTriggers() {
        return gamepad.leftTrigger(0).and(gamepad.rightTrigger(0));
    }

    public Trigger leftYTrigger(ThresholdType t, double threshold) {
        return axisTrigger(t, threshold, gamepad.getLeftY());
    }

    public Trigger leftXTrigger(ThresholdType t, double threshold) {
        return axisTrigger(t, threshold, gamepad.getLeftX());
    }

    public Trigger rightYTrigger(ThresholdType t, double threshold) {
        return axisTrigger(t, threshold, gamepad.getRightY());
    }

    public Trigger rightXTrigger(ThresholdType t, double threshold) {
        return axisTrigger(t, threshold, gamepad.getRightX());
    }

    public Trigger axisTrigger(ThresholdType t, double threshold, double axis) {
        return new Trigger(
                () -> {
                    double value = axis;
                    switch (t) {
                        case GREATER_THAN:
                            return value > threshold;
                        case LESS_THAN:
                            return value < threshold;
                        case ABS_GREATER_THAN: // Also called Deadband
                            return Math.abs(value) > threshold;
                        default:
                            return false;
                    }
                });
    }

    public static enum ThresholdType {
        GREATER_THAN,
        LESS_THAN,
        ABS_GREATER_THAN;
    }

    private void rumble(double leftIntensity, double rightIntensity) {
        gamepad.getHID().setRumble(RumbleType.kLeftRumble, leftIntensity);
        gamepad.getHID().setRumble(RumbleType.kRightRumble, rightIntensity);
    }

    /** Command that can be used to rumble the pilot controller */
    public Command rumble(
            String name, double leftIntensity, double rightIntensity, double durationSeconds) {
        return new RunCommand(() -> rumble(leftIntensity, rightIntensity), this)
                .withTimeout(durationSeconds)
                .ignoringDisable(true)
                .withName(name);
    }

    public Command rumble(String name, double intensity, double durationSeconds) {
        return rumble(name, intensity, intensity, durationSeconds);
    }

    public abstract void setupTeleopButtons();

    public abstract void setupDisabledButtons();

    public abstract void setupTestButtons();
}

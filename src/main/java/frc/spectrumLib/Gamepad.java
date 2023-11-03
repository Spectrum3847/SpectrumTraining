package frc.spectrumLib;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.RobotTelemetry;
import java.util.function.DoubleSupplier;

public abstract class Gamepad extends SubsystemBase {

    public boolean configured = false;
    private boolean printed = false;
    public CommandXboxController controller;

    /**
     * Creates a new Gamepad.
     *
     * @param port The port the gamepad is plugged into
     * @param name The name of the gamepad
     */
    public Gamepad(String name, int port) {
        controller = new CommandXboxController(port);
    }

    @Override
    public void periodic() {
        configure();
    }

    // Configure the driver controller
    public void configure() {
        // Detect whether the xbox controller has been plugged in after start-up
        if (!configured) {
            boolean isConnected = controller.getHID().isConnected();
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

    public double getTwist() {
        double right = controller.getRightTriggerAxis();
        double left = controller.getLeftTriggerAxis();
        double value = right - left;
        if (controller.getHID().isConnected()) {
            return value;
        }
        return 0;
    }

    /** Setup modifier bumper and trigger buttons */
    public Trigger noBumpers() {
        return controller.rightBumper().negate().and(controller.leftBumper().negate());
    }

    public Trigger leftBumperOnly() {
        return controller.leftBumper().and(controller.rightBumper().negate());
    }

    public Trigger rightBumperOnly() {
        return controller.rightBumper().and(controller.leftBumper().negate());
    }

    public Trigger bothBumpers() {
        return controller.rightBumper().and(controller.leftBumper());
    }

    public Trigger noTriggers() {
        return controller.leftTrigger(0).negate().and(controller.rightTrigger(0).negate());
    }

    public Trigger leftTriggerOnly() {
        return controller.leftTrigger(0).and(controller.rightTrigger(0).negate());
    }

    public Trigger rightTriggerOnly() {
        return controller.rightTrigger(0).and(controller.leftTrigger(0).negate());
    }

    public Trigger bothTriggers() {
        return controller.leftTrigger(0).and(controller.rightTrigger(0));
    }

    public Trigger leftYTrigger(ThresholdType t, double threshold) {
        return axisTrigger(t, threshold, () -> controller.getLeftY());
    }

    public Trigger leftXTrigger(ThresholdType t, double threshold) {
        return axisTrigger(t, threshold, () -> controller.getLeftX());
    }

    public Trigger rightYTrigger(ThresholdType t, double threshold) {
        return axisTrigger(t, threshold, () -> controller.getRightY());
    }

    public Trigger rightXTrigger(ThresholdType t, double threshold) {
        return axisTrigger(t, threshold, () -> controller.getRightX());
    }

    private Trigger axisTrigger(ThresholdType t, double threshold, DoubleSupplier v) {
        return new Trigger(
                () -> {
                    double value = v.getAsDouble();
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
        controller.getHID().setRumble(RumbleType.kLeftRumble, leftIntensity);
        controller.getHID().setRumble(RumbleType.kRightRumble, rightIntensity);
    }

    /** Command that can be used to rumble the pilot controller */
    public Command rumbleCommand(
            double leftIntensity, double rightIntensity, double durationSeconds) {
        return new RunCommand(() -> rumble(leftIntensity, rightIntensity), this)
                .withTimeout(durationSeconds)
                .ignoringDisable(true)
                .withName("Gamepad.Rumble");
    }

    public Command rumbleCommand(double intensity, double durationSeconds) {
        return rumbleCommand(intensity, intensity, durationSeconds);
    }

    public abstract void setupTeleopButtons();

    public abstract void setupDisabledButtons();

    public abstract void setupTestButtons();
}

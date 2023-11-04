package frc.robot.swerve.commands;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.pilot.commands.PilotCommands;
import java.util.function.DoubleSupplier;

public class SwerveCommands {

    public static void setupDefaultCommand() {
        Robot.swerve.setDefaultCommand(PilotCommands.pilotDrive());
    }

    /**
     * Turn the swerve wheels to an X to prevent the robot from moving
     *
     * @return
     */
    public static Command Xbrake() {
        return Xbrake.run().withName("Swerve: Xbrake");
    }

    /**
     * Drive the swerve
     *
     * @param velocityX
     * @param velocityY
     * @param rotationalRate
     * @param isFieldOriented
     * @param isOpenLoop
     * @return Command
     */
    public static Command Drive(
            DoubleSupplier velocityX,
            DoubleSupplier velocityY,
            DoubleSupplier rotationalRate,
            boolean isFieldOriented,
            boolean isOpenLoop) {
        return Drive.run(velocityX, velocityY, rotationalRate, isFieldOriented, isOpenLoop)
                .withName("Swerve.Drive")
                .ignoringDisable(true);
    }

    /**
     * Reset the turn controller and then run the drive command with a angle supplier. This can be
     * used for aiming at a goal or heading locking, etc
     *
     * @param velocityX
     * @param velocityY
     * @param goalAngle
     * @param isFieldOriented
     * @return
     */
    public static Command aimDrive(
            DoubleSupplier velocityX,
            DoubleSupplier velocityY,
            DoubleSupplier goalAngle,
            boolean isFieldOriented,
            boolean isOpenLoop) {
        return resetTurnController()
                .andThen(
                        Drive(
                                velocityX,
                                velocityY,
                                () -> Robot.swerve.calculateRotationController(goalAngle),
                                isFieldOriented,
                                isOpenLoop))
                .withName("Swerve.AimDrive");
    }

    /**
     * Apply a chassis speed to the swerve
     *
     * @param speeds
     * @param isOpenLoop
     * @return
     */
    public static Command ApplyChassisSpeeds(ChassisSpeeds speeds, boolean isOpenLoop) {
        return ApplyChassisSpeeds.run(speeds, isOpenLoop).withName("Swerve.ApplyChassisSpeeds");
    }

    /**
     * Reset the turn controller
     *
     * @return
     */
    public static Command resetTurnController() {
        return Robot.swerve
                .runOnce(() -> Robot.swerve.resetRotationController())
                .withName("ResetTurnController");
    }

    // Swerve Command Options
    // - Drive needs to work with slow mode (this might be done in PilotCommands)
}

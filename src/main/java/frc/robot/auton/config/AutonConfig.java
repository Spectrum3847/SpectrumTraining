package frc.robot.auton.config;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;
import com.pathplanner.lib.util.HolonomicPathFollowerConfig;
import com.pathplanner.lib.util.PIDConstants;
import com.pathplanner.lib.util.ReplanningConfig;
import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.Robot;
import frc.robot.auton.commands.TargetOverride;
import frc.robot.swerve.commands.ApplyChassisSpeeds;

public class AutonConfig {
    // TODO: #1 @EDPendleton24: The PID constants have to be different for Translation and Rotation
    // TODO: Check if required commands work
    // TODO: Check if PID and other constants are correct

    public static final double kTranslationP = 5;
    public static final double kTranslationI = 0.0;
    public static final double kTranslationD = 0.0;
    public static final double kRotationP = 5;
    public static final double kRotationI = 0.0;
    public static final double kRotationD = 0.0;
    public static final double maxModuleSpeed = 4.5;
    public static final double driveBaseRadius = 0.4;

    public static HolonomicPathFollowerConfig AutonPathFollowerConfig =
            new HolonomicPathFollowerConfig( // HolonomicPathFollowerConfig, this should likely
                    // live in your Constants class
                    new PIDConstants(
                            kTranslationP,
                            kTranslationI,
                            kTranslationD), // Translation PID constants
                    new PIDConstants(kRotationP, kRotationI, kRotationD), // Rotation PID constants
                    maxModuleSpeed, // Max module speed, in m/s
                    driveBaseRadius, // Drive base radius in meters. Distance from robot center to
                    // furthest module.
                    new ReplanningConfig() // Default path replanning config. See the API for
                    // the options here
                    );

    public static void ConfigureAutoBuilder() {
        // All other subsystem initialization
        // ...

        // Set the method that will be used to get rotation overrides
        PPHolonomicDriveController.setRotationTargetOverride(
                () -> TargetOverride.getRotationTargetOverride());

        // Configure AutoBuilder last
        AutoBuilder.configureHolonomic(
                Robot.swerve::getPose, // Robot pose supplier
                Robot.swerve::resetPose, // Method to reset odometry (will be called if your auto
                // has a starting pose)
                Robot.swerve::getRobotRelativeSpeeds, // ChassisSpeeds supplier. MUST BE ROBOT
                // RELATIVE
                ApplyChassisSpeeds.robotRelativeOutput(
                        false), // Method that will drive the robot given ROBOT
                // RELATIVE
                // ChassisSpeeds
                AutonConfig.AutonPathFollowerConfig,
                () -> {
                    // Boolean supplier that controls when the path will be mirrored for the red
                    // alliance
                    // This will flip the path being followed to the red side of the field.
                    // THE ORIGIN WILL REMAIN ON THE BLUE SIDE

                    var alliance = DriverStation.getAlliance();
                    if (alliance.isPresent()) {
                        return alliance.get() == DriverStation.Alliance.Red;
                    }
                    return false;
                },
                Robot.swerve // Reference to this subsystem to set requirements
                );
    }
}

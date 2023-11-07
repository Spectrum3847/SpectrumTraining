package frc.robot.auton;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.PathPlannerAuto;
import com.pathplanner.lib.util.HolonomicPathFollowerConfig;
import com.pathplanner.lib.util.PIDConstants;
import com.pathplanner.lib.util.ReplanningConfig;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.RobotTelemetry;
import frc.robot.auton.commands.AutoBalance;
import frc.robot.auton.config.AutonConfig;

public class Auton extends SubsystemBase {
    public static final SendableChooser<Command> autonChooser = new SendableChooser<>();
    private static boolean autoMessagePrinted = true;
    private static double autonStart = 0;

    // A chooser for autonomous commands
    public static void setupSelectors() {
        autonChooser.setDefaultOption("Clean Side 3", new PathPlannerAuto("Clean Side 3"));
        // autonChooser.addOption("Clean3", AutoPaths.CleanSide());
    }

    // Subsystem Documentation:
    // https://docs.wpilib.org/en/stable/docs/software/commandbased/subsystems.html
    public Auton() {
        configureAutoBuilder(); // configures the auto builder
        setupNamedCommands(); // registers named commands
        setupSelectors(); // runs the command to start the chooser for auto on shuffleboard

        RobotTelemetry.print("Auton Subsystem Initialized: ");
    }

    public static void setupNamedCommands() {
        // Register Named Commands
        NamedCommands.registerCommand("autoBalance", new AutoBalance());
    }

    public static void configureAutoBuilder() {
        // Configure the AutoBuilder last
        AutoBuilder.configureHolonomic(
                Robot.swerve::getPose, // Robot pose supplier
                Robot.swerve
                        ::resetPose, // Method to reset odometry (will be called if your auto has a
                // starting pose)
                Robot.swerve
                        ::getRobotRelativeSpeeds, // ChassisSpeeds supplier. MUST BE ROBOT RELATIVE
                Robot.swerve::driveRobotRelative, // Method that will drive the robot given ROBOT
                // RELATIVE ChassisSpeeds
                AutonConfig.AutonPathFollowerConfig,
                Robot.swerve // Reference to this subsystem to set requirements
                );
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public static Command getAutonomousCommand() {
        // return new CharacterizeLauncher(Robot.launcher);
        Command auton = autonChooser.getSelected(); // sees what auto is chosen on shuffleboard
        if (auton != null) {
            return auton; // checks to make sure there is an auto and if there is it runs an auto
        } else {
            return new PrintCommand(
                    "*** AUTON COMMAND IS NULL ***"); // runs if there is no auto chosen, which
            // shouldn't happen because of the default
            // auto set to nothing which still runs
            // something
        }
    }

    /** This method is called in AutonInit */
    public static void startAutonTimer() {
        autonStart = Timer.getFPGATimestamp();
        autoMessagePrinted = false;
    }

    /** Called in RobotPeriodic and displays the duration of the auton command Based on 6328 code */
    public static void printAutoDuration() {
        Command autoCommand = Auton.getAutonomousCommand();
        if (autoCommand != null) {
            if (!autoCommand.isScheduled() && !autoMessagePrinted) {
                if (DriverStation.isAutonomousEnabled()) {
                    RobotTelemetry.print(
                            String.format(
                                    "*** Auton finished in %.2f secs ***",
                                    Timer.getFPGATimestamp() - autonStart));
                } else {
                    RobotTelemetry.print(
                            String.format(
                                    "*** Auton CANCELLED in %.2f secs ***",
                                    Timer.getFPGATimestamp() - autonStart));
                }
                autoMessagePrinted = true;
            }
        }
    }

}

package frc.robot.auton;

import com.pathplanner.lib.commands.PathPlannerAuto;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import frc.robot.Robot;
import frc.robot.RobotTelemetry;
import frc.robot.auton.commands.FollowSinglePath;
import frc.robot.auton.config.AutonConfig;
import frc.robot.swerve.commands.SwerveCommands;
import frc.robot.swerve.commands.SysIdCommands;

public class Auton extends SubsystemBase {
    public static final SendableChooser<Command> autonChooser = new SendableChooser<>();
    private static boolean autoMessagePrinted = true;
    private static double autonStart = 0;

    // A chooser for autonomous commands
    public static void setupSelectors() {
        autonChooser.setDefaultOption("4 Piece Middle", new PathPlannerAuto("4 Piece Middle"));

        // autonChooser.addOption(
        //         "Example Path", FollowSinglePath.getSinglePath("Example Path")); // Runs single
        // Path
        autonChooser.addOption(
                "Example Path", FollowSinglePath.getSinglePath("Example Path")); // Runs full Auto

        autonChooser.addOption("1 Meter", new PathPlannerAuto("1 Meter")); // Runs full Auto
        
        //SysId Configuration Autos
        autonChooser.addOption("SysIdQuasistaticForward", SysIdCommands.sysIdQuasistaticForward());
        autonChooser.addOption("SysIdQuasistaticBackward", SysIdCommands.sysIdQuasistaticBackward());
        autonChooser.addOption("SysIdDynamicForward", SysIdCommands.sysIdDynamicForward());
        autonChooser.addOption("SysIdDynamicBackward", SysIdCommands.sysIdDynamicBackward());
        
        autonChooser.addOption(
                "Test Swerve",
                SwerveCommands.Drive(
                        () -> 0.1,
                        () -> 0,
                        () -> 1,
                        () -> true, // true is field oriented
                        () -> true)); // Runs full Auto

        SmartDashboard.putData("Auto Chooser", autonChooser);
    }

    // Setup the named commands
    public static void setupNamedCommands() {
        // Register Named Commands
        // NamedCommands.registerCommand("autoBalance", new AutoBalance());
        // NamedCommands.registerCommand("alignToSpeaker", new AutoBalance());
    }

    // Subsystem Documentation:
    // https://docs.wpilib.org/en/stable/docs/software/commandbased/subsystems.html
    public Auton() {
        AutonConfig.ConfigureAutoBuilder(); // configures the auto builder
        setupNamedCommands(); // registers named commands
        setupSelectors(); // runs the command to start the chooser for auto on shuffleboard

        RobotTelemetry.print("Auton Subsystem Initialized: ");
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

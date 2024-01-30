package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.auton.Auton;
import frc.robot.auton.commands.TargetOverride;
import frc.robot.intake.Intake;
import frc.robot.intake.IntakeCommands;
import frc.robot.leds.LEDs;
import frc.robot.leds.LEDsCommands;
import frc.robot.operator.Operator;
import frc.robot.operator.OperatorCommands;
import frc.robot.pilot.Pilot;
import frc.robot.pilot.PilotCommands;
import frc.robot.pose.Pose;
import frc.robot.slide.Slide;
import frc.robot.slide.SlideCommands;
import frc.robot.swerve.Swerve;
import frc.robot.swerve.commands.SwerveCommands;
import frc.robot.training.Training;
import frc.robot.trajectories.Trajectories;
import frc.robot.vision.Vision;
import frc.spectrumLib.util.CrashTracker;
import org.littletonrobotics.junction.LoggedRobot;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.NT4Publisher;
import org.littletonrobotics.junction.wpilog.WPILOGWriter;

public class Robot extends LoggedRobot {
    public static RobotConfig config;
    public static RobotTelemetry telemetry;

    /** Create a single static instance of all of your subsystems */
    public static Swerve swerve;

    public static Intake intake;
    public static Slide slide;
    public static LEDs leds;
    public static Pilot pilot;
    public static Operator operator;
    public static Auton auton;
    public static Pose pose;
    public static Vision vision;
    public static Trajectories trajectories;
    public static Training training;

    /**
     * This method cancels all commands and returns subsystems to their default commands and the
     * gamepad configs are reset so that new bindings can be assigned based on mode This method
     * should be called when each mode is intialized
     */
    public static void resetCommandsAndButtons() {
        CommandScheduler.getInstance().cancelAll(); // Disable any currently running commands
        CommandScheduler.getInstance().getActiveButtonLoop().clear();

        // Reset Config for all gamepads and other button bindings
        pilot.resetConfig();
        operator.resetConfig();
    }

    /* ROBOT INIT (Initialization) */
    /** This method is called once when the robot is first powered on. */
    public void robotInit() {
        try {
            RobotTelemetry.print("--- Robot Init Starting ---");

            /** Set up the config */
            config = new RobotConfig();

            /**
             * Intialize the Subsystems of the robot. Subsystems are how we divide up the robot
             * code. Anything with an output that needs to be independently controlled is a
             * subsystem Something that don't have an output are alos subsystems.
             */
            // Intializes position based subsystems
            vision = new Vision();
            System.out.println("Started Vision");
            swerve = new Swerve();
            System.out.println("Started Swerve");
            pose = new Pose();
            System.out.println("Started Pose");
            trajectories = new Trajectories();
            System.out.println("Started Trajectories");

            // Starts subsystems
            intake = new Intake(config.intakeAttached);
            System.out.println("Started Intake");
            slide = new Slide(true);
            System.out.println("Started Slide");

            // Starts gamepads and LEDs
            pilot = new Pilot();
            operator = new Operator();
            System.out.println("Started Gamepads");
            leds = new LEDs();
            System.out.println("Started LEDs");

            /** Intialize Telemetry and Auton */
            auton = new Auton();
            System.out.println("Started Auton");
            telemetry = new RobotTelemetry();
            System.out.println("Started Telemetry");
            training = new Training();
            System.out.println("Started Training");
            advantageKitInit();

            /**
             * Set Default Commands this method should exist for each subsystem that has default
             * command these must be done after all the subsystems are intialized
             */
            // TrainingCommands.setupDefaultCommand();
            SwerveCommands.setupDefaultCommand();
            IntakeCommands.setupDefaultCommand();
            SlideCommands.setupDefaultCommand();
            LEDsCommands.setupDefaultCommand();
            PilotCommands.setupDefaultCommand();
            OperatorCommands.setupDefaultCommand();

            RobotTelemetry.print("--- Robot Init Complete ---");
        } catch (Throwable t) {
            // intercept error and log it
            CrashTracker.logThrowableCrash(t);
            throw t;
        }
    }

    /* ROBOT PERIODIC  */
    /**
     * This method is called periodically the entire time the robot is running. Periodic methods are
     * called every 20 ms (50 times per second) by default Since the robot software is always
     * looping you shouldn't pause the execution of the robot code This ensures that new values are
     * updated from the gamepads and sent to the motors
     */
    public void robotPeriodic() {
        try {
            /**
             * Runs the Scheduler. This is responsible for polling buttons, adding newly-scheduled
             * commands, running already-scheduled commands, removing finished or interrupted
             * commands, and running subsystem periodic() methods. This must be called from the
             * robot's periodic block in order for anything in the Command-based framework to work.
             */
            CommandScheduler.getInstance().run();
        } catch (Throwable t) {
            // intercept error and log it
            CrashTracker.logThrowableCrash(t);
            throw t;
        }
    }

    /* DISABLED MODE */
    /** This mode is run when the robot is disabled All motor/accuator outputs are turned off */

    /** This method is called once when disabled starts */
    public void disabledInit() {
        RobotTelemetry.print("### Disabled Init Starting ### ");

        resetCommandsAndButtons();

        RobotTelemetry.print("### Disabled Init Complete ### ");
    }

    /** This method is called periodically while disabled. */
    public void disabledPeriodic() {}

    /** This method is called once when disabled exits */
    public void disabledExit() {
        RobotTelemetry.print("### Disabled Exit### ");
    }

    /* AUTONOMOUS MODE (AUTO) */
    /**
     * This mode is run when the DriverStation Software is set to autonomous and enabled. In this
     * mode the robot is not able to read values from the gamepads
     */

    /** This method is called once when autonomous starts */
    public void autonomousInit() {
        resetCommandsAndButtons();
        Command autonCommand = Auton.getAutonomousCommand();
        try {
            RobotTelemetry.print("@@@ Auton Init Starting @@@ ");
            TargetOverride.stopTracking();
            autonCommand.schedule();
            Auton.startAutonTimer();
            RobotTelemetry.print("@@@ Auton Init Complete @@@ ");
        } catch (Throwable t) {
            // intercept error and log it
            CrashTracker.logThrowableCrash(t);
            throw t;
        }
    }

    /** This method is called periodically during autonomous. */
    public void autonomousPeriodic() {}

    /** This method is called once when autonomous exits */
    public void autonomousExit() {
        RobotTelemetry.print("@@@ Auton Exit @@@ ");
    }

    /* TELEOP MODE */
    /**
     * This mode is run when the DriverStation Software is set to teleop and enabled. In this mode
     * the robot is fully enabled and can move it's outputs and read values from the gamepads
     */

    /** This method is called once when teleop starts */
    public void teleopInit() {
        try {
            RobotTelemetry.print("!!! Teleop Init Starting !!! ");
            resetCommandsAndButtons();

            RobotTelemetry.print("!!! Teleop Init Complete !!! ");
        } catch (Throwable t) {
            // intercept error and log it
            CrashTracker.logThrowableCrash(t);
            throw t;
        }
    }

    /** This method is called periodically during operator control. */
    public void teleopPeriodic() {}

    /** This method is called once when teleop exits */
    public void teleopExit() {
        RobotTelemetry.print("!!! Teleop Exit !!! ");
    }

    /* TEST MODE */
    /**
     * This mode is run when the DriverStation Software is set to test and enabled. In this mode the
     * is fully enabled and can move it's outputs and read values from the gamepads. This mode is
     * never enabled by the competition field It can be used to test specific features or modes of
     * the robot
     */

    /** This method is called once when test mode starts */
    public void testInit() {
        try {
            RobotTelemetry.print("~~~ Test Init Starting ~~~ ");
            resetCommandsAndButtons();

            RobotTelemetry.print("~~~ Test Init Complete ~~~ ");
        } catch (Throwable t) {
            // intercept error and log it
            CrashTracker.logThrowableCrash(t);
            throw t;
        }
    }

    /** This method is called periodically during test. */
    public void testPeriodic() {}

    /** This method is called once when the robot exits test mode */
    public void testExit() {
        RobotTelemetry.print("~~~ Test Exit ~~~ ");
    }

    /* SIMULATION MODE */
    /**
     * This mode is run when the software is running in simulation and not on an actual robot. This
     * mode is never enabled by the competition field
     */

    /** This method is called once when a simulation starts */
    public void simulationInit() {
        RobotTelemetry.print("$$$ Simulation Init Starting $$$ ");

        RobotTelemetry.print("$$$ Simulation Init Complete $$$ ");
    }

    /** This method is called periodically during simulation. */
    public void simulationPeriodic() {}

    /** This method is called once at the end of RobotInit to begin logging */
    public void advantageKitInit() {
        // Set up data receivers & replay source
        switch (Robot.config.getRobotType()) {
            case SIM:
                // Running a physics simulator, log to NT
                Logger.addDataReceiver(new NT4Publisher());
                break;

            default:
                // Running on a real robot, log to a USB stick
                Logger.addDataReceiver(new WPILOGWriter("/U"));
                Logger.addDataReceiver(new NT4Publisher());
                break;
        }

        // Start AdvantageKit logger
        Logger.start();
    }
}

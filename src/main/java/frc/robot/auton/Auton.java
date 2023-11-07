package frc.robot.auton;

import com.pathplanner.lib.auto.NamedCommands;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotTelemetry;
import frc.robot.auton.commands.AutoBalance;

public class Auton extends SubsystemBase {
    public static final SendableChooser<Command> autonChooser = new SendableChooser<>();

    // Subsystem Documentation:
    // https://docs.wpilib.org/en/stable/docs/software/commandbased/subsystems.html
    public Auton() {
        setupNamedCommands();

        RobotTelemetry.print("Auton Subsystem Initialized: ");
    }

    public static void setupNamedCommands() {
        // Register Named Commands
        NamedCommands.registerCommand("autoBalance", new AutoBalance());
    }

    // A chooser for autonomous commands
    public static void setupSelectors() {
        // autonChooser.setDefaultOption("Balance w/ Mobility (1 Piece)", AutoPaths.OverCharge());
        // autonChooser.addOption("Clean3", AutoPaths.CleanSide());
    }
}

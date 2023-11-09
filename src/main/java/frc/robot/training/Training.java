package frc.robot.training;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotTelemetry;

public class Training extends SubsystemBase {

    public TrainingConfig config;

    /**
     * This is the constructor for the Training subsystem. This subsystem is used to test and train
     * new students. It is not used in competition.
     */
    // Subsystem Documentation:
    // https://docs.wpilib.org/en/stable/docs/software/commandbased/subsystems.html
    public Training() {
        config = new TrainingConfig();

        RobotTelemetry.print("Training Subsystem Initialized: ");
    }
}

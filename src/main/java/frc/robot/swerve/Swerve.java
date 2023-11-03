package frc.robot.swerve;

import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Robot;
import frc.robot.swerve.configs.MUSICDISC2023;
import frc.robot.swerve.configs.NOTEBLOCK2023;
import frc.spectrumLib.swerve.SwerveDrivetrain;
import frc.spectrumLib.swerve.config.SwerveConfig;

public class Swerve implements Subsystem {
    SwerveConfig config;
    SwerveDrivetrain drivetrain;

    public Swerve() {
        switch (Robot.config.getRobotType()) {
            case NOTEBLOCK:
                config = NOTEBLOCK2023.config;
                break;
            case MUSICDISC:
                config = MUSICDISC2023.config;
                break;
            default:
                config = NOTEBLOCK2023.config;
                break;
        }
        drivetrain = new SwerveDrivetrain(config);
    }
}

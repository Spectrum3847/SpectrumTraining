package frc.robot.swerve.commands;

import edu.wpi.first.wpilibj2.command.Command;
import java.util.function.DoubleSupplier;

public class SwerveCommands {

    public Command Xbrake() {
        return Xbrake.run().withName("Swerve: Xbrake");
    }

    public Command Drive(
            DoubleSupplier velocityX, DoubleSupplier velocityY, DoubleSupplier rotationalRate) {
        return Drive.run(velocityX, velocityY, rotationalRate).withName("Swerve: Drive");
    }
}

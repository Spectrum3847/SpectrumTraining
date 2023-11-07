// Created by Spectrum3847
package frc.robot.auton.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;

public class AutoBalance extends Command {
    public double currentAngle = 100;
    private double currentRate = 100;
    private double angleOffset;
    private boolean shouldDrive;
    Command driveCommand;

    public AutoBalance() {
        addRequirements(Robot.swerve);
    }

    @Override
    public void initialize() {}

    @Override
    public void execute() {}

    private double driveSpeed() {
        return 0;
    }

    private double currentAngle() {
        return 0;
    }

    @Override
    public void end(boolean interrupted) {}

    @Override
    public boolean isFinished() {
        return false;
    }
}

package frc.robot.auton.commands;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.Robot;
import frc.robot.vision.VisionConfig;
import java.util.Optional;

public class TargetOverride {
    public static boolean trackNote = false;
    public static boolean trackSpeaker = false;
    private static final String m_limelightNote = VisionConfig.DETECT_LL;
    private static final String m_limelightSpeaker = VisionConfig.DEFAULT_LL;

    public static Optional<Rotation2d> getRotationTargetOverride() {
        // Some condition that should decide if we want to override rotation
        if (trackNote) {
            // Return an optional containing the rotation override (this should be a field relative
            // rotation)
            return Optional.of(Rotation2d.fromDegrees(getDegreesNote()));
        } else if (trackSpeaker) {
            return Optional.of(Rotation2d.fromDegrees(getDegreesSpeaker()));
        } else {
            // return an empty optional when we don't want to override the path's rotation
            return Optional.empty();
        }
    }

    public static Command trackNote() {
        return new InstantCommand(
                () -> {
                    trackSpeaker = false;
                    trackNote = true;
                    System.out.println("TrackNote Started");
                },
                Robot.vision);
    }

    public static Command trackSpeaker() {
        return new InstantCommand(
                () -> {
                    trackNote = false;
                    trackSpeaker = true;
                    System.out.println("TrackSpeaker Started");
                },
                Robot.vision);
    }

    public static Command stopTracking() {
        return new InstantCommand(
                () -> {
                    trackNote = false;
                    trackSpeaker = false;
                    System.out.println("Tracking Stopped");
                },
                Robot.vision);
    }

    public static double getDegreesNote() {
        return Robot.swerve.getRotation().getDegrees()
                - Robot.vision.getHorizontalOffset(m_limelightNote);
    }

    public static double getDegreesSpeaker() {
        return Robot.swerve.getRotation().getDegrees()
                - Robot.vision.getHorizontalOffset(m_limelightSpeaker);
    }
}

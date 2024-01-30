package frc.robot.auton.commands;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.vision.VisionConfig;

import java.util.Optional;

public class TargetOverride {
    public static boolean trackNote = false;
    public static boolean trackSpeaker = false;
    private static final String m_limelightNote = VisionConfig.DETECT_LL;
    private static final String m_limelightSpeaker = VisionConfig.DEFAULT_LL;
    
    public Optional<Rotation2d> getRotationTargetOverride() {
        // Some condition that should decide if we want to override rotation
        if (trackNote) {
            // Return an optional containing the rotation override (this should be a field relative
            // rotation)
            return Optional.of(Rotation2d.fromDegrees(Robot.vision.getHorizontalOffset(m_limelightNote) + Robot.swerve.getRotation().getDegrees()));
        }
        else if (trackSpeaker) {
            return Optional.of(Rotation2d.fromDegrees(Robot.vision.getHorizontalOffset(m_limelightSpeaker) + Robot.swerve.getRotation().getDegrees()));
        }
        else {
            // return an empty optional when we don't want to override the path's rotation
            return Optional.empty();
        }
    }

    public static Command trackNote() {
        trackNote = true;
        return null;
    }

    public static Command trackSpeaker() {
        trackSpeaker = true;
        return null;
    }

    public static Command stopTracking() {
        trackNote = false;
        trackSpeaker = false;
        return null;
    }
}

// package frc.robot.auton.commands;

// import edu.wpi.first.math.geometry.Rotation2d;
// import java.util.Optional;

// public class TargetOverride {
//     public Optional<Rotation2d> getRotationTargetOverride() {
//         // Some condition that should decide if we want to override rotation
//         if (Limelight.hasGamePieceTarget()) {
//             // Return an optional containing the rotation override (this should be a field
// relative
//             // rotation)
//             return Optional.of(Limelight.getRobotToGamePieceRotation());
//         } else {
//             // return an empty optional when we don't want to override the path's rotation
//             return Optional.empty();
//         }
//     }
// }

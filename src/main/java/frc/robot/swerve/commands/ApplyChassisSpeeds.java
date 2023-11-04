package frc.robot.swerve.commands;

import com.ctre.phoenix6.StatusCode;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.spectrumLib.swerve.SwerveModule;
import frc.spectrumLib.swerve.SwerveRequest;

/** Accepts a generic ChassisSpeeds to apply to the drivetrain. */
public class ApplyChassisSpeeds implements SwerveRequest {

    public static Command run(ChassisSpeeds speeds, boolean isOpenLoop) {
        return Robot.swerve.applyRequest(
                () -> new ApplyChassisSpeeds().withSpeeds(speeds).withIsOpenLoop(isOpenLoop));
    }

    /** The chassis speeds to apply to the drivetrain. */
    public ChassisSpeeds Speeds = new ChassisSpeeds();
    /** The center of rotation to rotate around. */
    public Translation2d CenterOfRotation = new Translation2d(0, 0);
    /** True to use open-loop control while stopped. */
    public boolean IsOpenLoop = false;

    public StatusCode apply(
            SwerveControlRequestParameters parameters, SwerveModule... modulesToApply) {
        var states = parameters.kinematics.toSwerveModuleStates(Speeds, CenterOfRotation);
        for (int i = 0; i < modulesToApply.length; ++i) {
            modulesToApply[i].apply(states[i], IsOpenLoop);
        }

        return StatusCode.OK;
    }

    public ApplyChassisSpeeds withSpeeds(ChassisSpeeds speeds) {
        this.Speeds = speeds;
        return this;
    }

    public ApplyChassisSpeeds withCenterOfRotation(Translation2d centerOfRotation) {
        this.CenterOfRotation = centerOfRotation;
        return this;
    }

    public ApplyChassisSpeeds withIsOpenLoop(boolean isOpenLoop) {
        this.IsOpenLoop = isOpenLoop;
        return this;
    }
}

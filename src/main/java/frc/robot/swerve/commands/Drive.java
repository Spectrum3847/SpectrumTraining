package frc.robot.swerve.commands;

import com.ctre.phoenix6.StatusCode;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.spectrumLib.swerve.SwerveModule;
import frc.spectrumLib.swerve.SwerveRequest;
import java.util.function.DoubleSupplier;

/**
 * Drives the swerve drivetrain in a field-centric manner.
 *
 * <p>When users use this request, they specify the direction the robot should travel oriented
 * against the field, and the rate at which their robot should rotate about the center of the robot.
 *
 * <p>An example scenario is that the robot is oriented to the east, the VelocityX is +5 m/s,
 * VelocityY is 0 m/s, and RotationRate is 0.5 rad/s. In this scenario, the robot would drive
 * northward at 5 m/s and turn counterclockwise at 0.5 rad/s.
 */
public class Drive implements SwerveRequest {

    public static Command run() {
        return Robot.swerve.applyRequest(() -> new Drive()).withName("Drive");
    }

    public static Command run(
            DoubleSupplier velocityX, DoubleSupplier velocityY, DoubleSupplier rotationalRate) {
        return Robot.swerve
                .applyRequest(
                        () ->
                                new Drive()
                                        .withVelocityX(velocityX.getAsDouble())
                                        .withVelocityY(velocityY.getAsDouble())
                                        .withRotationalRate(rotationalRate.getAsDouble()))
                .withName("Drive");
    }

    /**
     * The velocity in the X direction. X is defined as forward according to WPILib convention, so
     * this determines how fast to travel forward.
     */
    public double VelocityX = 0;
    /**
     * The velocity in the Y direction. Y is defined as to the left according to WPILib convention,
     * so this determines how fast to travel to the left.
     */
    public double VelocityY = 0;
    /**
     * The angular rate to rotate at. Angular rate is defined as counterclockwise positive, so this
     * determines how fast to turn counterclockwise.
     *
     * <p>This is in radians per second
     */
    public double RotationalRate = 0;
    /** The allowable deadband of the request. */
    public double Deadband = 0;
    /** Rotational deadband of the request */
    public double RotationalDeadband = 0;

    /** True to use open-loop control when driving. */
    public boolean IsOpenLoop = false;

    /** The last applied state in case we don't have anything to drive */
    protected SwerveModuleState[] m_lastAppliedState = null;

    public StatusCode apply(
            SwerveControlRequestParameters parameters, SwerveModule... modulesToApply) {
        double toApplyX = VelocityX;
        double toApplyY = VelocityY;
        double toApplyOmega = RotationalRate;
        if (Math.sqrt(toApplyX * toApplyX + toApplyY * toApplyY) < Deadband) {
            toApplyX = 0;
            toApplyY = 0;
        }
        if (Math.abs(toApplyOmega) < RotationalDeadband) toApplyOmega = 0;

        ChassisSpeeds speeds =
                ChassisSpeeds.discretize(
                        ChassisSpeeds.fromFieldRelativeSpeeds(
                                toApplyX,
                                toApplyY,
                                toApplyOmega,
                                parameters.currentPose.getRotation()),
                        parameters.updatePeriod);

        var states = parameters.kinematics.toSwerveModuleStates(speeds, new Translation2d());

        for (int i = 0; i < modulesToApply.length; ++i) {
            modulesToApply[i].apply(states[i], IsOpenLoop);
        }

        return StatusCode.OK;
    }

    public Drive withVelocityX(double velocityX) {
        this.VelocityX = velocityX;
        return this;
    }

    public Drive withVelocityY(double velocityY) {
        this.VelocityY = velocityY;
        return this;
    }

    public Drive withRotationalRate(double rotationalRate) {
        this.RotationalRate = rotationalRate;
        return this;
    }

    public Drive withDeadband(double deadband) {
        this.Deadband = deadband;
        return this;
    }

    public Drive withRotationalDeadband(double RotationalDeadband) {
        this.RotationalDeadband = RotationalDeadband;
        return this;
    }

    public Drive withIsOpenLoop(boolean isOpenLoop) {
        this.IsOpenLoop = isOpenLoop;
        return this;
    }
}

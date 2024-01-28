package frc.robot.swerve.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.Robot;
import frc.robot.vision.VisionConfig;
import java.util.function.DoubleSupplier;

public class TurnToNote extends PIDCommand {

    private static final double kp = 0.06;
    private static final double tolerance = 0.01;
    private static final double maxOutput = Robot.swerve.config.maxVelocity * 0.5;
    private static final double error = 0.3;
    Command driveCommand;
    DoubleSupplier fwdPositiveSupplier;
    private static double out = 0;
    private int pipelineIndex = VisionConfig.noteDetectorPipeline;
    private double heading = Integer.MIN_VALUE;
    private static final String m_limelight = VisionConfig.DETECT_LL;

    /**
     * Creates a new AlignToVisionTarget command that aligns to a vision target (apriltag,
     * retroreflective tape, detector target) on the Field Oriented X-axis.
     *
     * @param limelight {@link VisionConfig}
     * @param fwdPositiveSupplier
     * @param offset
     * @param pipeline
     */
    public TurnToNote() {
        super(
                // The controller that the command will use
                new PIDController(kp, 0, 0),
                // This should return the measurement
                () -> getHorizontalOffset(),
                // This should return the setpoint (can also be a constant)
                () -> 0,
                // This uses the output
                output -> setOutput(output),
                Robot.swerve);

        this.getController().setTolerance(tolerance);
        driveCommand =
                SwerveCommands.Drive(
                        () -> 0, // Allows pilot to drive fwd and rev
                        () -> 0, // Moves us center to the tag
                        () -> getOutput(), // Aligns to grid
                        () -> getFieldRelative(), // full velocity
                        () -> true); // Field relative is true
        // Use addRequirements() here to declare subsystem dependencies.
        // Configure additional PID options by calling `getController` here.
        this.setName("AlignToConeOBject");
    }

    public double getSteering() {
        // if customizable heading is set, rotate to that heading
        if (heading != Integer.MIN_VALUE) {
            return Robot.swerve.calculateRotationController(() -> heading);
        }

        // dont set rotation on detector pipelines
        if (pipelineIndex > 2) {
            return 0;
        }
        return Robot.swerve.calculateRotationController(() -> Math.PI);
    }

    public boolean getFieldRelative() {
        // We go to RobotPOV to drive straight at the target
        return false;
    }

    public static double getOutput() {
        return out;
    }

    public static void setOutput(double output) {
        out = output;
        if (Math.abs(out) > 1) {
            out = 1 * Math.signum(out);
        }

        out = out * maxOutput;
    }

    // Returns the measurement from the Limelight
    private static double getHorizontalOffset() {
        return Robot.vision.getHorizontalOffset(m_limelight);
    }

    @Override
    public void initialize() {
        super.initialize();
        out = 0;
        // getLedCommand(tagID).initialize();
        Robot.swerve.resetRotationController();
        driveCommand.initialize();
        Robot.vision.setLimelightPipeline(m_limelight, pipelineIndex);
    }

    @Override
    public void execute() {
        super.execute();
        if (this.getController().atSetpoint() || !Robot.vision.isDetetTarget()) {
            out = 0;
        }
        driveCommand.execute();
    }

    @Override
    public void end(boolean interrupted) {
        // Robot.vision.setLimelightPipeline(VisionConfig.aprilTagPipeline);
        // getLedCommand(tagID).end(interrupted);
    }

    /**
     * Returns true when the command should end. If in auto, automatically end when crosshair gets
     * close to setpoint {@link #error}
     */
    @Override
    public boolean isFinished() {
        if (DriverStation.isAutonomousEnabled()) {
            if (Math.abs(getHorizontalOffset()) <= error) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
}

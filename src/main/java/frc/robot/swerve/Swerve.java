package frc.robot.swerve;

import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Robot;
import frc.robot.RobotTelemetry;
import frc.robot.swerve.configs.MUSICDISC2023;
import frc.robot.swerve.configs.NOTEBLOCK2023;
import frc.spectrumLib.swerve.SwerveDrivetrain;
import frc.spectrumLib.swerve.SwerveDrivetrain.SwerveState;
import frc.spectrumLib.swerve.SwerveRequest;
import frc.spectrumLib.swerve.config.SwerveConfig;
import java.util.function.Consumer;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

public class Swerve implements Subsystem {
    public final SwerveConfig config;
    private final SwerveDrivetrain drivetrain;
    private final RotationController rotationController;
    private double OdometryUpdateFrequency = 250;
    private double targetHeading = 0;

    public Swerve() {
        RobotTelemetry.print("Swerve Subsystem Starting: ");

        // Choose the correct swerve configuration
        switch (Robot.config.getRobotType()) {
            case NOTEBLOCK:
                config = NOTEBLOCK2023.config;
                break;
            case MUSICDISC:
                config = MUSICDISC2023.config;
                break;
            case SIM: // runs in simulation and replay
            case REPLAY:
                OdometryUpdateFrequency = 50;
                config = NOTEBLOCK2023.config;
                break;
            default:
                config = NOTEBLOCK2023.config;
                break;
        }
        drivetrain = new SwerveDrivetrain(config, OdometryUpdateFrequency);

        rotationController = new RotationController(this);
        RobotTelemetry.print("Swerve Subsystem Initialized: ");
    }

    @Override
    public void periodic() {}

    @Override
    public void simulationPeriodic() {
        drivetrain.updateSimState(0.02, 12);
    }

    public Command applyRequest(Supplier<SwerveRequest> requestSupplier) {
        return run(() -> setControlMode(requestSupplier.get()));
    }

    /**
     * Use this to control the swerve drive, set motors, etc.
     *
     * @param mode
     */
    private void setControlMode(SwerveRequest mode) {
        drivetrain.setControl(mode);
    }

    public SwerveState getState() {
        return drivetrain.getState();
    }

    public Pose2d getPose() {
        return getState().Pose;
    }

    public Rotation2d getRotation() {
        return getPose().getRotation();
    }

    public void resetRotationController() {
        rotationController.reset();
    }

    public double calculateRotationController(DoubleSupplier targetRadians) {
        return rotationController.calculate(targetRadians.getAsDouble());
    }

    public void setTargetHeading(double targetHeading) {
        this.targetHeading = targetHeading;
    }

    public double getTargetHeading() {
        return targetHeading;
    }

    /**
     * Takes the current orientation of the robot plus an angle offset and makes it X forward for
     * field-relative maneuvers.
     */
    public void seedFieldRelative(double offsetDegrees) {
        drivetrain.seedFieldRelative(offsetDegrees);
    }

    /** This will zero the entire odometry, and place the robot at 0,0 */
    public void zeroOdoemtry() {
        drivetrain.tareEverything();
    }

    public void addVisionMeasurement(
            Pose2d visionRobotPoseMeters,
            double timestampSeconds,
            Matrix<N3, N1> visionMeasurementStdDevs) {
        drivetrain.addVisionMeasurement(
                visionRobotPoseMeters, timestampSeconds, visionMeasurementStdDevs);
    }

    public void addVisionMeasurement(Pose2d visionRobotPoseMeters, double timestampSeconds) {
        drivetrain.addVisionMeasurement(visionRobotPoseMeters, timestampSeconds);
    }

    public void setVisionMeasurementStdDevs(Matrix<N3, N1> visionMeasurementStdDevs) {
        drivetrain.setVisionMeasurementStdDevs(visionMeasurementStdDevs);
    }

    /**
     * Register the specified lambda to be executed whenever our SwerveDriveState function is
     * updated in our odometry thread.
     *
     * <p>It is imperative that this function is cheap, as it will be executed along with the
     * odometry call, and if this takes a long time, it may negatively impact the odometry of this
     * stack.
     *
     * <p>This can also be used for logging data if the function performs logging instead of
     * telemetry
     *
     * @param telemetryFunction Function to call for telemetry or logging
     */
    public void registerTelemetry(Consumer<SwerveState> telemetryFunction) {
        drivetrain.registerTelemetry(telemetryFunction);
    }
}

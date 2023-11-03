package frc.robot.swerve.configs;

import edu.wpi.first.math.util.Units;
import frc.spectrumLib.swerve.config.DefaultConfig;
import frc.spectrumLib.swerve.config.DefaultConfig.SlotGains;
import frc.spectrumLib.swerve.config.ModuleConfig;
import frc.spectrumLib.swerve.config.ModuleConfig.SwerveModuleSteerFeedbackType;
import frc.spectrumLib.swerve.config.SwerveConfig;

public class NOTEBLOCK2023 {

    // Angle Offsets
    private static final double kFrontLeftCANcoderOffset = -0.83544921875;
    private static final double kFrontRightCANncoderOffset = -0.15234375;
    private static final double kBackLeftCANcoderOffset = -0.4794921875;
    private static final double kBackRightCANcoderOffset = -0.84130859375;

    // Tuning Config
    // Estimated at first, then fudge-factored to make odom match record
    private static final double kWheelRadiusInches = 2.167;
    private static final double speedAt12VoltsMps = 16;
    private static final double slipCurrent = 30;
    private static final SlotGains driveGains = new SlotGains(3, 0, 0, 0, 0);
    private static final SlotGains steerGains = new SlotGains(100, 0, 0.05, 0, 0);

    // Device Setup
    private static final String kCANbusName = "rio";
    private static final boolean supportsPro = false;
    private static final SwerveModuleSteerFeedbackType steerFeedbackType =
            SwerveModuleSteerFeedbackType.FusedCANcoder;

    // Physical Config
    private static final double wheelBaseInches = 21.5;
    private static final double trackWidthInches = 18.5;
    private static final double kDriveGearRatio = (6.746 / 1.0);
    private static final double kSteerGearRatio = (50.0 / 14.0) * (60.0 / 10.0);

    // Wheel Positions
    private static final double kFrontLeftXPos = Units.inchesToMeters(wheelBaseInches / 2.0);
    private static final double kFrontLeftYPos = Units.inchesToMeters(trackWidthInches / 2.0);
    private static final double kFrontRightXPos = Units.inchesToMeters(wheelBaseInches / 2.0);
    private static final double kFrontRightYPos = Units.inchesToMeters(-trackWidthInches / 2.0);
    private static final double kBackLeftXPos = Units.inchesToMeters(-wheelBaseInches / 2.0);
    private static final double kBackLeftYPos = Units.inchesToMeters(trackWidthInches / 2.0);
    private static final double kBackRightXPos = Units.inchesToMeters(-wheelBaseInches / 2.0);
    private static final double kBackRightYPos = Units.inchesToMeters(-trackWidthInches / 2.0);

    public static final ModuleConfig FrontLeft =
            DefaultConfig.FrontLeft.withCANcoderOffset(kFrontLeftCANcoderOffset)
                    .withLocationX(kFrontLeftXPos)
                    .withLocationY(kFrontLeftYPos)
                    .withSlipCurrent(slipCurrent)
                    .withSpeedAt12VoltsMps(speedAt12VoltsMps)
                    .withDriveMotorGearRatio(kDriveGearRatio)
                    .withSteerMotorGearRatio(kSteerGearRatio)
                    .withDriveMotorGains(driveGains)
                    .withSteerMotorGains(steerGains)
                    .withWheelRadius(kWheelRadiusInches)
                    .withFeedbackSource(steerFeedbackType);

    public static final ModuleConfig FrontRight =
            DefaultConfig.FrontRight.withCANcoderOffset(kFrontRightCANncoderOffset)
                    .withLocationX(kFrontRightXPos)
                    .withLocationY(kFrontRightYPos)
                    .withSlipCurrent(slipCurrent)
                    .withSpeedAt12VoltsMps(speedAt12VoltsMps)
                    .withDriveMotorGearRatio(kDriveGearRatio)
                    .withSteerMotorGearRatio(kSteerGearRatio)
                    .withDriveMotorGains(driveGains)
                    .withSteerMotorGains(steerGains)
                    .withWheelRadius(kWheelRadiusInches)
                    .withFeedbackSource(steerFeedbackType);

    public static final ModuleConfig BackLeft =
            DefaultConfig.BackLeft.withCANcoderOffset(kBackLeftCANcoderOffset)
                    .withLocationX(kBackLeftXPos)
                    .withLocationY(kBackLeftYPos)
                    .withSlipCurrent(slipCurrent)
                    .withSpeedAt12VoltsMps(speedAt12VoltsMps)
                    .withDriveMotorGearRatio(kDriveGearRatio)
                    .withSteerMotorGearRatio(kSteerGearRatio)
                    .withDriveMotorGains(driveGains)
                    .withSteerMotorGains(steerGains)
                    .withWheelRadius(kWheelRadiusInches)
                    .withFeedbackSource(steerFeedbackType);

    public static final ModuleConfig BackRight =
            DefaultConfig.BackRight.withCANcoderOffset(kBackRightCANcoderOffset)
                    .withLocationX(kBackRightXPos)
                    .withLocationY(kBackRightYPos)
                    .withSlipCurrent(slipCurrent)
                    .withSpeedAt12VoltsMps(speedAt12VoltsMps)
                    .withDriveMotorGearRatio(kDriveGearRatio)
                    .withSteerMotorGearRatio(kSteerGearRatio)
                    .withDriveMotorGains(driveGains)
                    .withSteerMotorGains(steerGains)
                    .withWheelRadius(kWheelRadiusInches)
                    .withFeedbackSource(steerFeedbackType);

    public static final ModuleConfig[] ModuleConfigs = {FrontLeft, FrontRight, BackLeft, BackRight};

    public static final SwerveConfig config =
            DefaultConfig.DrivetrainConstants.withCANbusName(kCANbusName)
                    .withSupportsPro(supportsPro)
                    .withModules(ModuleConfigs);
}

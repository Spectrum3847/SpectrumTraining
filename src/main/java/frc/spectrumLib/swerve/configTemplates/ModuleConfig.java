package frc.spectrumLib.swerve.configTemplates;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfiguration;

/** This is a class for the configuration of a single swerve module. */
public class ModuleConfig {
    public final String canBus;
    public final int driveMotorID;
    public final int angleMotorID;
    public final int absAngleSensorID;
    public final double angleOffset;

    public final TalonFXConfiguration swerveAngleFXConfig;
    public final TalonFXConfiguration swerveDriveFXConfig;
    public final CANcoderConfiguration swerveCanCoderConfig;

    public ModuleConfig(
            String canBus,
            int driveMotorID,
            int angleMotorID,
            int AbsAngleSensorID,
            double angleOffset,
            PhysicalConfig physical,
            TuningConfig tuning) {
        this.canBus = canBus;
        this.driveMotorID = driveMotorID;
        this.angleMotorID = angleMotorID;
        this.absAngleSensorID = AbsAngleSensorID;
        this.angleOffset = angleOffset;

        swerveAngleFXConfig = new TalonFXConfiguration();
        swerveDriveFXConfig = new TalonFXConfiguration();
        swerveCanCoderConfig = new CANCoderConfiguration();

        /* Swerve Angle Motor Configurations */
        SupplyCurrentLimitConfiguration angleSupplyLimit =
                new SupplyCurrentLimitConfiguration(
                        tuning.angleEnableCurrentLimit,
                        tuning.angleContinuousCurrentLimit,
                        tuning.anglePeakCurrentLimit,
                        tuning.anglePeakCurrentDuration);

        swerveAngleFXConfig.slot0.kP = tuning.angleKP;
        swerveAngleFXConfig.slot0.kI = 0;
        swerveAngleFXConfig.slot0.kD = tuning.angleKD;
        swerveAngleFXConfig.slot0.kF = 0;
        swerveAngleFXConfig.slot0.allowableClosedloopError = 0.0;
        swerveAngleFXConfig.supplyCurrLimit = angleSupplyLimit;
        swerveAngleFXConfig.initializationStrategy = SensorInitializationStrategy.BootToZero;

        /* Swerve Drive Motor Configuration */
        SupplyCurrentLimitConfiguration driveSupplyLimit =
                new SupplyCurrentLimitConfiguration(
                        tuning.driveEnableCurrentLimit,
                        tuning.driveContinuousCurrentLimit,
                        tuning.drivePeakCurrentLimit,
                        tuning.drivePeakCurrentDuration);

        swerveDriveFXConfig.slot0.kP = tuning.driveKP;
        swerveDriveFXConfig.slot0.kI = 0;
        swerveDriveFXConfig.slot0.kD = tuning.driveKD;
        swerveDriveFXConfig.slot0.kF =
                1023 / ((tuning.maxVelocity) / PhysicalConfig.swerveMetersPerPulse);
        swerveDriveFXConfig.supplyCurrLimit = driveSupplyLimit;
        swerveDriveFXConfig.initializationStrategy = SensorInitializationStrategy.BootToZero;
        swerveDriveFXConfig.openloopRamp = 0.0;
        swerveDriveFXConfig.closedloopRamp = 0.0;

        /* Swerve CANCoder Configuration */
        swerveCanCoderConfig.absoluteSensorRange = AbsoluteSensorRange.Unsigned_0_to_360;
        swerveCanCoderConfig.sensorDirection = physical.angleSensorInvert;
        swerveCanCoderConfig.initializationStrategy =
                SensorInitializationStrategy.BootToAbsolutePosition;
        swerveCanCoderConfig.sensorTimeBase = SensorTimeBase.PerSecond;
    }

    public ModuleConfig(
            int driveMotorID,
            int angleMotorID,
            int absAngleSensorID,
            double angleOffset,
            PhysicalConfig physical,
            TuningConfig tuning) {
        this("rio", driveMotorID, angleMotorID, absAngleSensorID, angleOffset, physical, tuning);
    }
}

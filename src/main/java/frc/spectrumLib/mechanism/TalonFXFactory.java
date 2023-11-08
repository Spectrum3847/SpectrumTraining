package frc.spectrumLib.mechanism;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.ForwardLimitSourceValue;
import com.ctre.phoenix6.signals.ForwardLimitTypeValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.ReverseLimitSourceValue;
import com.ctre.phoenix6.signals.ReverseLimitTypeValue;
import edu.wpi.first.wpilibj.DriverStation;
import frc.spectrumLib.util.CanDeviceId;

/**
 * Creates CANTalon objects and configures all the parameters we care about to factory defaults.
 * Closed-loop and sensor parameters are not set, as these are expected to be set by the
 * application.
 */
public class TalonFXFactory {

    private static NeutralModeValue NEUTRAL_MODE = NeutralModeValue.Brake;
    private static InvertedValue INVERT_VALUE = InvertedValue.CounterClockwise_Positive;
    private static double NEUTRAL_DEADBAND = 0.04;
    private static double SUPPLY_CURRENT_LIMIT = 40;
    private TalonFXConfiguration config;

    private TalonFXFactory() {
        config = getDefaultConfig();
    }

    // create a CANTalon with the default (out of the box) configuration
    public static TalonFX createDefaultTalon(CanDeviceId id) {
        var talon = createTalon(id);
        talon.getConfigurator().apply(getDefaultConfig());
        return talon;
    }

    public static TalonFX createConfigTalon(CanDeviceId id, TalonFXConfiguration config) {
        var talon = createTalon(id);
        talon.getConfigurator().apply(config);
        return talon;
    }

    // Use this to apply a created configuration to an existing talon
    public TalonFX applyConfig(TalonFX talon) {
        talon.getConfigurator().apply(config);
        return talon;
    }

    // Use this to create a TalonFX with the custom configuration
    public TalonFX createNew(CanDeviceId id) {
        var talon = createTalon(id);
        talon.getConfigurator().apply(config);
        return talon;
    }

    public static TalonFXFactory start() {
        return new TalonFXFactory();
    }

    public TalonFXFactory withCounterClockwise_Positive() {
        config.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;
        return this;
    }

    public TalonFXFactory withClockwise_Positive() {
        config.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;
        return this;
    }

    public TalonFXFactory withSupplyCurrentLimit(double supplyLimit, boolean enabled) {
        config.CurrentLimits.SupplyCurrentLimit = supplyLimit;
        config.CurrentLimits.SupplyCurrentLimitEnable = enabled;
        return this;
    }

    public TalonFXFactory withStatorCurrentLimit(double statorLimit, boolean enabled) {
        config.CurrentLimits.StatorCurrentLimit = statorLimit;
        config.CurrentLimits.StatorCurrentLimitEnable = enabled;
        return this;
    }

    public TalonFXFactory withFeedbackPID(int slot, double kP, double kI, double kD) {
        if (slot == 0) {
            config.Slot0.kP = kP;
            config.Slot0.kI = kI;
            config.Slot0.kD = kD;
        } else if (slot == 1) {
            config.Slot1.kP = kP;
            config.Slot1.kI = kI;
            config.Slot1.kD = kD;
        } else if (slot == 2) {
            config.Slot2.kP = kP;
            config.Slot2.kI = kI;
            config.Slot2.kD = kD;
        } else {
            DriverStation.reportWarning("TalonFXFactory: Invalid Feedback slot", false);
        }
        return this;
    }

    public TalonFXFactory withFeedbackPID(double kP, double kI, double kD) {
        return withFeedbackPID(0, kP, kI, kD);
    }

    public TalonFXFactory withFeedForward(int slot, double kV, double kA, double kS, double kG) {
        if (slot == 0) {
            config.Slot0.kV = kV;
            config.Slot0.kA = kA;
            config.Slot0.kS = kS;
            config.Slot0.kG = kG;
        } else if (slot == 1) {
            config.Slot1.kV = kV;
            config.Slot1.kA = kA;
            config.Slot1.kS = kS;
            config.Slot1.kG = kG;
        } else if (slot == 2) {
            config.Slot2.kV = kV;
            config.Slot2.kA = kA;
            config.Slot2.kS = kS;
            config.Slot2.kG = kG;
        } else {
            DriverStation.reportWarning("TalonFXFactory: Invalid FeedForward slot", false);
        }
        return this;
    }

    public TalonFXFactory withFeedForward(double kV, double kA, double kS, double kG) {
        return withFeedForward(0, kV, kA, kS, kG);
    }

    public TalonFXFactory withMotionMagic(double cruiseVelocity, double acceleration, double jerk) {
        config.MotionMagic.MotionMagicCruiseVelocity = cruiseVelocity;
        config.MotionMagic.MotionMagicAcceleration = acceleration;
        config.MotionMagic.MotionMagicJerk = jerk;
        return this;
    }

    // This is the ratio of rotor rotations to the mechanism's output.
    // If a remote sensor is used this a ratio of sensor rotations to the mechanism's output.
    public TalonFXFactory withGearRatio(double gearRatio) {
        config.Feedback.SensorToMechanismRatio = gearRatio;
        return this;
    }

    public TalonFXFactory withNeutralBrakeMode(TalonFXConfiguration config, boolean brakeMode) {
        if (brakeMode) {
            config.MotorOutput.NeutralMode = NeutralModeValue.Brake;
        } else {
            config.MotorOutput.NeutralMode = NeutralModeValue.Coast;
        }
        return this;
    }

    // Create a new follower talon with same configuration as the leader talon
    public static TalonFX createPermanentFollowerTalon(
            CanDeviceId follower_id, TalonFX leaderTalonFX, boolean opposeLeaderDirection) {
        String leaderCanBus = leaderTalonFX.getNetwork();
        int leaderId = leaderTalonFX.getDeviceID();
        if (!follower_id.getBus().equals(leaderCanBus)) {
            throw new RuntimeException("Master and Slave Talons must be on the same CAN bus");
        }

        TalonFXConfiguration followerConfig = getDefaultConfig();
        leaderTalonFX.getConfigurator().refresh(followerConfig);
        final TalonFX talon = createConfigTalon(follower_id, followerConfig);

        talon.setControl(new Follower(leaderId, opposeLeaderDirection));
        return talon;
    }

    public static TalonFXConfiguration getDefaultConfig() {
        TalonFXConfiguration config = new TalonFXConfiguration();

        config.MotorOutput.NeutralMode = NEUTRAL_MODE;
        config.MotorOutput.Inverted = INVERT_VALUE;
        config.MotorOutput.DutyCycleNeutralDeadband = NEUTRAL_DEADBAND;
        config.MotorOutput.PeakForwardDutyCycle = 1.0;
        config.MotorOutput.PeakReverseDutyCycle = -1.0;

        config.CurrentLimits.SupplyCurrentLimit = SUPPLY_CURRENT_LIMIT;
        config.CurrentLimits.SupplyCurrentLimitEnable = true;
        config.CurrentLimits.StatorCurrentLimitEnable = false;

        config.SoftwareLimitSwitch.ForwardSoftLimitEnable = false;
        config.SoftwareLimitSwitch.ForwardSoftLimitThreshold = 0;
        config.SoftwareLimitSwitch.ReverseSoftLimitEnable = false;
        config.SoftwareLimitSwitch.ReverseSoftLimitThreshold = 0;

        config.Feedback.FeedbackSensorSource = FeedbackSensorSourceValue.RotorSensor;
        config.Feedback.FeedbackRotorOffset = 0;
        config.Feedback.SensorToMechanismRatio = 1;

        config.HardwareLimitSwitch.ForwardLimitEnable = false;
        config.HardwareLimitSwitch.ForwardLimitAutosetPositionEnable = false;
        config.HardwareLimitSwitch.ForwardLimitSource = ForwardLimitSourceValue.LimitSwitchPin;
        config.HardwareLimitSwitch.ForwardLimitType = ForwardLimitTypeValue.NormallyOpen;
        config.HardwareLimitSwitch.ReverseLimitEnable = false;
        config.HardwareLimitSwitch.ReverseLimitAutosetPositionEnable = false;
        config.HardwareLimitSwitch.ReverseLimitSource = ReverseLimitSourceValue.LimitSwitchPin;
        config.HardwareLimitSwitch.ReverseLimitType = ReverseLimitTypeValue.NormallyOpen;

        config.Audio.BeepOnBoot = true;
        config.Audio.AllowMusicDurDisable = true;
        config.Audio.BeepOnConfig = true;

        return config;
    }

    private static TalonFX createTalon(CanDeviceId id) {
        TalonFX talon = new TalonFX(id.getDeviceNumber(), id.getBus());
        talon.clearStickyFaults();

        return talon;
    }
}

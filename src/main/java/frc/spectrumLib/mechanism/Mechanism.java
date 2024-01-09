package frc.spectrumLib.mechanism;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.MotionMagicTorqueCurrentFOC;
import com.ctre.phoenix6.controls.MotionMagicVelocityTorqueCurrentFOC;
import com.ctre.phoenix6.controls.MotionMagicVelocityVoltage;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.GravityTypeValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.spectrumLib.util.CanDeviceId;

public abstract class Mechanism implements Subsystem {
    protected boolean attached = false;
    protected TalonFX motor;
    public Config config;

    public Mechanism(boolean attached) {
        this.attached = attached;
        this.config = setConfig();
    }

    protected abstract Config setConfig();

    protected void setConfig(Config config) {
        this.config = config;
    };

    public void stop() {
        if (attached) {
            motor.setControl(new DutyCycleOut(0));
        }
    }

    public void setMMVelocityFOC(double velocity) {
        if (attached) {
            MotionMagicVelocityTorqueCurrentFOC mm = config.mmVelocityFOC.withVelocity(velocity);
            motor.setControl(mm);
        }
    }

    public void setMMPositionFOC(double position) {
        if (attached) {
            MotionMagicTorqueCurrentFOC mm = config.mmPositionFOC.withPosition(position);
            motor.setControl(mm);
        }
    }

    public void setPercentOutput(double percent) {
        if (attached) {
            DutyCycleOut output = config.percentOutput.withOutput(percent);
            motor.setControl(output);
        }
    }

    public static class Config {
        public String name;
        public CanDeviceId id;
        public TalonFXConfiguration talonConfig;

        public MotionMagicVelocityTorqueCurrentFOC mmVelocityFOC =
                new MotionMagicVelocityTorqueCurrentFOC(0);
        public MotionMagicTorqueCurrentFOC mmPositionFOC = new MotionMagicTorqueCurrentFOC(0);
        public MotionMagicVelocityVoltage mmVelocityVoltage = new MotionMagicVelocityVoltage(0);
        public MotionMagicVoltage mmPositionVoltage = new MotionMagicVoltage(0);
        public DutyCycleOut percentOutput = new DutyCycleOut(0);

        public Config(String name, int id, String canbus) {
            this.name = name;
            this.id = new CanDeviceId(id, canbus);
            talonConfig = new TalonFXConfiguration();
        }

        public void applyTalonConfig(TalonFX talon) {
            talon.getConfigurator().apply(talonConfig);
        }

        public void configCounterClockwise_Positive() {
            talonConfig.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;
        }

        public void configClockwise_Positive() {
            talonConfig.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;
        }

        public void configSupplyCurrentLimit(double supplyLimit, boolean enabled) {
            talonConfig.CurrentLimits.SupplyCurrentLimit = supplyLimit;
            talonConfig.CurrentLimits.SupplyCurrentLimitEnable = enabled;
        }

        public void configStatorCurrentLimit(double statorLimit, boolean enabled) {
            talonConfig.CurrentLimits.StatorCurrentLimit = statorLimit;
            talonConfig.CurrentLimits.StatorCurrentLimitEnable = enabled;
        }

        public void configNeutralDeadband(double deadband) {
            talonConfig.MotorOutput.DutyCycleNeutralDeadband = deadband;
        }

        public void configPeakOutput(double forward, double reverse) {
            talonConfig.MotorOutput.PeakForwardDutyCycle = forward;
            talonConfig.MotorOutput.PeakReverseDutyCycle = reverse;
        }

        public void configForwardSoftLimit(double threshold, boolean enabled) {
            talonConfig.SoftwareLimitSwitch.ForwardSoftLimitThreshold = threshold;
            talonConfig.SoftwareLimitSwitch.ForwardSoftLimitEnable = enabled;
        }

        public void configReverseSoftLimit(double threshold, boolean enabled) {
            talonConfig.SoftwareLimitSwitch.ReverseSoftLimitThreshold = threshold;
            talonConfig.SoftwareLimitSwitch.ReverseSoftLimitEnable = enabled;
        }

        // Configure optional motion magic velocity parameters
        public void configMotionMagicVelocity(double acceleration, double feedforward) {
            mmVelocityFOC =
                    mmVelocityFOC.withAcceleration(acceleration).withFeedForward(feedforward);
            mmVelocityVoltage =
                    mmVelocityVoltage.withAcceleration(acceleration).withFeedForward(feedforward);
        }

        // Configure optional motion magic position parameters
        public void configMotionMagicPosition(double feedforward) {
            mmPositionFOC = mmPositionFOC.withFeedForward(feedforward);
            mmPositionVoltage = mmPositionVoltage.withFeedForward(feedforward);
        }

        public void configMotionMagic(double cruiseVelocity, double acceleration, double jerk) {
            talonConfig.MotionMagic.MotionMagicCruiseVelocity = cruiseVelocity;
            talonConfig.MotionMagic.MotionMagicAcceleration = acceleration;
            talonConfig.MotionMagic.MotionMagicJerk = jerk;
        }

        // This is the ratio of rotor rotations to the mechanism's output.
        // If a remote sensor is used this a ratio of sensor rotations to the mechanism's output.
        public void configGearRatio(double gearRatio) {
            talonConfig.Feedback.SensorToMechanismRatio = gearRatio;
        }

        public double getGearRatio() {
            return talonConfig.Feedback.SensorToMechanismRatio;
        }

        public void configNeutralBrakeMode(boolean isInBrake) {
            if (isInBrake) {
                talonConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;
            } else {
                talonConfig.MotorOutput.NeutralMode = NeutralModeValue.Coast;
            }
        }

        /**
         * Defaults to slot 0
         *
         * @param kP
         * @param kI
         * @param kD
         */
        public void configPIDGains(double kP, double kI, double kD) {
            configPIDGains(0, kP, kI, kD);
        }

        public void configPIDGains(int slot, double kP, double kI, double kD) {
            talonConfigFeedbackPID(slot, kP, kI, kD);
        }

        /**
         * Defaults to slot 0
         *
         * @param kS
         * @param kV
         * @param kA
         * @param kG
         */
        public void configFeedForwardGains(double kS, double kV, double kA, double kG) {
            configFeedForwardGains(0, kS, kV, kA, kG);
        }

        public void configFeedForwardGains(int slot, double kS, double kV, double kA, double kG) {
            talonConfigFeedForward(slot, kV, kA, kS, kG);
        }

        public void configFeedbackSensorSource(FeedbackSensorSourceValue source) {
            configFeedbackSensorSource(source, 0);
        }

        public void configFeedbackSensorSource(FeedbackSensorSourceValue source, double offset) {
            talonConfig.Feedback.FeedbackSensorSource = source;
            talonConfig.Feedback.FeedbackRotorOffset = offset;
        }

        /**
         * Defaults to slot 0
         *
         * @param isArm
         */
        public void configGravityType(boolean isArm) {
            configGravityType(0, isArm);
        }

        public void configGravityType(int slot, boolean isArm) {
            GravityTypeValue gravityType =
                    isArm ? GravityTypeValue.Arm_Cosine : GravityTypeValue.Elevator_Static;
            if (slot == 0) {
                talonConfig.Slot0.GravityType = gravityType;
            } else if (slot == 1) {
                talonConfig.Slot1.GravityType = gravityType;
            } else if (slot == 2) {
                talonConfig.Slot2.GravityType = gravityType;
            } else {
                DriverStation.reportWarning("MechConfig: Invalid slot", false);
            }
        }

        // Configure the TalonFXConfiguration feed forward gains
        private void talonConfigFeedForward(int slot, double kV, double kA, double kS, double kG) {
            if (slot == 0) {
                talonConfig.Slot0.kV = kV;
                talonConfig.Slot0.kA = kA;
                talonConfig.Slot0.kS = kS;
                talonConfig.Slot0.kG = kG;
            } else if (slot == 1) {
                talonConfig.Slot1.kV = kV;
                talonConfig.Slot1.kA = kA;
                talonConfig.Slot1.kS = kS;
                talonConfig.Slot1.kG = kG;
            } else if (slot == 2) {
                talonConfig.Slot2.kV = kV;
                talonConfig.Slot2.kA = kA;
                talonConfig.Slot2.kS = kS;
                talonConfig.Slot2.kG = kG;
            } else {
                DriverStation.reportWarning("MechConfig: Invalid FeedForward slot", false);
            }
        }

        private void talonConfigFeedbackPID(int slot, double kP, double kI, double kD) {
            if (slot == 0) {
                talonConfig.Slot0.kP = kP;
                talonConfig.Slot0.kI = kI;
                talonConfig.Slot0.kD = kD;
            } else if (slot == 1) {
                talonConfig.Slot1.kP = kP;
                talonConfig.Slot1.kI = kI;
                talonConfig.Slot1.kD = kD;
            } else if (slot == 2) {
                talonConfig.Slot2.kP = kP;
                talonConfig.Slot2.kI = kI;
                talonConfig.Slot2.kD = kD;
            } else {
                DriverStation.reportWarning("MechConfig: Invalid Feedback slot", false);
            }
        }
    }
}
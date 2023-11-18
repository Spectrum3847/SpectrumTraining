package frc.spectrumLib.mechanism;

import com.ctre.phoenix6.controls.MotionMagicVelocityVoltage;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.spectrumLib.util.CanDeviceId;

public abstract class Mechanism extends SubsystemBase {
    protected boolean attached = false;
    public TalonFX motor;
    public Config config;

    public Mechanism(boolean attached) {
        this.attached = attached;
        this.config = setConfig();
    }

    protected abstract Config setConfig();

    protected void setConfig(Config config) {
        this.config = config;
    };

    public void setMMVelocity(double velocity) {
        if (attached) {
            MotionMagicVelocityVoltage mm = config.mmVelocity.withVelocity(velocity);
            motor.setControl(mm);
        }
    }

    public void setMMPosition(double position) {
        if (attached) {
            MotionMagicVoltage mm = config.mmPosition.withPosition(position);
            motor.setControl(mm);
        }
    }

    public static class Config {
        public String name;
        public CanDeviceId id;
        public boolean inverted;
        public boolean neutralBrakeMode;
        public double gearRatio = 1;
        public double supplyCurrentLimit;

        public MotionMagicVelocityVoltage mmVelocity = new MotionMagicVelocityVoltage(0);
        public MotionMagicVoltage mmPosition = new MotionMagicVoltage(0);
        public Slot slot0 = new Slot(0);
        public Slot slot1 = new Slot(1);
        public Slot slot2 = new Slot(2);

        public class Slot {
            public int slot;
            public double kP = 0;
            public double kI = 0;
            public double kD = 0;
            public double kS = 0;
            public double kV = 0;
            public double kA = 0;
            public double kG = 0;
            public boolean isArm = false;

            private Slot(int slot) {
                if (slot < 0 || slot > 2) {
                    DriverStation.reportWarning("MechConfig: Invalid slot", false);
                    return;
                }
                this.slot = slot;
            }
        }

        public Config(
                String name,
                int id,
                String canbus,
                boolean inverted,
                boolean neutralBrakeMode,
                double supplyCurrentLimit) {
            this.name = name;
            this.id = new CanDeviceId(id, canbus);
            this.inverted = inverted;
            this.neutralBrakeMode = neutralBrakeMode;
            this.supplyCurrentLimit = supplyCurrentLimit;
        }

        // Configure optional motion magic velocity parameters
        public void configMotionMagicVelocity(double acceleration, double feedforward) {
            mmVelocity = mmVelocity.withAcceleration(acceleration).withFeedForward(feedforward);
        }

        // Configure optional motion magic position parameters
        public void configMotionMagicPosition(double feedforward) {
            mmPosition = mmPosition.withFeedForward(feedforward);
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
            Slot s = getSlot(slot);
            s.kP = kP;
            s.kI = kI;
            s.kD = kD;
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
            Slot s = getSlot(slot);
            s.kS = kS;
            s.kV = kV;
            s.kA = kA;
            s.kG = kG;
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
            Slot s = getSlot(slot);
            s.isArm = isArm;
        }

        private Slot getSlot(int slot) {
            if (slot == 0) {
                return slot0;
            } else if (slot == 1) {
                return slot1;
            } else if (slot == 2) {
                return slot2;
            } else {
                DriverStation.reportWarning("MechConfig: Invalid slot", false);
                return null;
            }
        }
    }
}

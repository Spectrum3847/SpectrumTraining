package frc.spectrumLib.mechanism;

import com.ctre.phoenix6.controls.MotionMagicVelocityVoltage;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj2.command.Subsystem;

public abstract class Mechanism implements Subsystem {
    protected TalonFX motor;
    public MechConfig config;

    public Mechanism(MechConfig config) {
        this.config = config;
    }

    public void setMMVelocity(double velocity) {
        if (motor != null) {
            MotionMagicVelocityVoltage mm = config.mmVelocity.withVelocity(velocity);
            motor.setControl(mm);
        }
    }

    public void setMMPosition(double position) {
        if (motor != null) {
            MotionMagicVoltage mm = config.mmPosition.withPosition(position);
            motor.setControl(mm);
        }
    }
}

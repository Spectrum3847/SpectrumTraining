package frc.robot.swerve.commands;

import edu.wpi.first.units.Distance;
import edu.wpi.first.units.Measure;
import edu.wpi.first.units.MutableMeasure;
import edu.wpi.first.units.Velocity;
import edu.wpi.first.units.Voltage;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import frc.robot.Robot;
import frc.spectrumLib.swerve.config.DefaultConfig;
import frc.spectrumLib.swerve.config.ModuleConfig;
import static edu.wpi.first.units.MutableMeasure.mutable;
import static edu.wpi.first.units.Units.Meters;
import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.Volts;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import java.util.function.DoubleSupplier;

import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;



public class SysIdCommands {
    private static final String kCANBus = "canivore";

   private final TalonFX m_leftLeader = new TalonFX(0, kCANBus);
   private final TalonFX m_rightLeader = new TalonFX(1, kCANBus);
   private final TalonFX m_leftFollower = new TalonFX(2, kCANBus);
   private final TalonFX m_rightFollower = new TalonFX(3, kCANBus);

   private final DutyCycleOut m_leftOut = new DutyCycleOut(0);
   private final DutyCycleOut m_rightOut = new DutyCycleOut(0);

    // Creates a SysIdRoutine
    SysIdRoutine routine = new SysIdRoutine(
    new SysIdRoutine.Config(),
    new SysIdRoutine.Mechanism(this::voltageDrive, this::logMotors, this)
    );
    
    public static Command sysIdQuasistaticForward(){
        return routine.sysIdQuasistatic(SysIdRoutine.Direction.kForward);
    }

    public static Command sysIdQuasistaticBackward(){
        return routine.sysIdQuasistatic(SysIdRoutine.Direction.kReverse);
    }

    public static Command sysIdDynamicForward(){
        return routine.sysIdDynamic(SysIdRoutine.Direction.kForward);
    }

    public static Command sysIdDynamicBackward(){
        return routine.sysIdDynamic(SysIdRoutine.Direction.kReverse);
    }
}
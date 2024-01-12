package frc.robot.intake;

import edu.wpi.first.wpilibj2.command.Command;
import frc.spectrumLib.mechanism.Mechanism;
import frc.spectrumLib.mechanism.TalonFXFactory;

public class Intake extends Mechanism {
    public class IntakeConfig extends Config {
        // Intake speeds in rpm
        public double falconMaxSpeed = 6380; // RPM
        public double intake = 6500; // 2000
        public double slowIntake = 900; // 600
        public double slowIntakePercentage =
                0.06; // This is used instead of a velocity to lower hold current
        public double holdIntakePercentage =
                0.1; // This is used instead of a velocity to lower hold current
        public double eject = -3000;
        public double drop = -300;
        public double floorDrop = -500;

        public final double velocityKp = 0.5;
        public final double velocityKv = 0.5;
        public final double currentLimit = 20;

        public IntakeConfig() {
            super("Intake", 52, "rio");
            configPIDGains(0, velocityKp, 0, 0);
            configFeedForwardGains(0, velocityKv, 0, 0);
            configMotionMagic(120, 195, 50); // TODO: configure jerk
            configSupplyCurrentLimit(currentLimit, true);
            configCounterClockwise_Positive();
            // TODO: config nominal output?
        }
    }

    public IntakeConfig config;

    protected Config setConfig() {
        config = new IntakeConfig();
        return config;
    }

    public Intake(boolean attached) {
        super(attached);
        if (attached) {
            motor = TalonFXFactory.createConfigTalon(config.id, config.talonConfig);
        }
    }

    @Override
    public void periodic() {}

    public Command runPercent(double percent) {
        return run(() -> setPercentOutput(percent)).withName("Intake.runPercent");
    }

    public Command runVelocity(double velocity) {
        return run(() -> setVelocity(velocity)).withName("Intake.runVelocity");
    }
}

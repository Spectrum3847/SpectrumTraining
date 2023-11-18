package frc.robot.intake;

import edu.wpi.first.wpilibj2.command.Command;
import frc.spectrumLib.mechanism.Mechanism;
import frc.spectrumLib.mechanism.TalonFXFactory;

public class Intake extends Mechanism {
    public class IntakeConfig extends Config {

        public double fullSpeed = 1.0;
        public double ejectSpeed = -1.0;

        public IntakeConfig() {
            super("Intake", 60, "3847");
            configPIDGains(0, 0.55, 0, 0.2);
            configFeedForwardGains(0, 0, 0, 0);
            configClockwise_Positive();
            configGearRatio(2);
            configSupplyCurrentLimit(20, true);
        }
    }

    public IntakeConfig config;

    public Intake(boolean attached) {
        super(attached);
        if (attached) {
            motor = TalonFXFactory.createConfigTalon(config.id, config.talonConfig);
        }
    }

    @Override
    public void periodic() {}

    public Command runVelocity(double velocity) {
        return run(() -> setMMVelocityFOC(velocity)).withName("Intake.runVelocity");
    }

    @Override
    protected Config setConfig() {
        config = new IntakeConfig();
        return config;
    }
}

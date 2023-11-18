package frc.robot.slide;

import edu.wpi.first.wpilibj2.command.Command;
import frc.spectrumLib.mechanism.Mechanism;
import frc.spectrumLib.mechanism.TalonFXFactory;

public class Slide extends Mechanism {
    public class SlideConfig extends Config {

        public double top = 10;

        public SlideConfig() {
            super("Slide", 50, "3847");
            configSupplyCurrentLimit(20, true);
            configPIDGains(0, 0.55, 0, 0.2);
            configFeedForwardGains(0, 0, 0, 0);
            configReverseSoftLimit(0, true);
            configForwardSoftLimit(top, true);
        }
    }

    public SlideConfig config;

    public Slide(boolean attached) {
        super(attached);
        if (attached) {
            motor = TalonFXFactory.createConfigTalon(config.id, config.talonConfig);
        }
    }

    @Override
    public void periodic() {}

    public Command runPosition(double position) {
        return run(() -> setMMPositionFOC(position)).withName("Slide.runPosition");
    }

    public Command runStop() {
        return run(() -> stop()).withName("Slide.runStop");
    }

    @Override
    protected Config setConfig() {
        config = new SlideConfig();
        return config;
    }
}

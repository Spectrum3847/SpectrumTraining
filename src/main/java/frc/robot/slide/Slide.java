package frc.robot.slide;

import frc.spectrumLib.mechanism.Mechanism;

public class Slide extends Mechanism {
    public SlideConfig config;

    public static class SlideConfig extends Config {

        public double fullSpeed = 1.0;
        public double ejectSpeed = -1.0;

        public SlideConfig() {
            super("Slide", 60, "3847", false, false, 40);
            configPIDGains(0, 0.55, 0, 0.2);
            configFeedForwardGains(0, 0, 0, 0);
        }
    }

    public Slide() {
        super();
    }

    protected Config setConfig() {
        config = new SlideConfig();
        return config;
    }

    @Override
    public void periodic() {}
}

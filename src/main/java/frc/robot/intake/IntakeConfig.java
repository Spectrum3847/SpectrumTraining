package frc.robot.intake;

import frc.spectrumLib.mechanism.MechConfig;

public class IntakeConfig extends MechConfig {

    public double fullSpeed = 1.0;
    public double ejectSpeed = -1.0;

    public IntakeConfig() {
        super("Intake", 60, "intake", false, false, 40);
        configPIDGains(0, 0.55, 0, 0.2);
        configFeedForwardGains(0, 0, 0, 0);
    }
}

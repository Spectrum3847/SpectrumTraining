package frc.spectrumLib.mechanism;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public final class MechanismTelemetry {
    protected ShuffleboardTab tab = Shuffleboard.getTab("Mechanism Telemetry"); // TODO: fix
    private boolean initialized = false;

    private MechanismTelemetry() {}

    public void testMode() {
        if (!initialized) {
            tab.add("Automatic System Check", false)
                    .withWidget("Toggle Button")
                    .withSize(2, 1)
                    .withPosition(0, 0);
            initialized = true;
        }
    }

    public void runSystemCheck() {}
}

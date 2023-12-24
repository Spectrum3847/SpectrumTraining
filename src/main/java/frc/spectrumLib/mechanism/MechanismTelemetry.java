package frc.spectrumLib.mechanism;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.shuffleboard.SimpleWidget;
import frc.robot.RobotTelemetry;

public final class MechanismTelemetry {
    protected static ShuffleboardTab tab;
    private static SimpleWidget chooser;
    private static boolean initialized = false;
    private static boolean warningGiven = false;
    private static boolean systemCheckRunning = false;
    private static int count = 0;

    private MechanismTelemetry() {}

    public static void testMode() {
        if (!initialized) {
            count++;
            tab = Shuffleboard.getTab("System Check"); // TODO: fix
            chooser =
                    tab.add("Automatic System Check #" + count, false)
                            .withWidget("Toggle Switch")
                            .withSize(2, 1)
                            .withPosition(5, 5);
            initialized = true;
        }
    }

    private static void runSystemCheck() {
        System.out.println("YOOOO ITS WKRING");
    }

    private static void cancelSystemCheck() {
        // commandSequence.cancel();
        System.out.println("CANCEL RAN");
    }

    public static void systemChecker() {
        // System.out.println(
        //         "systemCheckRunning: "
        //                 + systemCheckRunning
        //                 + " chooser value: "
        //                 + chooser.getEntry().getBoolean(false)
        //                 + " initialized: "
        //                 + initialized);
        if (!DriverStation.isTestEnabled() && initialized && !warningGiven) {
            DriverStation.reportWarning(
                    "Automatic System Check not allowed to run if not in test mode", false);
            warningGiven = true;
            return;
        }
        if (!initialized) return;

        if (!systemCheckRunning && chooser.getEntry().getBoolean(false)) {
            runSystemCheck();
            systemCheckRunning = true;
        }

        if (systemCheckRunning && !chooser.getEntry().getBoolean(false)) {
            cancelSystemCheck();
            systemCheckRunning = false;
        }
    }

    public static void close() {
        if (initialized) {
            chooser.getEntry().setBoolean(false);
            chooser.close();
            chooser.getEntry().close();
            chooser.getEntry().unpublish();
            RobotTelemetry.print("Mechanism Telemetry Closed");
            initialized = false;
            warningGiven = false;
        }
    }
}

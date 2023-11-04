package frc.robot.pilot;

public class PilotConfig {
    public static final String name = "Pilot";
    public static final int port = 0;

    public static final double slowModeScalor = 0.5;

    public final double leftStickDeadzone = 0.1;
    public final double leftStickExp = 2.0;
    public final double leftStickScalar = 1.0;

    public final double triggersDeadzone = 0.1;
    public final double triggersExp = 2.0;
    public final double triggersScalar = 1.0;
}

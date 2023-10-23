package frc.robot;

public final class RobotConfig {
    public final Motors motors = new Motors();

    public static final int ledPWMport = 0;

    /** Define the motor IDs in this file */
    public final class Motors {
        public static final int intakeMotor = 1;
    }

    private RobotType robotType = null;

    public RobotConfig() {
        checkRobotType();
        switch (getRobotType()) {
            case SIM:
                /* Set all the constants specifically for the simulation*/
                break;
            default:
                /* Set all the constants specifically for the robot */
                break;
        }
    }

    /** Set the RobotType based on if simulation or the MAC address of the RIO */
    public RobotType checkRobotType() {
        if (Robot.isSimulation()) {
            robotType = RobotType.SIM;
            RobotTelemetry.print("Robot Type: Simulation");
        } else {
            robotType = RobotType.DEFAULT;
            RobotTelemetry.print("Robot Type: Default");
        }
        return robotType;
    }

    public RobotType getRobotType() {
        return robotType;
    }

    public enum RobotType {
        DEFAULT,
        SIM
    }
}

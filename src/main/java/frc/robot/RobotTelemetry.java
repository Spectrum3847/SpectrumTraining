package frc.robot;

import frc.spectrumLib.Telemetry;
import org.littletonrobotics.junction.Logger;

public class RobotTelemetry extends Telemetry {

    public RobotTelemetry() {
        super();
        Logger.recordMetadata("RobotType", Robot.config.getRobotType().name());
    }
}

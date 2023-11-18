package frc.robot.intake;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotTelemetry;
import frc.spectrumLib.mechanism.Mechanism;
import frc.spectrumLib.mechanism.TalonFXFactory;

public class Intake extends Mechanism {
    public IntakeConfig config;

    public static class IntakeConfig extends Config {

        public double fullSpeed = 1.0;
        public double ejectSpeed = -1.0;

        public IntakeConfig() {
            super("Intake", 60, "3847", false, false, 40);
            configPIDGains(0, 0.55, 0, 0.2);
            configFeedForwardGains(0, 0, 0, 0);
        }
    }

    public Intake() {
        super(new IntakeConfig());
        motor =
                TalonFXFactory.start()
                        .withGearRatio(config.gearRatio)
                        .withNeutralBrakeMode(config.neutralBrakeMode)
                        .withClockwise_Positive()
                        .withFeedbackPID(
                                config.slot0.slot,
                                config.slot0.kP,
                                config.slot0.kI,
                                config.slot0.kD)
                        .createNew(config.id);
    }

    public void periodic() {
        RobotTelemetry.print("Intake name: " + config.name);
    }

    public Command runVelocity(double velocity) {
        return run(() -> setMMVelocity(velocity));
    }
}

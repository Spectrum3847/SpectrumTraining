package frc.robot.intake;

import edu.wpi.first.wpilibj2.command.Command;
import frc.spectrumLib.mechanism.Mechanism;
import frc.spectrumLib.mechanism.TalonFXFactory;

public class Intake extends Mechanism {
    public IntakeConfig config;

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

    public Command runVelocity(double velocity) {
        return run(() -> setMMVelocity(velocity));
    }

}

package frc.robot.intake;

import frc.spectrumLib.mechanism.Mechanism;
import frc.spectrumLib.mechanism.TalonFXFactory;

public class Intake extends Mechanism {
    IntakeConfig config;

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

    public void runIntake() {
        setMMVelocity(config.fullSpeed);
    }

    public void ejectIntake() {
        setMMVelocity(config.ejectSpeed);
    }
}

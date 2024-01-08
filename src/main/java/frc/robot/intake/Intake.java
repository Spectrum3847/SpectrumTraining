package frc.robot.intake;

import edu.wpi.first.wpilibj2.command.Command;
import frc.spectrumLib.mechanism.Mechanism;
import frc.spectrumLib.mechanism.TalonFXFactory;
import frc.spectrumLib.util.Conversions;
import org.littletonrobotics.junction.AutoLogOutput;

public class Intake extends Mechanism {
    public class IntakeConfig extends Config {

        /* Intake output in revolutions per min */
        public double maxSpeed = 6800;
        public double intake = 6500;
        public double slowIntake = 900;
        public double eject = -3000;
        public double drop = -300;
        public double floorDrop = -500;
        public double intakeCone = 3000;

        /* Percentage Intake Output */
        public double slowIntakePercentage = 0.06;
        public double holdIntakePercentage = 0.1;

        /* Intake positions? */
        public double customPosition = 500;

        /* Intake config values */
        public double currentLimit = 12;
        public double threshold = 20; // TODO: how set threshold
        public double velocityKp = 0.156152; // 0.156152 // 0.065 * 2048 * (1/1023) * (1/10) * 12
        public double velocityKv = 0.12; // 0.0519 * 2048 * (1/1023) * (1/10) * 12
        // TODO: feedback sensor? old implementation:
        // motor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);

        public IntakeConfig() {
            super("Intake", 52, "rio");
            configPIDGains(0, velocityKp, 0, 0);
            configFeedForwardGains(0.24, velocityKv, 0, 0);
            configGearRatio(2);
            configSupplyCurrentLimit(currentLimit, true);
            configNeutralBrakeMode(true);
            configCounterClockwise_Positive();
            configMotionMagic(5, 10, 50);
        }
    }

    public IntakeConfig config;

    public Intake(boolean attached) {
        super(attached);
        if (attached) {
            motor = TalonFXFactory.createConfigTalon(config.id, config.talonConfig);
        }
    }

    @Override
    public void periodic() {}

    /**
     * Runs the intake at a given velocity
     *
     * @param velocity in revolutions per minute
     */
    public Command runVelocity(double velocity) {
        return run(() -> setMMVelocityFOC(Conversions.RPMtoRPS(velocity)))
                .withName("Intake.runVelocity");
    }

    public Command runPosition(double position) {
        return run(() -> setMMPositionFOC(position)).withName("Intake.runPosition");
    }

    @Override
    protected Config setConfig() {
        config = new IntakeConfig();
        return config;
    }

    @AutoLogOutput(key = "Intake/Velocity")
    public double getMotorVelocity() {
        return Conversions.RPStoRPM(motor.getVelocity().getValueAsDouble());
    }
}

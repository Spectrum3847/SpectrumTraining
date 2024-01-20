package frc.robot.shoulder;

import edu.wpi.first.wpilibj2.command.Command;
import frc.spectrumLib.mechanism.Mechanism;
import frc.spectrumLib.mechanism.TalonFXFactory;
import org.littletonrobotics.junction.AutoLogOutput;

public class Shoulder extends Mechanism {
    public class ShoulderConfig extends Config {

        /* Shoulder constants in rotations */
        public final double maxRotation = 66;
        public final double minRotation = 0.1;

        /* Shoulder positions in percentage of max rotation || 0 is vertical */
        public final int intake = 0;
        public final int airIntake = 15;
        public final int shelfIntake = 82;

        public final int home = 40;
        public final int unblockCameraPos = 22;
        public final int stow = 31;

        public final int floor = 15;
        public final int coneTop = 94;
        public final double coneMid = coneTop;
        public final int prescore = 60;
        public final int cubeUp = 90;

        public final int scorePos = -106;
        public final double zeroSpeed = -0.2;
        public final int launch = 40;

        // Physical Constants
        public final double gearRatio = 1;

        /* Intake config values */
        public double currentLimit = 5;
        public double threshold = 5;
        public double velocityKp = 0.8;
        public double velocityKv = 0.013;
        public double velocityKs = 0;

        public ShoulderConfig() {
            super("Shoulder", 42, "rio");
            configPIDGains(0, velocityKp, 0, 0);
            configFeedForwardGains(velocityKs, velocityKv, 0, 0);
            configGearRatio(1); // TODO: check gear ratio
            configSupplyCurrentLimit(currentLimit, threshold, true);
            configNeutralBrakeMode(true);
            configCounterClockwise_Positive();
            configReverseSoftLimit(minRotation, true);
            configForwardSoftLimit(maxRotation, true);
            configMotionMagic(51, 205, 0);
        }
    }

    public ShoulderConfig config;

    public Shoulder(boolean attached) {
        super(attached);
        if (attached) {
            motor = TalonFXFactory.createConfigTalon(config.id, config.talonConfig);
        }
    }

    @Override
    public void periodic() {}

    /**
     * Sets the intake motor to a specified position.
     *
     * @param percent percentage of max rotation (0 is vertical). Note that the percentage is not
     *     [-1,1] but rather [-100,100]
     */
    public Command runPosition(double percent) {
        return run(() -> setMMPosition(percentToRotation(percent))).withName("Shoulder.runPercent");
    }

    /**
     * Sets the intake motor to a specified position.
     *
     * @param percent percentage of max rotation (0 is vertical). Note that the percentage is not
     *     [-1,1] but rather [-100,100]
     */
    public Command runFOCPosition(double percent) {
        return run(() -> setMMPositionFOC(percentToRotation(percent)))
                .withName("Shoulder.runPercent");
    }

    /**
     * Runs the intake at a specified percentage of its maximum output.
     *
     * @param percent The percentage of the maximum output to run the intake at.
     */
    public Command runManualOutput(double percent) {
        return run(() -> setPercentOutput(percent)).withName("Shoulder.runPercentage");
    }

    public Command brakeMode() {
        return startEnd(() -> setBrakeMode(false), () -> setBrakeMode(true))
                .withName("Shoulder.brakeMode")
                .ignoringDisable(true);
    }

    /**
     * Stops the intake motor.
     *
     * @return
     */
    public Command runStop() {
        return run(() -> stop()).withName("Shoulder.stop");
    }

    /* Custom Commands */
    /** Holds the position of the Shoulder. */
    public Command runHoldShoulder() { // TODO: review; inline custom commands vs. seperate class
        return new Command() {
            double holdPosition = 0; // rotations

            // constructor
            {
                setName("Shoulder.holdPosition");
                addRequirements(Shoulder.this);
            }

            @Override
            public void initialize() {
                holdPosition = motor.getPosition().getValueAsDouble();
            }

            @Override
            public void execute() {
                setMMPosition(holdPosition);
            }

            @Override
            public void end(boolean interrupted) {
                stop();
            }
        };
    }

    @Override
    protected Config setConfig() {
        config = new ShoulderConfig();
        return config;
    }

    /* Logging */

    /** Returns the position of the motor in rotations */
    @AutoLogOutput(key = "Shoulder/Motor Position (rotations)")
    public double getMotorPosition() {
        if (attached) {
            return motor.getPosition().getValueAsDouble();
        }
        return 0;
    }

    /** Returns the position of the motor as a percentage of max rotation */
    @AutoLogOutput(key = "Shoulder/Motor Position (percent)")
    public double getMotorPercentAngle() {
        if (attached) {
            return motor.getPosition().getValueAsDouble() / config.maxRotation * 100;
        }
        return 0;
    }

    /* Helper */
    public double percentToRotation(double percent) {
        return config.maxRotation * (percent / 100);
    }
}

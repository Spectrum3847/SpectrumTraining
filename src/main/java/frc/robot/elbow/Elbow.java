package frc.robot.elbow;

import edu.wpi.first.wpilibj2.command.Command;
import frc.spectrumLib.mechanism.Mechanism;
import frc.spectrumLib.mechanism.TalonFXFactory;
import org.littletonrobotics.junction.AutoLogOutput;

public class Elbow extends Mechanism {
    public class ElbowConfig extends Config {

        /* Elbow constants in rotations */
        public final double maxRotation = 30.8;
        public final double minRotation = -33.3;

        /* Elbow positions in percentage of max rotation || 0 is vertical */
        public final int intake = -96;
        public final int airIntake = -43;
        public final int shelfIntake = -95;

        public final int home = -45;
        public final int unblockCameraPos = -88;
        public final int stow = -29;

        public final int floor = -65;
        public final int coneTop = -80;
        public final double coneMid = coneTop;
        public final int prescore = -60;
        public final int cubeUp = -112;

        public final int scorePos = -106;

        public final double zeroSpeed = -0.2;

        /* Intake config values */
        public double currentLimit = 5;
        public double threshold = 5;
        public double velocityKp = 0.8;
        public double velocityKv = 0.013;
        public double velocityKs = 0;

        public ElbowConfig() {
            super("Elbow", 41, "rio");
            configPIDGains(0, velocityKp, 0, 0);
            configFeedForwardGains(velocityKs, velocityKv, 0, 0);
            configGearRatio(1); // TODO: check gear ratio
            configSupplyCurrentLimit(currentLimit, threshold, true);
            configNeutralBrakeMode(true);
            configClockwise_Positive();
            configReverseSoftLimit(minRotation, true);
            configForwardSoftLimit(maxRotation, true);
            configMotionMagic(51, 205, 0);
        }
    }

    public ElbowConfig config;

    public Elbow(boolean attached) {
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
        return run(() -> setMMPosition(percentToRotation(percent))).withName("Elbow.runPercent");
    }

    /**
     * Sets the intake motor to a specified position using FOC control. Will require different PID
     * and Motion Magic gains
     *
     * @param percent percentage of max rotation (0 is vertical). Note that the percentage is not
     *     [-1,1] but rather [-100,100]
     */
    public Command runFOCPosition(double percent) {
        return run(() -> setMMPositionFOC(percentToRotation(percent))).withName("Elbow.runPercent");
    }

    /**
     * Runs the intake at a specified percentage of its maximum output.
     *
     * @param percent The percentage of the maximum output to run the intake at.
     */
    public Command runManualOutput(double percent) {
        return run(() -> setPercentOutput(percent)).withName("Elbow.runPercentage");
    }

    public Command brakeMode() {
        return startEnd(() -> setBrakeMode(false), () -> setBrakeMode(true))
                .withName("Elbow.brakeMode")
                .ignoringDisable(true);
    }

    /**
     * Stops the intake motor.
     *
     * @return
     */
    public Command runStop() {
        return run(() -> stop()).withName("Elbow.stop");
    }

    /* Custom Commands */
    /** Holds the position of the elbow. */
    public Command runHoldElbow() { // TODO: review; inline custom commands vs. seperate class
        return new Command() {
            double holdPosition = 0; // rotations

            // constructor
            {
                setName("Elbow.holdPosition");
                addRequirements(Elbow.this);
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
        config = new ElbowConfig();
        return config;
    }

    /* Logging */

    /** Returns the position of the motor in rotations */
    @AutoLogOutput(key = "Elbow/Motor Position (rotations)")
    public double getMotorPosition() {
        if (attached) {
            return motor.getPosition().getValueAsDouble();
        }
        return 0;
    }

    /** Returns the position of the motor as a percentage of max rotation */
    @AutoLogOutput(key = "Elbow/Motor Position (percent)")
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

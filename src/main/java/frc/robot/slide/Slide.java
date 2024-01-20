package frc.robot.slide;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import frc.spectrumLib.mechanism.Mechanism;
import frc.spectrumLib.mechanism.TalonFXFactory;
import java.util.function.DoubleSupplier;
import org.littletonrobotics.junction.AutoLogOutput;

public class Slide extends Mechanism {
    public class SlideConfig extends Config {

        /* Slide constants in rotations */
        public final double maxHeight = 38.6; // absolute max: 39.1
        public final double minHeight = 0.29;

        /* Slide positions in rotations */
        public double fullExtend = maxHeight;
        public double home = minHeight;
        public double startingMotorPos = -0.15;

        /* Slide config settings */
        public final double zeroSpeed = -0.2;
        public final double positionKp = 0.86; // 20 FOC // 10 Regular
        public final double positionKv = 0.013; // .12 FOC // .15 regular
        public final double currentLimit = 30;
        public final double threshold = 30;

        public SlideConfig() {
            super("Slide", 43, "rio");
            configPIDGains(0, positionKp, 0, 0);
            configFeedForwardGains(0, positionKv, 0, 0);
            configMotionMagic(120, 195, 0); // 40, 120 FOC // 120, 195 Regular
            configSupplyCurrentLimit(currentLimit, threshold, true);
            configForwardSoftLimit(maxHeight, true);
            configReverseSoftLimit(minHeight, true);
            configNeutralBrakeMode(true);
            // configMotionMagicPosition(0.12);
            configClockwise_Positive();
        }
    }

    public SlideConfig config;

    public Slide(boolean attached) {
        super(attached);
        if (attached) {
            motor = TalonFXFactory.createConfigTalon(config.id, config.talonConfig);
        }
    }

    @Override
    public void periodic() {}

    /* Commands */

    /**
     * Runs the slide to the specified position.
     *
     * @param position position in revolutions
     */
    public Command runPosition(double position) {
        return run(() -> setMMPosition(position)).withName("Slide.runPosition");
    }

    /**
     * Runs the slide to the specified position using FOC control. Will require different PID and
     * feedforward configs
     *
     * @param position position in revolutions
     */
    public Command runFOCPosition(double position) {
        return run(() -> setMMPositionFOC(position)).withName("Slide.runFOCPosition");
    }

    /**
     * Runs the slide at a specified percentage of its maximum output.
     *
     * @param percent fractional units between -1 and +1
     */
    public Command runPercentage(double percent) {
        return run(() -> setPercentOutput(percent)).withName("Slide.runPercentage");
    }

    public Command runPercentage(DoubleSupplier percentSupplier) {
        return runPercentage(percentSupplier.getAsDouble());
    }

    // TODO: having commands in the slide class would mean you are using slide commands from
    // two different places
    public Command runStop() {
        return run(() -> stop()).withName("Slide.runStop");
    }

    /**
     * Temporarily sets the slide to coast mode. The configuration is applied when the command is
     * started and reverted when the command is ended.
     */
    public Command coastMode() {
        return startEnd(() -> setBrakeMode(false), () -> setBrakeMode(true))
                .ignoringDisable(true)
                .withName("Slide.coastMode");
        // the motor
    }

    /* Custom Commands */

    /** Holds the position of the slide. */
    public Command holdPosition() { // TODO: inline custom commands vs. seperate class
        return new Command() {
            double holdPosition = 0; // rotations

            // constructor
            {
                setName("Slide.holdPosition");
                addRequirements(Slide.this);
            }

            @Override
            public void initialize() {
                stop();
                holdPosition = motor.getPosition().getValueAsDouble();
            }

            @Override
            public void execute() {
                double currentPosition = motor.getPosition().getValueAsDouble();
                if (Math.abs(holdPosition - currentPosition) <= 5) {
                    setMMPosition(
                            holdPosition); // TODO: somehow change mode depending on current control
                } else {
                    DriverStation.reportError(
                            "SlideHoldPosition tried to go too far away from current position. Current Position: "
                                    + currentPosition
                                    + " || Hold Position: "
                                    + holdPosition,
                            false);
                }
            }

            @Override
            public void end(boolean interrupted) {
                stop();
            }
        };
    }

    // TODO: inline vs custom command
    public Command zeroSlideRoutine() {
        return new FunctionalCommand( //TODO: refresh config in order to modify soft limits
                        () ->
                                config.configReverseSoftLimit(
                                        config.talonConfig
                                                .SoftwareLimitSwitch
                                                .ReverseSoftLimitThreshold,
                                        false),
                        () -> setPercentOutput(config.zeroSpeed),
                        (b) -> {
                            zeroMotor();
                            config.configReverseSoftLimit(
                                    config.talonConfig
                                            .SoftwareLimitSwitch
                                            .ReverseSoftLimitThreshold,
                                    true);
                        },
                        () -> false,
                        this)
                .withName("Slide.zeroSlideRoutine");
    }

    @Override
    protected Config setConfig() {
        config = new SlideConfig();
        return config;
    }

    @AutoLogOutput(key = "Slide/Position (rotations)")
    public double getMotorPosition() {
        return motor.getPosition().getValueAsDouble();
    }
}

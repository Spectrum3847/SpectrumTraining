package frc.robot.slide;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import frc.spectrumLib.mechanism.Mechanism;
import frc.spectrumLib.mechanism.TalonFXFactory;

// TODO: was slide using voltage compensation? afaik it wasn't
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
        public final double positionKp = 0.86;
        public final double positionKv = 0.012813; // TODO: this value could be 0
        public final double currentLimit = 20;

        public SlideConfig() {
            super("Slide", 43, "rio");
            configPIDGains(0, positionKp, 0, 0);
            configFeedForwardGains(0, positionKv, 0, 0);
            configMotionMagic(120, 195, 50); // TODO: configure jerk
            configSupplyCurrentLimit(currentLimit, true);
            configForwardSoftLimit(maxHeight, true);
            configReverseSoftLimit(minHeight, true);
            configClockwise_Positive();
            // TODO: config nominal output?
        }
    }

    public SlideConfig config;

    public Slide(boolean attached) {
        super(attached);
        if (attached) {
            motor = TalonFXFactory.createConfigTalon(config.id, config.talonConfig);
            setMotorPosition(config.startingMotorPos);
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
        return run(() -> setMMPositionFOC(position)).withName("Slide.runPosition");
    }

    /**
     * Runs the slide at a specified percentage of its maximum output.
     *
     * @param percent fractional units between -1 and +1
     */
    public Command runPercentage(double percent) {
        return run(() -> setPercentOutput(percent)).withName("Slide.runPercentage");
    }

    // TODO: review: having commands in the slide class would mean you are using slide commands from
    // two different places
    public Command runStop() {
        return run(() -> stop()).withName("Slide.runStop");
    }

    /**
     * Temporarily sets the slide to coast mode. The configuration is applied when the command is
     * started and reverted when the command is ended.
     */
    public Command coastMode() {
        return startEnd(
                        () -> config.configNeutralBrakeMode(false),
                        () -> config.configNeutralBrakeMode(true))
                .ignoringDisable(true)
                .withName("Slide.coastMode"); // TODO: test; the config may have to be reapplied to
        // the motor
    }

    /* Custom Commands */

    /** Holds the position of the slide. */
    public Command holdPosition() { // TODO: review; inline custom commands vs. seperate class
        return new Command() {
            double holdPosition = 0; // rotations

            // constructor
            {
                setName("Slide.holdPosition");
                addRequirements(Slide.this);
            }

            @Override
            public void initialize() {
                holdPosition = getMotorPosition();
            }

            @Override
            public void execute() {
                double currentPosition = getMotorPosition();
                if (Math.abs(holdPosition - currentPosition) <= 5000) {
                    setMMPositionFOC(holdPosition);
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

    // TODO: review; inline looks really nasty
    public Command zeroSlideRoutine() {
        return new FunctionalCommand(
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
}

package frc.robot.elbow;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import frc.robot.elbow.Elbow;
import frc.spectrumLib.mechanism.Mechanism;
import frc.spectrumLib.mechanism.TalonFXFactory;

public class Elbow extends Mechanism{

    public class ElbowConfig extends Config {

        /* Elbow constants in rotations */
        public double maxHeight = 30.88671875; // idk
        public double minHeight = 0.29;

        /* Elbow positions in rotations */
        public double fullExtend = maxHeight;
        public double startingMotorPos = -0.15;

        //Elbow positions
        public double positionKp = 0.080078;
        public double positionKv = 0.012812;
        public double currentLimit = 5.0;

        // Positions set as percentage of
        // 0 is vertical
        public int initializedPosition = 90;

        public int intake = -96;                         
        public int airIntake = -43;
        public int shelfIntake = -95;

        public int home = -45; // -25
        public int unblockCameraPos = -88;
        public int stow = -29;

        public int floor = -65;
        public int coneTop = -80;
        public double coneMid = coneTop;
        public int prescore = -60;
        public int cubeUp = -112; // -108

        public int scorePos = -106;

        public int throwBack = 0;
        public int throwFwd = -50;
        // public final int cubeScorePos = cubeUp - 2;

        public double zeroSpeed = -0.2;

        // Physical Constants
        public double gearRatio = 1; // TODO: change; not actually used though
        public ElbowConfig() {
            super("Elbow", 41, "rio");
            configPIDGains(0, positionKp, 0, 0);
            configFeedForwardGains(0, positionKv, 0, 0);
            configMotionMagic(51.26953125, 205.078125, 50); // TODO: configure jerk
            configSupplyCurrentLimit(currentLimit, true);
            configForwardSoftLimit(maxHeight, true);
            configReverseSoftLimit(minHeight, true);
            configClockwise_Positive();
        }
    }

    public ElbowConfig config;

    public Elbow(boolean attached) {
        super(attached);
        if (attached) {
            motor = TalonFXFactory.createConfigTalon(config.id, config.talonConfig);
            setMotorPosition(config.startingMotorPos);
        }
    }

    @Override
    public void periodic() {}
    
    public Command runPercentage(double percent) {
        return run(() -> setPercentOutput(percent)).withName("Elbow.runPercentage");
    }

    public Command runPosition(double position) {
        return run(() -> setMMPositionFOC(position)).withName("Slide.runPosition");
    }

    public Command runStop() {
        return run(() -> stop()).withName("Elbow.runStop");
    }

    public double percentToRot(double percent) {
        return percent/100 * 30.88671875;
    }

    public Command coastMode() {
        return startEnd(
                        () -> config.configNeutralBrakeMode(false),
                        () -> config.configNeutralBrakeMode(true))
                .ignoringDisable(true)
                .withName("Elbow.coastMode");
    }

    public Command holdPosition() { 
        return new Command() {
            double holdPosition = 0;

            {
                setName("Elbow.holdPosition");
                addRequirements(Elbow.this);
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
                            "ElbowHoldPosition tried to go tebloo far away from current position. Current Position: "
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

    public Command zeroElbowRoutine() {
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
                .withName("Elbow.zeroElbowRoutine");
    }

    @Override
    protected Config setConfig() {
        config = new ElbowConfig();
        return config;
    }
}


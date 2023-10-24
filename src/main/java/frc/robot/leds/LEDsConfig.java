package frc.robot.leds;

public class LEDsConfig {
    public static final int port = 0;
    public static final int length = 43;
    public static final int staticLength = 14;
    public static final int staticSectionLength = 3;
    public static final double strobeFastDuration = 0.1;
    public static final double strobeSlowDuration = 0.2;
    public static final double breathDuration = 1.0;
    public static final double rainbowCycleLength = 25.0;
    public static final double rainbowDuration = 0.25;
    public static final double waveExponent = 0.4;
    public static final double waveFastCycleLength = 25.0;
    public static final double waveFastDuration = 0.25;
    public static final double waveSlowCycleLength = 25.0;
    public static final double waveSlowDuration = 3.0;
    public static final double waveAllianceCycleLength = 15.0;
    public static final double waveAllianceDuration = 2.0;
    public static final double autoFadeTime = 2.5; // 3s nominal
    public static final double autoFadeMaxTime = 5.0; // Return to normal

    public enum Section {
        STATIC,
        FULL,
        STATIC_LOW,
        STATIC_MID,
        STATIC_HIGH;

        public int start() {
            switch (this) {
                case STATIC:
                    return 0;
                case FULL:
                    return 0;
                case STATIC_LOW:
                    return 0;
                case STATIC_MID:
                    return staticSectionLength;
                case STATIC_HIGH:
                    return staticLength - staticSectionLength;
                default:
                    return 0;
            }
        }

        public int end() {
            switch (this) {
                case STATIC:
                    return staticLength;
                case FULL:
                    return length;
                case STATIC_LOW:
                    return staticSectionLength;
                case STATIC_MID:
                    return staticLength - staticSectionLength;
                case STATIC_HIGH:
                    return staticLength;
                default:
                    return length;
            }
        }
    }
}

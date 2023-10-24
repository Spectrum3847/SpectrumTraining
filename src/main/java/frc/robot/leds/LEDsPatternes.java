package frc.robot.leds;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.util.Color;
import frc.robot.Robot;
import frc.robot.leds.LEDsConfig.Section;
import java.util.List;

public class LEDsPatternes {
    // Many of these were borrowed from 6328-2023 code
    public static void solid(Section section, Color color) {
        if (color != null) {
            for (int i = section.start(); i < section.end(); i++) {
                Robot.leds.setLED(i, color);
            }
        }
    }

    /**
     * Sets a percentage of the LEDs to a color
     *
     * @param percent The percentage of LEDs to set
     * @param color The color to set the LEDs to
     */
    public static void solid(double percent, Color color) {
        for (int i = 0;
                i < MathUtil.clamp(LEDsConfig.length * percent, 0, LEDsConfig.length);
                i++) {
            Robot.leds.setLED(i, color);
        }
    }

    /**
     * Set a section of the LEDs to strobe
     *
     * @param section The section of the LEDs to strobe
     * @param color The color to strobe
     * @param duration The duration of the strobe
     */
    public static void strobe(Section section, Color color, double duration) {
        boolean on = ((Timer.getFPGATimestamp() % duration) / duration) > 0.5;
        solid(section, on ? color : Color.kBlack);
    }

    public static void breath(Section section, Color c1, Color c2, double duration) {
        breath(section, c1, c2, duration, Timer.getFPGATimestamp());
    }

    public static void breath(
            Section section, Color c1, Color c2, double duration, double timestamp) {
        double x =
                ((timestamp % LEDsConfig.breathDuration) / LEDsConfig.breathDuration)
                        * 2.0
                        * Math.PI;
        double ratio = (Math.sin(x) + 1.0) / 2.0;
        double red = (c1.red * (1 - ratio)) + (c2.red * ratio);
        double green = (c1.green * (1 - ratio)) + (c2.green * ratio);
        double blue = (c1.blue * (1 - ratio)) + (c2.blue * ratio);
        solid(section, new Color(red, green, blue));
    }

    /**
     * Sets the LEDs to a rainbow pattern
     *
     * @param section The section of the LEDs to set
     * @param cycleLength The length of the rainbow cycle in LEDs
     * @param duration The duration of the rainbow
     */
    public static void rainbow(Section section, double cycleLength, double duration) {
        double x = (1 - ((Timer.getFPGATimestamp() / duration) % 1.0)) * 180.0;
        double xDiffPerLed = 180.0 / cycleLength;
        for (int i = 0; i < section.end(); i++) {
            x += xDiffPerLed;
            x %= 180.0;
            if (i >= section.start()) {
                Robot.leds.setHSV(i, (int) x, 255, 255);
            }
        }
    }

    /**
     * Sets the LEDs to a wave pattern
     *
     * @param section The section of the LEDs to set
     * @param c1 The first color of the wave
     * @param c2 The second color of the wave
     * @param cycleLength The length of the wave cycle in LEDs
     * @param duration The duration of the wave
     */
    public static void wave(
            Section section, Color c1, Color c2, double cycleLength, double duration) {
        double x = (1 - ((Timer.getFPGATimestamp() % duration) / duration)) * 2.0 * Math.PI;
        double xDiffPerLed = (2.0 * Math.PI) / cycleLength;
        for (int i = 0; i < section.end(); i++) {
            x += xDiffPerLed;
            if (i >= section.start()) {
                double ratio = (Math.pow(Math.sin(x), LEDsConfig.waveExponent) + 1.0) / 2.0;
                if (Double.isNaN(ratio)) {
                    ratio = (-Math.pow(Math.sin(x + Math.PI), LEDsConfig.waveExponent) + 1.0) / 2.0;
                }
                if (Double.isNaN(ratio)) {
                    ratio = 0.5;
                }
                double red = (c1.red * (1 - ratio)) + (c2.red * ratio);
                double green = (c1.green * (1 - ratio)) + (c2.green * ratio);
                double blue = (c1.blue * (1 - ratio)) + (c2.blue * ratio);
                Robot.leds.setLED(i, new Color(red, green, blue));
            }
        }
    }

    public static void stripes(Section section, List<Color> colors, int length, double duration) {
        int offset =
                (int) (Timer.getFPGATimestamp() % duration / duration * length * colors.size());
        for (int i = section.start(); i < section.end(); i++) {
            int colorIndex =
                    (int) (Math.floor((double) (i - offset) / length) + colors.size())
                            % colors.size();
            colorIndex = colors.size() - 1 - colorIndex;
            Robot.leds.setLED(i, colors.get(colorIndex));
        }
    }
}

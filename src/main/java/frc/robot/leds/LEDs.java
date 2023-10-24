package frc.robot.leds;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.leds.commands.LEDsCommands;

public class LEDs extends SubsystemBase {
    public LEDsConfig config;
    private int priority = 0;
    // LED IO
    private final AddressableLED leds;
    private final AddressableLEDBuffer buffer;
    private Runnable currentPattern = () -> {}; // Start with blank pattern

    public LEDs() {
        config = new LEDsConfig();
        leds = new AddressableLED(LEDsConfig.port);
        leds.setLength(LEDsConfig.length);
        buffer = new AddressableLEDBuffer(LEDsConfig.length);
        leds.setData(buffer);
        leds.start();
    }

    public void periodic() {
        // Run the current LED command
        currentPattern.run();
        // Update the LEDs
        leds.setData(buffer);
    }

    public void setPriority(int p) {
        priority = p;
    }

    public void resetDefault() {
        setPriority(0);
        if (LEDsCommands.defaultCommand() != null) {
            currentPattern = () -> LEDsCommands.defaultCommand().execute();
        } else {
            currentPattern = () -> {}; // If there is no default command, do nothing
        }
    }

    public boolean setPatternIfHigherPriority(Runnable r, int p) {
        if (p >= priority) {
            setPriority(p);
            currentPattern = r;
            return true;
        } else {
            return false;
        }
    }

    public void setLED(int i, Color c) {
        buffer.setLED(i, c);
    }

    public void setLED(int i, int r, int g, int b) {
        buffer.setLED(i, new Color(r, g, b));
    }

    public void setHSV(int i, int h, int s, int v) {
        buffer.setHSV(i, h, s, v);
    }
}

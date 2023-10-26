package frc.spectrumLib.leds;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class SpectrumLEDs extends SubsystemBase {
    protected int priority = 0;
    // LED IO
    private AddressableLED leds;
    private SpectrumLEDBuffer buffer;
    protected Runnable defaultPattern = () -> {}; // Start with blank pattern

    public SpectrumLEDs(int port, int length) {
        leds = new AddressableLED(port);
        leds.setLength(length);
        buffer = new SpectrumLEDBuffer(length);
        leds.setData(buffer.getLEDBuffer());
        leds.start();
    }

    /**
     * Allows to reconfigure the LEDs after boot. This can be helpful if different robot
     * configurations have different LEDs
     */
    public void setLEDsPortLength(int port, int length) {
        leds.stop();
        leds.close();
        leds = new AddressableLED(port);
        leds.setLength(length);
        buffer = new SpectrumLEDBuffer(length);
        leds.start();
    }

    public void periodic() {
        leds.setData(buffer.getLEDBuffer());
    }

    public void resetPriority() {
        buffer.resetPriorityBuffer();
    }

    public void setLED(int i, Color c, int priority) {
        buffer.setLED(i, c, priority);
    }

    public void setLED(int i, int r, int g, int b, int priority) {
        buffer.setLED(i, new Color(r, g, b), priority);
    }

    public void setHSV(int i, int h, int s, int v, int priority) {
        buffer.setHSV(i, h, s, v, priority);
    }
}

package frc.spectrumLib.mechanism;

import com.ctre.phoenix6.hardware.TalonFX;
import frc.spectrumLib.util.CanDeviceId;

public class Mechanism {
    private TalonFX motor;

    public Mechanism() {
        motor =
                TalonFXFactory.start()
                        .withSupplyCurrentLimit(0, false)
                        .createNew(new CanDeviceId(0));
    }
}

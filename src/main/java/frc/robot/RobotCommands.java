package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.leds.commands.LEDsCommands;
import frc.robot.training.commands.TrainingCommands;

/**
 * This class is used for commands that use multiple subsystems and don't directly call a gamepad.
 * This is often command groups such as moving an arm and turning on an intake, etc. In 2023 we
 * called this MechanismCommands.java
 */
public class RobotCommands {

    public static Command PrintAndBreathLED() {
        return TrainingCommands.printOnceCommand("BreathBlueLED")
                .alongWith(LEDsCommands.breathBlueLED().ignoringDisable(true));
    }
}

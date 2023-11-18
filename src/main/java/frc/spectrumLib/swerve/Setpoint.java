package frc.spectrumLib.swerve;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;

public class Setpoint {
    public ChassisSpeeds chassisSpeeds;
    public SwerveModuleState[] moduleStates;

    public Setpoint(ChassisSpeeds chassisSpeeds, SwerveModuleState[] states) {
        this.chassisSpeeds = chassisSpeeds;
        this.moduleStates = states;
    }
}

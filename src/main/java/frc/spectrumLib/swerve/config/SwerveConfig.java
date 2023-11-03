package frc.spectrumLib.swerve.config;

public class SwerveConfig {
    /** CAN ID of the Pigeon2 on the drivetrain */
    public int Pigeon2Id = 0;
    /** Name of CANivore the swerve drive is on */
    public String CANbusName = "rio";

    /** If using Pro, specify this as true to make use of all the Pro features */
    public boolean SupportsPro = false;

    public ModuleConfig[] modules = new ModuleConfig[0];

    public SwerveConfig withPigeon2Id(int id) {
        this.Pigeon2Id = id;
        return this;
    }

    public SwerveConfig withCANbusName(String name) {
        this.CANbusName = name;
        return this;
    }

    public SwerveConfig withSupportsPro(boolean supportsPro) {
        this.SupportsPro = supportsPro;
        return this;
    }

    public SwerveConfig withModules(ModuleConfig[] modules) {
        this.modules = modules;
        return this;
    }
}

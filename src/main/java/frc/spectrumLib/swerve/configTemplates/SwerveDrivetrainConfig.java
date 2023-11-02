package frc.spectrumLib.swerve;

public class SwerveDrivetrainConstants {
    /** CAN ID of the Pigeon2 on the drivetrain */
    public int Pigeon2Id = 0;
    /** Name of CANivore the swerve drive is on */
    public String CANbusName = "rio";

    /** If using Pro, specify this as true to make use of all the Pro features */
    public boolean SupportsPro = false;

    public SwerveDrivetrainConstants withPigeon2Id(int id) {
        this.Pigeon2Id = id;
        return this;
    }

    public SwerveDrivetrainConstants withCANbusName(String name) {
        this.CANbusName = name;
        return this;
    }

    public SwerveDrivetrainConstants withSupportsPro(boolean supportsPro) {
        this.SupportsPro = supportsPro;
        return this;
    }
}

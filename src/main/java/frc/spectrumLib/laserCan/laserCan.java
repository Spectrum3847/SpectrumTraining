package frc.spectrumLib.laserCan;

import au.grapplerobotics.LaserCan;

public class laserCan {
    private LaserCan lasercan;
    public LaserCan.Measurement measurement;


    // default constructor
    public laserCan(int id) {
        lasercan = new LaserCan(id);
        setShortRange();
        setRegionOfInterest(8, 8, 16, 16); // max region
        setTimingBudget(33); // Can only set ms to 20, 33, 50, and 100
    }

    public laserCan(int id, boolean shortRange, int x, int y, int w, int h, int ms) {
        lasercan = new LaserCan(id);
        if (shortRange = true) {
            setShortRange();
        } else {
            setLongRange();
        }
        setRegionOfInterest(x, y, w, h); // max region
        setTimingBudget(ms); // Can only set ms to 20, 33, 50, and 100
    }


    /* Helper methods for constructors */

    public void setShortRange() {
        lasercan.setRangingMode(LaserCan.RangingMode.SHORT);
    }

    public void setLongRange() {
        lasercan.setRangingMode(LaserCan.RangingMode.LONG);
    }

    public void setRegionOfInterest(int x, int y, int w, int h) {
        lasercan.setRegionOfInterest(new LaserCan.RegionOfInterest(x, y, w, h));
    }

    public void setTimingBudget(int ms) {
        switch(ms) {
            case 20:
                lasercan.setTimingBudget(LaserCan.TimingBudget.TIMING_BUDGET_20MS);
                break;
            case 33:
                lasercan.setTimingBudget(LaserCan.TimingBudget.TIMING_BUDGET_33MS);
                break;
            case 50:
                lasercan.setTimingBudget(LaserCan.TimingBudget.TIMING_BUDGET_50MS);
                break;
            case 100:
                lasercan.setTimingBudget(LaserCan.TimingBudget.TIMING_BUDGET_100MS);
                break;
        }
    }


    /* Other methods */

    public LaserCan.Measurement getMeasurement() {
        return lasercan.getMeasurement();
    }

    public int getDistance() {
        measurement = getMeasurement();
        if (measurement != null) {
            return measurement.distance_mm;
        } else {
            return 0;
        }
    }

    public int getStatus() {
        measurement = getMeasurement();
        return measurement.status;
    }
}

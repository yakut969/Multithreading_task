package epamlab.beans;


import epamlab.threads.Bus;

import java.util.List;

public class Station {

    public final Object lock = new Object();

    private int number;
    private Bus currentBus;
    private volatile int passengersCount;
    private boolean isBusArrived;
    private List<Station> stations;

    public Station(int number) {
        this.number = number;
        this.isBusArrived = false;
        this.currentBus = null;
        this.passengersCount = 0;
    }

    public int getNumber() {
        return number;
    }

    public boolean isBusArrived() {
        return isBusArrived;
    }

    public void setBusArrived(boolean busArrived) {
        isBusArrived = busArrived;
    }

    public Bus getCurrentBus() {
        return currentBus;
    }

    public void setCurrentBus(Bus currentBus) {
        this.currentBus = currentBus;
    }

    public int getPassengersCount() {
        return passengersCount;
    }

    public void setPassengersCount(int passengersCount) {
        this.passengersCount = passengersCount;
    }

    public boolean takingBusMonitor(List<Station> stations, Bus currentBus) {
        this.currentBus = currentBus;
        if (this.currentBus.isAnyEmptySeats() && passengersCount > 0) {
            this.stations = stations;
            isBusArrived = true;
            return true;
        }
        return false;
    }

    public void releaseBusMonitor() {
        stations = null;
        isBusArrived = false;
        synchronized (this.lock) {
            this.lock.notifyAll();
        }
    }

    public boolean boarding() {
        if (!currentBus.isAnyEmptySeats()) {
            releaseBusMonitor();
            return false;
        }
        currentBus.setOccupancy(currentBus.getOccupancy() + 1);
        passengersCount--;
        if (passengersCount <= 0) {
            releaseBusMonitor();
        }
        return true;
    }

    synchronized public void passengerIn() {
        passengersCount++;
    }

    synchronized public void passengerOut() {
        passengersCount--;
    }

    public boolean isPassengersOnStation() {
        return passengersCount > 0;
    }

}

package epamlab.threads;

import epamlab.beans.Route;
import epamlab.beans.Station;
import org.apache.log4j.Logger;

import static epamlab.constants.Constants.*;


public class Bus implements Runnable {

    public final Object lock = new Object();
    private static final Logger log = Logger.getLogger(Bus.class);

    private String name;
    private Route route;
    private int capacity;
    private volatile int occupancy;
    private int movementSpeed;
    private Station currentStation;
    private boolean isTransportationComplete;

    @Override
    public void run() {

        while (!isTransportationComplete) {
            synchronized (this.lock) {
                try {
                    Thread.sleep(movementSpeed * MULTIPLIER);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            synchronized (currentStation.lock) {
                while (currentStation.isBusArrived()) {
                    try {
                        currentStation.lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.warn(name + " arrived at station-" + currentStation.getNumber());
                currentStation.lock.notifyAll();

                if (currentStation.takingBusMonitor(this)) {
                    try {
                        currentStation.lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            //occupancy = capacity - getEmptySeats();
            int nextStation = route.getStations().indexOf(currentStation) + 1;
            if (nextStation >= route.getStations().size()) {
                if (Route.hasPassengers() && occupancy > 0) {
                    currentStation = route.getStations().get(0);
                } else {
                    isTransportationComplete = true;
                }

            } else {
                currentStation = route.getStations().get(nextStation);
            }
        }
        log.warn(name + " completed transportation.");

    }

    public Bus() {
    }

    public Bus(String name, int capacity, int movementSpeed, Route route) {
        this.route = route;
        this.name = name;
        this.capacity = capacity;
        this.movementSpeed = movementSpeed;
        this.occupancy = 0;
        this.currentStation = route.getStations().get(0);
        this.isTransportationComplete = false;
    }

    public boolean isAnyEmptySeats() {
        return (this.capacity - this.occupancy) > 0;
    }

    public int getEmptySeats() {
        return capacity - occupancy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getOccupancy() {
        return occupancy;
    }

    public void setOccupancy(int occupancy) {
        this.occupancy = occupancy;
    }
    
    
}

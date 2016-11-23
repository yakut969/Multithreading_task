package epamlab.threads;

import epamlab.beans.Station;
import org.apache.log4j.Logger;


public class Passenger implements Runnable {

    private static final Logger log = Logger.getLogger(Passenger.class);

    private String name;
    private Station departureStation;
    private Station currentStation;
    private Station arrivalStation;
    private boolean inBus;

    @Override
    public void run() {

        departureStation.passengerIn();
        log.warn(name + " on departure station" + departureStation.getNumber());
        while (!inBus) {
            synchronized (departureStation.lock) {
                while (!departureStation.isBusArrived()) {
                    try {
                        departureStation.lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (currentStation.boarding()) {
                    inBus = true;
                    log.warn(name + " in bus");
                }
            }
        }
        while (inBus) {
            synchronized (arrivalStation.lock) {
                try {
                    arrivalStation.lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            inBus = false;
        }
        arrivalStation.landing();
        log.warn(name + " on arrival station" + arrivalStation.getNumber());

    }

    public Passenger() {
    }

    public Passenger(String name, Station departureStation, Station arrivalStation) {
        this.name = name;
        this.departureStation = departureStation;
        this.currentStation = departureStation;
        this.arrivalStation = arrivalStation;
        this.inBus = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Station getDepartureStation() {
        return departureStation;
    }

    public void setDepartureStation(Station departureStation) {
        this.departureStation = departureStation;
    }

    public Station getCurrentStation() {
        return currentStation;
    }

    public void setCurrentStation(Station currentStation) {
        this.currentStation = currentStation;
    }

    public Station getArrivalStation() {
        return arrivalStation;
    }

    public void setArrivalStation(Station arrivalStation) {
        this.arrivalStation = arrivalStation;
    }

}

package epamlab.controller;

import epamlab.beans.Route;
import epamlab.threads.Bus;
import epamlab.threads.Passenger;

import static epamlab.constants.Constants.*;

public class MainController implements Runnable{

    private int busCapacity;
    private int passengerCount;
    private int stationsCount;
    private int busCount;
    private int movementInterval;
    private int movementSpeed;
    private Route route;

    private static MainController controller;

    @Override
    public void run() {

        route = new Route(stationsCount);

        generatePassengers();

        for (int i=0;i<busCount;i++){

            Bus bus = new Bus("Bus" + i, busCapacity, movementSpeed, route);
            Thread busThread = new Thread(bus);

            busThread.start();

            try {
                Thread.sleep(movementInterval*MULTIPLIER);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }


    }

    private MainController(int busCapacity, int passengerCount, int stationsCount, int busCount, int movementInterval, int movementSpeed) {
        this.busCapacity = busCapacity;
        this.passengerCount = passengerCount;
        this.stationsCount = stationsCount;
        this.busCount = busCount;
        this.movementInterval = movementInterval;
        this.movementSpeed = movementSpeed;
    }

    public static MainController getController(int busCapacity, int passengerCount, int stationsCount, int busCount, int movementInterval, int movementSpeed) {
        if (controller == null){
            return new MainController(busCapacity, passengerCount, stationsCount, busCount, movementInterval, movementSpeed);
        } else {
            return controller;
        }
    }

    private void generatePassengers(){

        for (int i=0;i<passengerCount;i++){
            int firstStationNumber = 0;
            int lastStationNumber = stationsCount * 2 - 1;
            int departureStation = 0;
            int arrivalStation = 0;

            departureStation = firstStationNumber + (int)(Math.random() * ((lastStationNumber - firstStationNumber) + 1));
            do {
                arrivalStation = firstStationNumber + (int)(Math.random() * ((lastStationNumber - firstStationNumber) + 1));
            } while (arrivalStation == departureStation);

            Passenger passenger = new Passenger("Passenger" + i, route.getStations().get(departureStation), route.getStations().get(arrivalStation));
            Thread passengerThread = new Thread(passenger);
            passengerThread.start();

        }

    }

}

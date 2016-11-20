package epamlab;

import epamlab.controller.MainController;
import org.apache.log4j.Logger;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import static epamlab.constants.Constants.*;


public class Runner {

    private static final Logger log = Logger.getLogger(Runner.class);

    public static void main(String[] args) {

        try {
            ResourceBundle rb = ResourceBundle.getBundle(INITIAL_PARAMETERS);

            int passengers = Integer.parseInt(rb.getString(PASSENGERS));
            int busesCount = Integer.parseInt(rb.getString(BUSES_COUNT));
            int speed = Integer.parseInt(rb.getString(SPEED));
            int stationsCount = Integer.parseInt(rb.getString(STATIONS_COUNT));
            int interval = Integer.parseInt(rb.getString(INTERVAL));
            int busCapacity = Integer.parseInt(rb.getString(BUS_CAPACITY));

            MainController controller = MainController.getController(busCapacity, passengers, stationsCount, busesCount, interval, speed);

            Thread controllerThread = new Thread(controller);
            log.warn("System starting");
            controllerThread.start();



        } catch (MissingResourceException e) {
            System.out.println(RESOURCE_PROBLEM);
        }

    }

}

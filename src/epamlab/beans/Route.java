package epamlab.beans;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Route {

    private static List<Station> stations = new ArrayList<>();
    private int stationsCount;

    public Route(int stationsCount) {

        this.stationsCount = stationsCount * 2;

        for (int i=0;i<this.stationsCount;i++){
            stations.add(new Station(i));
        }

    }

    public static boolean hasPassengers() {
        boolean hasPassengers = false;
        Iterator<Station> stationIterator = stations.iterator();
        while (stationIterator.hasNext() && !hasPassengers) {
            hasPassengers = stationIterator.next().isPassengersOnStation();
        }
        return hasPassengers;
    }


    public List<Station> getStations() {
        return stations;
    }
}

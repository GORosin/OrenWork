package server.sortingAlgorithms.comparators;

import common.models.FlightPlan;

import java.util.Comparator;

public class AirfareComparator implements Comparator<FlightPlan> {
    public int compare(FlightPlan flightPlan1, FlightPlan flightPlan2) {
        double flightPlan1Price = flightPlan1.getPrice();
        double flightPlan2Price = flightPlan2.getPrice();

        return (int) (flightPlan1Price - flightPlan2Price);
    }
}

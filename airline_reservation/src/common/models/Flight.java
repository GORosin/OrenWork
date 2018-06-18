package common.models;

import java.util.ArrayList;
import java.util.List;

public class Flight implements FlightPlan {
    private String originAirport;
    private String destinationAirport;
    private String arrivalTime;
    private String departureTime;
    private int flightNumber;
    private double price;
    private int currentDelayTime;
    private int minConnectionTime;

    public Flight(String originAirport, String destinationAirport, String arrivalTime, String departureTime,
                  int flightNumber, double price, int currentDelayTime, int minConnectionTime) {
        this.originAirport = originAirport;
        this.destinationAirport = destinationAirport;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
        this.flightNumber = flightNumber;
        this.price = price;
        this.currentDelayTime = currentDelayTime;
        this.minConnectionTime = minConnectionTime;
    }

    public String getOriginAirport() {
        return originAirport;
    }

    public String getDestinationAirport() {
        return destinationAirport;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public int getMinConnectionTime() {
        return minConnectionTime;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public int getFlightNumber() {
        return flightNumber;
    }

    public double getPrice() {
        return price;
    }

    public int getCurrentDelayTime() {
        return currentDelayTime;
    }

    public List<Flight> getFlights() {
        List arbitraryList = new ArrayList();
        arbitraryList.add(this);

        return arbitraryList;
    }

    @Override
    public String toString() {
        return "Flight " + this.flightNumber + " from: " + this.originAirport + " to: " + this.destinationAirport + " | Price: " + this.price + "";
    }
}

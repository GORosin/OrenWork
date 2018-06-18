package common.models;

import java.util.List;

public class Itinerary implements FlightPlan {
    private List<Flight> listOfFlights;
    private double price;

    public Itinerary(List<Flight> listOfFlights) {
        this.listOfFlights = listOfFlights;
        this.price = this.getPrice();
    }

    public void addFlight(Flight newFlight) {
        this.listOfFlights.add(newFlight);
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<Flight> getFlights() {
        return this.listOfFlights;
    }

    public double getPrice() {
        double price = 0;

        for (Flight flight : this.listOfFlights) {
            price = price + flight.getPrice();
        }

        return price;
    }

    @Override
    public String toString() {
        String output = "Flights ";
        for (int i = 0; i < this.listOfFlights.size(); i++) {
            output += this.listOfFlights.get(i).getFlightNumber();
            if (this.listOfFlights.size() == 2 && i == 0) {
                output += " & ";
            } else if (this.listOfFlights.size() > 1 && i != this.listOfFlights.size() - 1) {
                output += ", ";
            }
        }
        return output + " | Price: " + this.price + "";
    }
}

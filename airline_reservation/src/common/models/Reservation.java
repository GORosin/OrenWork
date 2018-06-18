package common.models;

public class Reservation {
    private FlightPlan flightPlan;
    private String customer;
    private String originAirportCode;
    private String destinationAirportCode;

    // create the reservation and then add it to the reservation server.data structure held in the Query Data Handler
    public Reservation(FlightPlan flightPlan, String customer, String origin, String dest) {
        this.flightPlan = flightPlan;
        this.customer = customer;
        this.originAirportCode = origin;
        this.destinationAirportCode = dest;
    }

    public FlightPlan getFlightPlan() {
        return flightPlan;
    }

    public void setFlightPlan(FlightPlan flightPlan) {
        this.flightPlan = flightPlan;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getOriginAirportCode() {
        return originAirportCode;
    }

    public String getDestinationAirportCode() {
        return destinationAirportCode;
    }

    @Override
    public String toString() {
        return "Customer: " + this.customer + " | Origin: " + this.originAirportCode + " | Destination: " + this.destinationAirportCode + " | " + this.flightPlan;
    }
}

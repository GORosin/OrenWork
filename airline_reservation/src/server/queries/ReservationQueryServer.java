package server.queries;

import server.dataHandlers.DataHandler;
import common.models.Reservation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ReservationQueryServer {
    private String passenger;
    private List<Reservation> reservations;

    public ReservationQueryServer(DataHandler dataHandler, String passenger) {
        this.passenger = passenger;
        this.reservations = dataHandler.getReservations();

    }

    /**
     * Makes a query given nothing but the passenger and returns a list of all reservations for that passenger
     *
     * @return List of reservations for the given passenger
     */
    public List<Reservation> queryPassenger() {
        List<Reservation> queriedList = new ArrayList<>();
        for (Reservation reservation : this.reservations) {
            if (Objects.equals(reservation.getCustomer(), this.passenger)) {
                queriedList.add(reservation);
            }
        }
        return queriedList;
    }

    /**
     * Starting with a list given the passenger from the contructor,
     * this method will reduce the query down to only reservations for the Origin and Destination Airports
     *
     * @param orgAirport String of the origin airport code
     * @param desAirport String of the destination airport code
     * @return Returns a list of all Reservations matching customer, origin, and destination
     */
    public List<Reservation> queryLocations(String orgAirport, String desAirport) {
        List<Reservation> matchedList = queryPassenger();
        List<Reservation> returnList = new ArrayList<Reservation>();
        for (Reservation reservation : matchedList) {
            if (reservation.getOriginAirportCode().equals(orgAirport)
                    && reservation.getDestinationAirportCode().equals(desAirport)) {
                returnList.add(reservation);
            }
        }
        return returnList;
    }
}

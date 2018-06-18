package server.dataHandlers;

import common.models.*;
import server.dataStates.FAAState;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class DataHandler {
    private static int state;
    private static FAAState faaState;
    private static Hashtable<String, Airport> FAAAirports;
    private static Hashtable<String, Airport> airports = new Hashtable<>();
    private static List<Reservation> reservations = new ArrayList<>();
    private static List<Flight> flights = new ArrayList<>();
    private static String[] airportCodes = {"ATL", "BOS", "DFW", "IAD", "JFK",
            "LAS", "LAX", "MCO", "ORD", "PHX", "ROC", "SEA", "SFO"};
    private static FileHandler fileHandler;

    public DataHandler(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

    /**
     * Given each airport code, crawl through the server.data "buffer" objects and generate an Airport object for each
     * airport code in the system. This method grabs server.data from several different server.data structures in order to create
     * each individual airport.
     */
    public void createAirports() {
        if (state == 0) {
            // iterate over each airport code and crawl buffer server.data files for specific information
            for (String airportCode : airportCodes) {
                String airportName = String.valueOf(fileHandler.getAirportsBuffer().get(airportCode).get(0));
                Object[] weather = fileHandler.getWeatherBuffer().get(airportCode).toArray();
                int delayTime = Integer.parseInt(String.valueOf(fileHandler.getDelaysBuffer().get(airportCode).get(0)));

                Airport newAirport = new Airport(airportName, airportCode, weather, 0, delayTime);
                airports.put(airportCode, newAirport);
            }
        }
        else {
            faaState.createAirports();
            FAAAirports = faaState.getAirports();
        }
    }

    /**
     * Iterate over each flight represented as a list (as read in from the csv) and create a Flight object from
     * it. Get delay from delaysBuffer and minConnectionTime from the connectionsBuffer
     */
    public void createFlights() {
        List<List> flightsAsLists = this.fileHandler.getFlightsBuffer();

        for (List flightAsList : flightsAsLists) {
            String originAirport = String.valueOf(flightAsList.get(0));
            String destinationAirport = String.valueOf(flightAsList.get(1));
            String departureTime = String.valueOf(flightAsList.get(2));
            String arrivalTime = String.valueOf(flightAsList.get(3));
            int flightNumber = Integer.parseInt(String.valueOf(flightAsList.get(4)));
            double airfare = Integer.parseInt(String.valueOf(flightAsList.get(5)));

            // The server.data is held in Hash tables made from reading the CSV files. Because the method to read the CSVs is
            // necessarily abstract (all of the csv formats are different) this gets really ugly.
            int currentDelayTime = Integer.parseInt(String.valueOf(
                    this.fileHandler.getDelaysBuffer().get(originAirport).get(0)));
            int minConnectionTime = Integer.parseInt(String.valueOf(
                    this.fileHandler.getConnectionsBuffer().get(originAirport).get(0)));

            Flight newFlight = new Flight(originAirport, destinationAirport, arrivalTime, departureTime, flightNumber,
                    airfare, currentDelayTime, minConnectionTime);

            this.flights.add(newFlight);

        }
    }

    /**
     * Deletes a reservation from the collection of reservations stored in memory
     *
     * @param reservationToBeDeleted The reservation object that will be removed
     */
    public void deleteReservation(Reservation reservationToBeDeleted) {
        Reservation reservation = matchReservation(reservationToBeDeleted);

        reservations.remove(reservation);
    }

    /**
     * Adds a reservation to the collection in memory
     *
     * @param newReservation Reservation object to be added to the collection
     */
    public void addReservation(Reservation newReservation) {
        Reservation reservationCheck = matchReservation(newReservation);

        if (reservationCheck == null) {
            this.reservations.add(newReservation);
        } else {
            System.out.println("This user already has a reservation for this origin/destination!");
        }
    }

    /**
     * Saves reservations to the flat .csv file to be loaded back into memory on system load
     */
    public void saveReservations() {
        // format the reservations to be written to a csv
        List<List> reservationsAsLists = createReservationsAsLists(reservations);
        fileHandler.writeCSV("server/data/reservations.csv", reservationsAsLists);
    }

    /**
     * Stores the reservations as a list of lists of their attributes for the purposes of being written to a csv
     * on save. The reservations are initially stored as Reservation objects and need to be made into lists first
     * @param reservations
     * @return A list of reservations stored as lists of attribtues
     */
    public List<List> createReservationsAsLists(List<Reservation> reservations) {
        List listOfReservations = new ArrayList();

        for (Reservation reservation : reservations) {
            List reservationAttributeList = new ArrayList();
            List<Integer> listOfFlightNumbers = new ArrayList();

            for (Flight flight : reservation.getFlightPlan().getFlights()) {
                listOfFlightNumbers.add(flight.getFlightNumber());
            }

            // retrieve the "public key" attributes from each reservation and add them to the attr. list
            String customer = reservation.getCustomer();
            String originAirport = reservation.getOriginAirportCode();
            String destinationAirport = reservation.getDestinationAirportCode();

            reservationAttributeList.add(customer);
            reservationAttributeList.add(originAirport);
            reservationAttributeList.add(destinationAirport);

            for (Integer flightNumber : listOfFlightNumbers) {
                reservationAttributeList.add(flightNumber);
            }

            listOfReservations.add(reservationAttributeList);
        }

        return listOfReservations;
    }

    /**
     * When passed a reservation, this method checks if a reservation currently exists with the same customer name,
     * origin airport and destination airport
     * @param reservationToBeMatched
     * @return Reservation object if it there is a match and null if there is not
     */
    public Reservation matchReservation(Reservation reservationToBeMatched) {
        String name = reservationToBeMatched.getCustomer();
        String originAirportCode = reservationToBeMatched.getOriginAirportCode();
        String destAirportCode = reservationToBeMatched.getDestinationAirportCode();

        for (Reservation reservation : reservations) {
            String currentCustName = reservation.getCustomer();
            String currentOrigAirportCode = reservation.getOriginAirportCode();
            String currentDestAirportCode = reservation.getDestinationAirportCode();

            if (currentCustName.equals(name) && currentOrigAirportCode.equals(originAirportCode)
                    && destAirportCode.equals(currentDestAirportCode)) {
                return reservation;
            }
        }
        return null;
    }

    /**
     * Tells the fileHandler class to read the reservations csv. Using that return value this class generates
     * Reservation objects from the List<List> of reservation attributes that is returned
     */
    public void readReservationsOnLoad() {
        List<List> reservationsAsLists = fileHandler.readInReservationsOnLoad();
        List<Reservation> reservations = new ArrayList<>();

        for (List reservationAsList : reservationsAsLists) {
            String customerName = String.valueOf(reservationAsList.get(0));
            reservationAsList.remove(0);
            String originAirportName = String.valueOf(reservationAsList.get(0));
            reservationAsList.remove(0);
            Airport originAirport = airports.get(originAirportName);
            String destAirportname = String.valueOf(reservationAsList.get(0));
            reservationAsList.remove(0);
            Airport destinationAirport = airports.get(destAirportname);

            List<Flight> flights = new ArrayList<>();
            FlightPlan flightForConstructor;

            for (Object flightNumber : reservationAsList) {
                Flight flight = getFlightUsingFlightNumber(Integer.parseInt(String.valueOf((flightNumber))));
                flights.add(flight);
            }

            if (flights.size() == 1) {
                flightForConstructor = flights.get(0);
            } else {
                flightForConstructor = new Itinerary(flights);
            }

            Reservation reservation = new Reservation(flightForConstructor, customerName,
                    originAirport.getAirportCode(), destinationAirport.getAirportCode());

            this.reservations.add(reservation);
        }
    }

    /**
     * Iterates over the flight objects stored and attempts to match a flight to the flightNumber passed in
     * @param flightNumber
     * @return a Flight object if there is a match and null if there is not
     */
    private Flight getFlightUsingFlightNumber(int flightNumber) {
        try {
            for (Flight flight : flights) {
                if (flightNumber == flight.getFlightNumber()) {
                    return flight;
                }
            }
        } catch (Exception e) {
            System.out.println("Did not match flight number to a real flight.");
        }

        return null;
    }

    /**
     * Returns all reservations as a list
     *
     * @return List of reservation objects
     */
    public List<Reservation> getReservations() {
        return this.reservations;
    }

    /**
     * All the airports in the system as a Hashtable
     *
     * @return A hashtable of Airport objects
     */
    public Hashtable<String, Airport> getAirports() {
        if (state == 0) {
            return this.airports;
        }
        else {
            return this.FAAAirports;
        }
    }

    /**
     * Gets a list of flight objects
     *
     * @return A list of all Flight objects in the system
     */
    public List<Flight> getFLights() {
        return this.flights;
    }

    public static Hashtable<String, Airport> getFAAAirports() {
        return FAAAirports;
    }

    public static int getState() {
        return state;
    }

    public static void setState(int state) {
        DataHandler.state = state;
    }
}

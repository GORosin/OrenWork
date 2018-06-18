package server.queries;

import server.dataHandlers.DataHandler;
import common.models.Flight;
import common.models.FlightPlan;
import common.models.Itinerary;
import server.sortingAlgorithms.comparators.DepartureComparator;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class FlightQueryServer {
    private DataHandler dataHandler;

    public FlightQueryServer(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }

    /**
     * Given a customer's preferred origin and destination with an optional limit of legs, find all of the
     * relevant Flights and Itineraries
     * @param originAirport
     * @param destinationAirport
     * @param sortStrategyOption
     * @param limitOfLegsOption
     * @return a list of the FlightPlan objects that fit the client's specs
     */
    public List<FlightPlan> getFlightPlans(String originAirport,
                                           String destinationAirport, Comparator sortStrategyOption,
                                           Integer limitOfLegsOption) {
        // the user has the option of passing a sort algo and to limit the number of flight legs.
        // if nothing is queried by the user to specify these things, pass null for both options
        // and the following logic will handle setting the defaults
        Comparator sortComparator;
        Integer limitOfLegs;

        if (sortStrategyOption != null) {
            sortComparator = sortStrategyOption;
        } else {
            sortComparator = new DepartureComparator();
        }

        if (limitOfLegsOption != null) {
            limitOfLegs = limitOfLegsOption;
        } else {
            limitOfLegs = 2;
        }

        List<Flight> allFlights = dataHandler.getFLights();
        List<FlightPlan> relevantFlights = getRelevantFlights(originAirport, destinationAirport, allFlights, limitOfLegs);
        relevantFlights.sort(sortComparator);
        List<FlightPlan> flightPlans = relevantFlights;

        return flightPlans;

    }

    /**
     * Does the actual logic of finding relevant flights
     * @param origin
     * @param dest
     * @param allFlights
     * @param limit
     * @return Returns the List<FlightPlan> with FlightPlans that fit the client's specs
     */
    private List<FlightPlan> getRelevantFlights(String origin, String dest, List<Flight> allFlights, int limit) {
        // create a deep copy of the all flights list so that the list itself can be mutated
        List<Flight> operableListOfFlights = new ArrayList<>(allFlights);
        List<FlightPlan> relevantFlights = new ArrayList<>();

        for (Flight flight : allFlights) {
            // if the flight works as a direct flight, add it to the relevant flights list and remove it
            if (origin.equals(flight.getOriginAirport()) && dest.equals(flight.getDestinationAirport())) {
                relevantFlights.add(flight);
                operableListOfFlights.remove(flight);
            }
        }

        // if the client has specified a limit of zero connections, go no further
        if (limit == 0) {
            return relevantFlights;
        }

        List<Flight> correctOriginFlights = new ArrayList<>();
        List<Flight> correctDestinationFlights = new ArrayList<>();
        List<Flight> listOfPossibleMiddleFlights = new ArrayList<>(operableListOfFlights);

        // create a list of all the flights with the correct origin airport and a separate
        // list of all flights with the correct destination airport
        for (Flight flight : operableListOfFlights) {
            if (origin.equals(flight.getOriginAirport())) {
                correctOriginFlights.add(flight);
                listOfPossibleMiddleFlights.remove(flight);
            }
            if (dest.equals(flight.getDestinationAirport())) {
                correctDestinationFlights.add(flight);
                listOfPossibleMiddleFlights.remove(flight);
            }
        }

        for (Flight originFlight : correctOriginFlights) {
            for (Flight destFlight : correctDestinationFlights) {
                if (checkCorrectConnectingAirport(originFlight, destFlight)) {
                    Boolean connectionIsValid = checkConnectionValidity(originFlight, destFlight);
                    if (connectionIsValid) {
                        List<Flight> listOfFlights = new ArrayList<>();
                        listOfFlights.add(originFlight);
                        listOfFlights.add(destFlight);

                        Itinerary validItinerary = makeItineraryFromListOfFlights(listOfFlights);
                        relevantFlights.add(validItinerary);
                    }
                }
            }
        }

        if (limit == 1) {
            return relevantFlights;
        }

        for (Flight firstFlight : correctOriginFlights) {
            for (Flight lastFlight : correctDestinationFlights) {
                // if the correct origin airport flight and the correct destination airport
                // flight do not match up in the middle, try to find another flight that works
                if (!checkCorrectConnectingAirport(firstFlight, lastFlight)) {
                    for (Flight middleFlight : listOfPossibleMiddleFlights) {
                        if (checkCorrectConnectingAirport(firstFlight, middleFlight) &&
                                checkCorrectConnectingAirport(middleFlight, lastFlight)) {
                            if (checkConnectionValidity(firstFlight, middleFlight)) {
                                if (checkConnectionValidity(middleFlight, lastFlight)) {
                                    List<Flight> listOfFlights = new ArrayList<>();
                                    listOfFlights.add(firstFlight);
                                    listOfFlights.add(middleFlight);
                                    listOfFlights.add(lastFlight);

                                    Itinerary validItinerary = makeItineraryFromListOfFlights(listOfFlights);
                                    relevantFlights.add(validItinerary);
                                }
                            }
                        }
                    }
                }
            }
        }

        return relevantFlights;
    }

    /**
     * Checks if two flights share a "middle" airport
     * @param origin
     * @param dest
     * @return true if the middle airport is the same
     */
    private Boolean checkCorrectConnectingAirport(Flight origin, Flight dest) {
        String firstFlightDestination = origin.getDestinationAirport();
        String secondFlightOrigin = dest.getOriginAirport();

        if (firstFlightDestination.equals(secondFlightOrigin)) {
            return true;
        }
        return false;
    }

    /**
     * checks if two flights have a schedule that will workout with a layover
     * @param origin
     * @param dest
     * @return returns true if the flights will work out
     */
    private Boolean checkConnectionValidity(Flight origin, Flight dest) {
        String originArrivalTime = origin.getArrivalTime();
        String destinationDepartureTime = dest.getDepartureTime();

        Date originDate = formatStringToDate(originArrivalTime);
        Date destDate = formatStringToDate(destinationDepartureTime);

        int delayTime = origin.getCurrentDelayTime();
        int connectionTime = dest.getMinConnectionTime();
        long layoverTime = Math.abs(destDate.getTime() - originDate.getTime()) / (60 * 1000);

        if (delayTime + layoverTime <= connectionTime) {
            return true;
        }

        return false;
    }

    /**
     * takes a flights server.data as a string and formats it to generate a Date object
     * @param timeString
     * @return the corresponding Date object
     */
    private Date formatStringToDate(String timeString) {
        if (timeString.length() < 6) {
            timeString = "0" + timeString;
        }

        String amOrPm = String.valueOf(timeString.charAt(5));
        timeString = timeString.substring(0, 5);

        amOrPm = amOrPm + "m";

        String formattedTimeString = timeString + " " + amOrPm;
        DateFormat formatter = new SimpleDateFormat("h:mm a");
        try {
            Date date = formatter.parse(formattedTimeString);
            return date;
        } catch (ParseException pe) {
            System.out.println("System failed to convert the time as a string to a Date format");
        }

        return null;
    }

    /**
     * Should be depracted
     * @param listOfFlights
     * @return corresponding itinerary
     */
    private Itinerary makeItineraryFromListOfFlights(List<Flight> listOfFlights) {
        return new Itinerary(listOfFlights);
    }
}
package server.RESTfulAPI;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import common.models.Flight;
import common.models.FlightPlan;
import common.models.Itinerary;
import common.models.Reservation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import server.dataHandlers.DataHandler;
import server.dataHandlers.FileHandler;
import server.queries.AirportQueryServer;
import server.queries.FlightQueryServer;
import server.queries.ReservationQueryServer;
import server.sortingAlgorithms.comparators.AirfareComparator;
import server.sortingAlgorithms.comparators.ArrivalComparator;
import server.sortingAlgorithms.comparators.DepartureComparator;

import java.util.Comparator;
import java.util.List;

//TODO Reservation Query location, Make and Delete Reservation

@Controller
public class AFRSController {
    private static FileHandler fileHandler = new FileHandler();
    private static DataHandler dataHandler;

    public AFRSController() {
        // put csv file server.data into lists of generic Objects
        fileHandler.genericReadCSV("server/data/connections.csv", ",", fileHandler.getConnectionsBuffer());
        fileHandler.genericReadCSV("server/data/delays.csv", ",", fileHandler.getDelaysBuffer());
        fileHandler.genericReadCSV("server/data/weather.csv", ",", fileHandler.getWeatherBuffer());
        fileHandler.genericReadCSV("server/data/airports.csv", ",", fileHandler.getAirportsBuffer());

        // the flights csv contains more than one airport code so it uses a different ReadCSV method
        fileHandler.readFlightsCSV("server/data/flights.csv", ",");

        // create the query dataHandler only after the fileHandler buffers have been generated
        dataHandler = new DataHandler(fileHandler);
        dataHandler.createAirports();
        dataHandler.createFlights();

        // Read in all reservations
        dataHandler.readReservationsOnLoad();
    }

    /**
     * @param origin
     * @param destination
     * @param option
     * @param limitOfLegOptions
     * @return
     */
    @RequestMapping(value = "/FlightQuery/{origin}/{destination}/{options}/{limitOfLegOptions}", method = RequestMethod.GET)
    public @ResponseBody
    String FlightQuery(@PathVariable("origin") String origin,
                       @PathVariable("destination") String destination,
                       @PathVariable("options") String option,
                       @PathVariable("limitOfLegOptions") String limitOfLegOptions) {
        Comparator compare = null;
        switch (option) {
            case "Airfare": {
                compare = new AirfareComparator();
                break;
            }
            case "Arrival": {
                compare = new ArrivalComparator();
                break;
            }
            case "Departure": {
                compare = new DepartureComparator();
                break;
            }
        }
        FlightQueryServer flightQuery = new FlightQueryServer(dataHandler);
        List<FlightPlan> plans = flightQuery.getFlightPlans(origin,
                destination, compare, Integer.parseInt(limitOfLegOptions));
        return new Gson().toJson(plans);
    }

    /**
     * @param code
     * @return
     */
    @RequestMapping(value = "/AirportQuery/{code}", method = RequestMethod.GET) //GET
    public @ResponseBody
    String AirportQuery(@PathVariable("code") String code) {
        AirportQueryServer airportQuery = new AirportQueryServer(dataHandler);
        return new Gson().toJson(airportQuery.returnAirport(code));
    }

    /**
     * @param passenger
     * @return
     */
    @RequestMapping(value = "/ReservationQueryPassenger/{passenger}", method = RequestMethod.GET) //GET
    public @ResponseBody
    String ReservationQueryPassenger(@PathVariable("passenger") String passenger) {
        ReservationQueryServer PassengerQuery = new ReservationQueryServer(dataHandler, passenger);
        return new Gson().toJson(PassengerQuery.queryPassenger());
    }

    /**
     * @param passenger
     * @param origin
     * @param destination
     * @return
     */
    @RequestMapping(value = "/ReservationQueryLocation/{passenger}/{origin}/{destination}", method = RequestMethod.GET)
    public @ResponseBody
    String ReservationQuery(@PathVariable("passenger") String passenger,
                            @PathVariable("origin") String origin,
                            @PathVariable("destination") String destination) {
        ReservationQueryServer LocationQuery = new ReservationQueryServer(dataHandler, passenger);
        return new Gson().toJson(LocationQuery.queryLocations(origin, destination));
    }

    /**
     * returns response for create reservation
     *
     * @param reservation
     * @return
     */
    @RequestMapping(value = "/MakeReservationQuery", method = RequestMethod.PUT) //PUT
    public @ResponseBody
    Reservation MakeReservation(@RequestBody String reservation) {
        // Turns JSON back into a Reservation Object to be added to the Datahandler
        JsonParser parser = new JsonParser();
        JsonObject obj = parser.parse(reservation).getAsJsonObject();
        Gson gson = new Gson();
        FlightPlan flightPlan = null;
        String customer = null, originAirportCode = null, destinationAirportCode = null;
        if (obj.getAsJsonObject().has("flightPlan")) {
            JsonElement flightPlanJSON = obj.getAsJsonObject().get("flightPlan").getAsJsonObject();
            Itinerary itinerary = gson.fromJson(flightPlanJSON, Itinerary.class);
            if (itinerary.getFlights() != null) {
                flightPlan = itinerary;
            } else {
                flightPlan = gson.fromJson(flightPlanJSON, Flight.class);
            }
        }
        if (obj.getAsJsonObject().has("customer")) {
            customer = obj.getAsJsonObject().get("customer").getAsString();
        }
        if (obj.getAsJsonObject().has("originAirportCode")) {
            originAirportCode = obj.getAsJsonObject().get("originAirportCode").getAsString();
        }
        if (obj.getAsJsonObject().has("destinationAirportCode")) {
            destinationAirportCode = obj.getAsJsonObject().get("destinationAirportCode").getAsString();
        }
        Reservation newReservation = new Reservation(flightPlan, customer, originAirportCode, destinationAirportCode);
        dataHandler.addReservation(newReservation);
        dataHandler.saveReservations();
        return newReservation;
    }

    /**
     * Deletes a reservation passed through as a JSON argument from the user
     *
     * @param reservation
     * @return
     */
    @RequestMapping(value = "/DeleteReservationQuery", method = RequestMethod.DELETE) //DELETE
    public @ResponseBody
    Reservation DeleteReservation(@RequestBody String reservation) {
        // Turns JSON back into a Reservation Object to be removed from the Datahandler
        JsonParser parser = new JsonParser();
        JsonObject obj = parser.parse(reservation).getAsJsonObject();
        Gson gson = new Gson();
        FlightPlan flightPlan = null;
        String customer = null, originAirportCode = null, destinationAirportCode = null;
        if (obj.getAsJsonObject().has("flightPlan")) {
            JsonElement flightPlanJSON = obj.getAsJsonObject().get("flightPlan").getAsJsonObject();
            Itinerary itinerary = gson.fromJson(flightPlanJSON, Itinerary.class);
            if (itinerary.getFlights() != null) {
                flightPlan = itinerary;
            } else {
                flightPlan = gson.fromJson(flightPlanJSON, Flight.class);
            }
        }
        if (obj.getAsJsonObject().has("customer")) {
            customer = obj.getAsJsonObject().get("customer").getAsString();
        }
        if (obj.getAsJsonObject().has("originAirportCode")) {
            originAirportCode = obj.getAsJsonObject().get("originAirportCode").getAsString();
        }
        if (obj.getAsJsonObject().has("destinationAirportCode")) {
            destinationAirportCode = obj.getAsJsonObject().get("destinationAirportCode").getAsString();
        }
        Reservation deleteReservation = new Reservation(flightPlan, customer, originAirportCode, destinationAirportCode);
        dataHandler.deleteReservation(deleteReservation);
        dataHandler.saveReservations();
        return deleteReservation;
    }

    /**
     * Allows for the changing of the state between local and FAA systems
     *
     * @return
     */
    @RequestMapping(value = "/ChangeState", method = RequestMethod.GET) //GET
    public @ResponseBody
    void changeState() {
        if (dataHandler.getState() == 0) {
            dataHandler.setState(1);
        }
        else {
            dataHandler.setState(0);
        }
    }
}

package server.queries;

import server.dataHandlers.DataHandler;
import common.models.Airport;

import java.util.Hashtable;

public class AirportQueryServer {

    private static Hashtable airports;

    /**
     * Assigns querydata to the object
     *
     * @param queryData the datahandler object needed to query for airports
     */
    public AirportQueryServer(DataHandler queryData) {
        if (queryData.getState() == 0) {
            this.airports = queryData.getAirports();
        }
        else {
            this.airports = queryData.getFAAAirports();
        }
    }

    /**
     * Returns the airport given a airport code
     *
     * @param airportCode 3 all cap letter airport code
     * @return Airport object that matches airportcode
     */
    public static Airport returnAirport(String airportCode) {
        return (Airport) airports.get(airportCode);
    }
}

package client.proxy;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import common.models.Flight;
import common.models.FlightPlan;
import common.models.Itinerary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class QueryFlight {
    public static List<FlightPlan> queryFlight(String originAirport, String destinationAirport, Comparator sortingStrategy, int legLimit) {
        String inputLine;
        StringBuilder response = new StringBuilder();
        try {
            URL serverURL = new URL("http://localhost:8080/FlightQuery/" + originAirport + "/" + destinationAirport + "/" + sortingStrategy + "/" + legLimit);
            HttpURLConnection con = (HttpURLConnection) serverURL.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(10000);
            con.setReadTimeout(10000);
            // Created a BufferedReader to read the contents of the request.
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));

            while ((inputLine = input.readLine()) != null) {
                response.append(inputLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<FlightPlan> queriedList = new ArrayList<>();
        JsonParser parser = new JsonParser();
        JsonArray Jarray = parser.parse(response.toString()).getAsJsonArray();
        Gson gson = new Gson();
        for (JsonElement obj : Jarray) {
            Itinerary itinerary = gson.fromJson(obj, Itinerary.class);
            if (itinerary.getFlights() != null) {
                queriedList.add(itinerary);
            } else {
                Flight flight = gson.fromJson(obj, Flight.class);
                queriedList.add(flight);
            }
        }
        return queriedList;
    }
}

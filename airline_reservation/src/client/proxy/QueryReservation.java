package client.proxy;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import common.models.Flight;
import common.models.FlightPlan;
import common.models.Itinerary;
import common.models.Reservation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class QueryReservation {
    public static List<Reservation> queryReservation(String passenger, String origin, String dest) {
        String inputLine;
        StringBuilder response = new StringBuilder();
        try {
            URL serverURL;
            if (origin == null || dest == null) {
                serverURL = new URL("http://localhost:8080/ReservationQueryPassenger/" + passenger);
            } else {
                serverURL = new URL("http://localhost:8080/ReservationQueryLocation/" + passenger + "/" + origin + "/" + dest);
            }
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

        JsonParser parser = new JsonParser();
        JsonArray Jarray = parser.parse(response.toString()).getAsJsonArray();
        Gson gson = new Gson();
        List<Reservation> reservationList = new ArrayList<>();
        for (JsonElement obj : Jarray) {
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

            reservationList.add(new Reservation(flightPlan, customer, originAirportCode, destinationAirportCode));
        }

        return reservationList;
    }
}

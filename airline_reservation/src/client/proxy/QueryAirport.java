package client.proxy;

import com.google.gson.Gson;
import common.models.Airport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class QueryAirport {

    public static Airport queryAirport(String airportCode){
        String inputLine;
        StringBuilder response = new StringBuilder();
        try {
            URL serverURL = new URL("http://localhost:8080/AirportQuery/" + airportCode);
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

        Gson gson = new Gson();
        return gson.fromJson(response.toString(), Airport.class);
    }
}

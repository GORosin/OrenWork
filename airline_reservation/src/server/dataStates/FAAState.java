package server.dataStates;

import common.models.Airport;
import common.models.Flight;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by nickmontemorano on 10/29/17.
 */
public class FAAState {
    private static Hashtable<String, Airport> airports = new Hashtable<>();
    private static String[] airportCodes = {"ATL", "BOS", "DFW", "IAD", "JFK",
            "LAS", "LAX", "MCO", "ORD", "PHX", "ROC", "SEA", "SFO"};

    public static void main(String[] args) {
        FAAState faaState = new FAAState();
        createAirports();
    }

    public static void createAirports() {
        List<Airport> FAAAirports = new ArrayList<>();
        try {
            for (String code : airportCodes) {
                URL airportCodeURL = new URL("http://services.faa.gov/airport/status/" + code + "?format=application/xml");

                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(airportCodeURL.openStream());

                doc.getDocumentElement().normalize();

                String airportName = doc.getElementsByTagName("Name").item(0).getTextContent();
                String[] weatherDescription = {doc.getElementsByTagName("Weather").item(1).getTextContent() + doc.getElementsByTagName("Temp").item(0).getTextContent()};
                String airportDelayTime = doc.getElementsByTagName("AvgDelay").item(0).getTextContent();

                int realDelayTime = Integer.parseInt(airportDelayTime.substring(0, 1)) * 60;
                Airport newAirport = new Airport(airportName, code, weatherDescription, 0, realDelayTime);
                airports.put(code, newAirport);
            }
        } catch (Exception exception) {
            System.out.println(exception.toString());
        }
    }

    public static Hashtable<String, Airport> getAirports() {
        return airports;
    }
}

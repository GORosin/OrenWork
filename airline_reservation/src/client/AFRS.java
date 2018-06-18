package client;

import client.proxy.*;
import client.request.RequestCommand;
import common.models.Airport;
import common.models.FlightPlan;
import common.models.Reservation;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import static client.Prompt.*;

public class AFRS {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        String choose;
        char c;
        boolean running = true;
        RequestCommand requestCommand = null;

        System.out.println("################################################################");
        System.out.println("###########    Airline Flight Reservation Server    ############");
        System.out.println("################################################################");

        while (running) {
            System.out.println("Query Airport Information (a)\tQuery Flight Information (b)");
            System.out.println("Query Reservations (c)\t\t\tMake Reservation (d)");
            System.out.println("Delete Reservation (e)\t\t\tUndo (f)");
            System.out.println("Redo (g)\t\t\t\t\t\tToggle State (h)");
            System.out.println("Quit (q)");
            choose = in.nextLine();
            choose = choose.toLowerCase();
            c = choose.charAt(0);

            switch (c) {
                case 'a':
                    System.out.println("Airport Information Query. Please type in airport code: ");
                    String airportCode = in.nextLine();
                    Airport airport = QueryAirport.queryAirport(airportCode);
                    if (airport != null) {
                        System.out.println(airport);
                    } else {
                        System.out.println("No airport by that Code in system!\n");
                    }
                    break;
                case 'b':
                    System.out.println("Flight Information Query.");
                    String originAirport = originAirportPrompt(in);
                    String destinationAirport = destinationAirportPrompt(in);
                    Comparator sortingStrategy = orderPrompt(in);
                    int legLimit = maximumConnectionsPrompt(in);
                    List<FlightPlan> flightPlans = QueryFlight.queryFlight(originAirport, destinationAirport, sortingStrategy, legLimit);
                    for (FlightPlan flight : flightPlans) {
                        System.out.println(flight);
                    }
                    break;
                case 'c':
                    System.out.println("Reservation Information Query");
                    String passenger = customerPrompt(in);
                    List<Reservation> matchingReservations = QueryReservation.queryReservation(passenger, null, null);
                    if (matchingReservations.size() > 0) {
                        for (Reservation reservation : matchingReservations) {
                            System.out.println(reservation);
                        }
                        System.out.println();
                    } else {
                        System.out.println("No Reservations Matched that Query. Try again.\n");
                    }
                    break;
                case 'd':
                    requestCommand = CreateReservationProxy.createReservationProxy(in);
                    break;
                case 'e':
                    requestCommand = DeleteReservationProxy.deleteReservationProxy(in);
                    break;
                case 'f':
                    if (requestCommand != null) {
                        requestCommand.undo();
                        System.out.println("Request Undo Successful");
                    } else {
                        System.out.println("There's no request to Undo");
                    }
                    break;
                case 'g':
                    if (requestCommand != null) {
                        requestCommand.execute();
                        System.out.println("Request Redo Successful");
                    } else {
                        System.out.println("There's no request to Redo");
                    }
                    break;
                case 'h':
                    try {
                        URL serverURL = new URL("http://localhost:8080/ChangeState");
                        HttpURLConnection con = (HttpURLConnection) serverURL.openConnection();
                        con.setRequestMethod("GET");
                        con.setConnectTimeout(10000);
                        con.setReadTimeout(10000);

                        String inputLine;
                        StringBuilder response = new StringBuilder();
                        // Created a BufferedReader to read the contents of the request.
                        BufferedReader input = new BufferedReader(
                                new InputStreamReader(con.getInputStream()));

                        while ((inputLine = input.readLine()) != null) {
                            response.append(inputLine);
                        }
                        System.out.println("FAA State Toggled");
                    }
                    catch (Exception except) {
                        System.out.println(except.toString());
                    }
                    break;
                case 'q':
                    running = false;
                    break;
                default:
                    System.out.println("");
            }
        }

        in.close();
    }
}
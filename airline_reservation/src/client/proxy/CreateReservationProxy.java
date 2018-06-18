package client.proxy;

import client.request.MakeReservation;
import client.request.RequestCommand;
import common.models.FlightPlan;
import common.models.Reservation;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import static client.Prompt.*;

public class CreateReservationProxy {
    public static RequestCommand createReservationProxy(Scanner in) {
        System.out.println("Create a new Reservation");

        String customer = customerPrompt(in);
        String resOriginAirport = originAirportPrompt(in);
        String resDestinationAirport = destinationAirportPrompt(in);
        Comparator resSortingStrategy = orderPrompt(in);
        int resLegLimit = maximumConnectionsPrompt(in);

        List<FlightPlan> reservationQueriedList = QueryFlight.queryFlight(resOriginAirport, resDestinationAirport, resSortingStrategy, resLegLimit);

        if (reservationQueriedList.size() > 0) {
            int index = 0;
            for (FlightPlan flight : reservationQueriedList) {
                System.out.println("Option Number: " + index + " | " + flight);
                index++;
            }

            System.out.println("Enter an Option Number from the list above or type 'q' to quit:");
            String optionNumber = in.nextLine();

            if (!optionNumber.equals("q")) {
                while (Integer.parseInt(optionNumber) >= reservationQueriedList.size()) {
                    System.out.println("Enter a Option Number from the list above or type 'q' to quit:");
                    optionNumber = in.nextLine();
                }

                // Create and add Reservation
                Reservation newReservation = new Reservation(reservationQueriedList.get(Integer.parseInt(optionNumber)), customer, resOriginAirport, resDestinationAirport);
                RequestCommand request = new MakeReservation(newReservation);
                request.execute();
                return request;
            }

        } else {
            System.out.println("No Flight Plans Matched that Request. Sorry!\n");
        }
        return null;
    }
}

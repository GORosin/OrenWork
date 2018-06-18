package client.proxy;

import client.request.RequestCommand;
import common.models.Reservation;

import java.util.List;
import java.util.Scanner;

import static client.Prompt.*;

public class DeleteReservationProxy {
    public static RequestCommand deleteReservationProxy(Scanner in) {
        System.out.println("Deleting a Reservation");

        String deleteCustomer = customerPrompt(in);
        String deletresOriginAirport = originAirportPrompt(in);
        String deletresDestinationAirport = destinationAirportPrompt(in);

        List<Reservation> matchingResAsList = QueryReservation.queryReservation(deleteCustomer, deletresOriginAirport, deletresDestinationAirport);

        if (matchingResAsList.size() > 0) {
            System.out.println("Are you sure you want to delete this reservation? : " + matchingResAsList.get(0) + "\ntype 'y' or 'n'");
            String yesOrNo = in.nextLine();
            yesOrNo = yesOrNo.toLowerCase();

            switch (yesOrNo) {
                case ("y"):
                    RequestCommand deleteReservation = new client.request.DeleteReservation(matchingResAsList.get(0));
                    deleteReservation.execute();
                    System.out.println("Reservation deleted!");
                    return deleteReservation;
                default:
                    System.out.println("");
            }
        } else {
            System.out.println("No Reservations Matched that Query. Try again.\n");
        }
        return null;
    }
}

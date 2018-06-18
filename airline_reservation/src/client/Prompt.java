package client;

import server.sortingAlgorithms.comparators.AirfareComparator;
import server.sortingAlgorithms.comparators.ArrivalComparator;
import server.sortingAlgorithms.comparators.DepartureComparator;

import java.util.Comparator;
import java.util.Scanner;

public class Prompt {
    /**
     * Asks for a customer name and returns it
     *
     * @param in The Scanner object
     * @return Returns the customers response
     */
    public static String customerPrompt(Scanner in) {
        System.out.println("Enter Customer Name:");
        String output = in.nextLine();
        if (!output.equals("")) {
            return output;
        } else {
            customerPrompt(in);
        }
        return null;
    }

    /**
     * Asks the user to input the airport code for the origin airport
     *
     * @param in The scanner object
     * @return Returns a string of the airport given by the user in all uppercase
     */
    public static String originAirportPrompt(Scanner in) {
        System.out.println("Enter an Origin Airport Code:");
        String output = in.nextLine();
        if (!output.equals("")) {
            return output.toUpperCase();
        } else {
            customerPrompt(in);
        }
        return null;
    }

    /**
     * Asks the user to input the airport code for the destination airport
     *
     * @param in The scanner object
     * @return Returns a string of the airport given by the user in all uppercase
     */
    public static String destinationAirportPrompt(Scanner in) {
        System.out.println("Enter a Destination Airport Code:");
        String output = in.nextLine();
        if (!output.equals("")) {
            return output.toUpperCase();
        } else {
            customerPrompt(in);
        }
        return null;
    }

    /**
     * Prompts the user to enter an order when making a flight query
     *
     * @param in The Scanner object
     * @return The appropriate FlightSortingStrategy given string input
     */
    public static Comparator orderPrompt(Scanner in) {
        System.out.println("Enter a Sorting Order (Airfare, Arrival, Departure):");
        String output = in.nextLine();
        switch (output) {
            case "Airfare":
                return new AirfareComparator();
            case "Arrival":
                return new ArrivalComparator();
            case "Departure":
                return new DepartureComparator();
            default:
                return null;
        }
    }

    /**
     * Asks the user for the maximum number of connections for their flight. Number must be between 0 and 2, inclusive
     *
     * @param in The scanner object
     * @return The int value of the max connections
     */
    public static int maximumConnectionsPrompt(Scanner in) {
        System.out.println("Enter a Maximum number of Connections (0-2):");
        String output = in.nextLine();
        if (!output.equals("")) {
            try {
                if (Integer.parseInt(output) < 3 && Integer.parseInt(output) >= 0) {
                    return Integer.parseInt(output.toUpperCase());
                }
            } catch (Exception e) {
                customerPrompt(in);
            }
        } else {
            customerPrompt(in);
        }
        return 0;
    }
}

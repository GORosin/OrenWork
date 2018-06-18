package server.sortingAlgorithms.comparators;

import common.models.FlightPlan;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class ArrivalComparator implements Comparator<FlightPlan> {
    public int compare(FlightPlan flightPlan1, FlightPlan flightPlan2) {
        String arrivalTimeAsString = flightPlan1.getFlights().get(0).getArrivalTime();
        String arrivalTime2AsString = flightPlan2.getFlights().get(0).getArrivalTime();

        Date date1 = formatStringToDate(arrivalTimeAsString);
        Date date2 = formatStringToDate(arrivalTime2AsString);

        return (int) (date1.getTime() - date2.getTime());
    }

    /**
     * Formats a string of time to a datetime object
     *
     * @param timeString String of time
     * @return returns a date object of the input time
     */
    private Date formatStringToDate(String timeString) {
        if (timeString.length() < 6) {
            timeString = "0" + timeString;
        }

        String amOrPm = String.valueOf(timeString.charAt(5));
        timeString = timeString.substring(0, 5);

        amOrPm = amOrPm + "m";

        String formattedTimeString = timeString + " " + amOrPm;
        DateFormat formatter = new SimpleDateFormat("h:mm a");
        try {
            Date date = formatter.parse(formattedTimeString);
            return date;
        } catch (ParseException pe) {
            System.out.println("System failed to convert the time as a string to a Date format");
        }

        return null;
    }
}

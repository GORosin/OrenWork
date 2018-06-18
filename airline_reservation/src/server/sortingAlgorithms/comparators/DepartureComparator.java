package server.sortingAlgorithms.comparators;

import common.models.FlightPlan;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class DepartureComparator implements Comparator<FlightPlan> {

    public int compare(FlightPlan flightPlan1, FlightPlan flightPlan2) {
        String departureTime1AsString = flightPlan1.getFlights().get(0).getDepartureTime();
        String departureTime2AsString = flightPlan2.getFlights().get(0).getDepartureTime();

        Date date1 = formatStringToDate(departureTime1AsString);
        Date date2 = formatStringToDate(departureTime2AsString);

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

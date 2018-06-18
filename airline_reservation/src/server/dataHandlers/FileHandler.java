package server.dataHandlers;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

public class FileHandler {
    // these object arrays will hold the raw server.data passed in from the csv files
    private Hashtable<String, List> connectionsBuffer = new Hashtable<>();
    private Hashtable<String, List> delaysBuffer = new Hashtable<>();
    private Hashtable<String, List> weatherBuffer = new Hashtable<>();
    private Hashtable<String, List> airportsBuffer = new Hashtable<>();

    // the flights CSV file must be handled differently and use a different server.data
    // structure because it does not have a single airport code
    private List<List> flightsBuffer = new ArrayList<>();

    // reads csv file and adds each line to a list as a String[]
    public void genericReadCSV(String csvFilePath, String delimiter, Hashtable dataHash) {
        BufferedReader br = null;
        String line = "";


        try {

            br = new BufferedReader(new FileReader(csvFilePath));
            while ((line = br.readLine()) != null) {
                String[] objectAsArray = line.split(delimiter);
                ArrayList objectAsList = new ArrayList<>(Arrays.asList(objectAsArray));

                // generate values for hashtable
                String airportCode = String.valueOf(objectAsList.get(0));
                objectAsList.remove(0);
                List information = objectAsList;

                dataHash.put(airportCode, information);
            }

        } catch (FileNotFoundException e) {
            System.out.println("File " + csvFilePath + " does not exist");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void readFlightsCSV(String csvFilePath, String delimiter) {
        BufferedReader br = null;
        String line = "";


        try {

            br = new BufferedReader(new FileReader(csvFilePath));
            while ((line = br.readLine()) != null) {
                String[] objectAsArray = line.split(delimiter);
                List objectAsList = new ArrayList(Arrays.asList(objectAsArray));
                flightsBuffer.add(objectAsList);
            }

        } catch (FileNotFoundException e) {
            System.out.println("File " + csvFilePath + " does not exist");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<List> readInReservationsOnLoad() {
        BufferedReader br = null;
        String line = "";
        List<List> reservations = new ArrayList<>();


        try {

            br = new BufferedReader(new FileReader("server/data/reservations.csv"));
            while ((line = br.readLine()) != null) {
                String[] objectAsArray = line.split(",");
                List objectAsList = new ArrayList(Arrays.asList(objectAsArray));
                reservations.add(objectAsList);
            }

        } catch (FileNotFoundException e) {
            System.out.println("File " + "server/data/reservations.csv" + " does not exist");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return reservations;
    }

    /**
     * This function needs to be passed a list of lists containing attributes of objects.
     * It will then iterate over each list of attributes in the list passed in and write them
     * to the csv file passed in.
     *
     * @param csvFilePath
     * @param objects
     */
    public void writeCSV(String csvFilePath, List<List> objects) {
        FileWriter fileWriter = null;
        String delimiter = ",";
        String newLineSeparator = "\n";

        try {
            fileWriter = new FileWriter(csvFilePath);

            // write each object out as strings separated by commas.
            // Place a newline if it is not the last item in the file
            for (List objectAsList : objects) {
                int endOfFileChecker = 1;

                for (int i = 0; i < objectAsList.size(); i++) {
                    fileWriter.append(String.valueOf(objectAsList.get(i)));
                    if (i < objectAsList.size() - 1) {
                        fileWriter.append(delimiter);
                    } else {
                        if (endOfFileChecker != objects.size()) {
                            fileWriter.append(newLineSeparator);
                        }
                    }
                }

                endOfFileChecker++;
            }
        } catch (Exception e) {
            System.out.println("Error writing to CSV file: " + csvFilePath);
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (Exception e) {
                System.out.println("Error flushing or closing CSV file.");
            }
        }
    }

    public Hashtable<String, List> getConnectionsBuffer() {

        return connectionsBuffer;
    }

    public Hashtable<String, List> getDelaysBuffer() {
        return delaysBuffer;
    }

    public Hashtable<String, List> getWeatherBuffer() {
        return weatherBuffer;
    }

    public Hashtable<String, List> getAirportsBuffer() {
        return airportsBuffer;
    }

    public List getFlightsBuffer() {
        return flightsBuffer;
    }
}

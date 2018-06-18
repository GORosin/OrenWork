package client.GUI;

import client.proxy.QueryAirport;
import client.proxy.QueryFlight;
import client.proxy.QueryReservation;
import client.request.DeleteReservation;
import client.request.MakeReservation;
import common.models.FlightPlan;
import common.models.Reservation;
import server.sortingAlgorithms.comparators.AirfareComparator;
import server.sortingAlgorithms.comparators.ArrivalComparator;
import server.sortingAlgorithms.comparators.DepartureComparator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Comparator;
import java.util.List;

/**
 * Created by nickmontemorano on 11/6/17.
 */
public class GUIMain {
    private static String originAirport;
    private static String destAirport;
    private static Comparator sortComparator;
    private static List<FlightPlan> plans;
    private static int legLimit;
    private static String customerName;

    private static JFrame mainFrame = new JFrame("Main");
    private static JTextArea outputTextArea = new JTextArea();
    private static JPanel currentlyVisible, mainButtonPanel, selectAirportButtonPanel, chooseAirportForFlightPanel,
            selectComparatorPanel, queryOrDelReservationPanel;
    private static JButton returnToMainButton = new JButton("Return to Menu");
    private static int windowWidth = 700;
    private static int windowHeight = 500;
    private static String[] airportCodes = {"ATL", "BOS", "DFW", "IAD", "JFK",
            "LAS", "LAX", "MCO", "ORD", "PHX", "ROC", "SEA", "SFO"};

    public static void main(String[] args) {
        mainFrame.setBounds(0, 0, windowWidth, windowHeight);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        outputTextArea.setBorder(BorderFactory.createLineBorder(Color.black, 5, true));
        outputTextArea.setEditable(false);
        //mainFrame.add(outputTextArea, BorderLayout.CENTER);

        JScrollPane scrollTextArea = new JScrollPane(outputTextArea);
        mainFrame.add(scrollTextArea);

        mainButtonPanel = mainButtons();
        selectAirportButtonPanel = airportQueryButtons();

        setVisiblePanel(mainButtonPanel);

        JButton switchStates = new JButton("Change States");
        switchStates.addActionListener(e -> {

        });
        mainFrame.add(switchStates, BorderLayout.EAST);

        mainFrame.setVisible(true);

        returnToMainButton.addActionListener(e -> {
            setVisiblePanel(mainButtonPanel);
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
            }
            catch (Exception except) {
                System.out.println(except.toString());
            }
        });
    }

    private static JPanel airportQueryButtons() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0, 1));
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        buttonPanel.setPreferredSize(new Dimension(200, 0));

        for (String code : airportCodes) {
            JButton button = new JButton(code);
            button.addActionListener(e -> {
                String airportInfo = QueryAirport.queryAirport(code).toString();
                outputTextArea.setText(airportInfo);
                setVisiblePanel(mainButtonPanel);
            });
            buttonPanel.add(button);
        }

        return buttonPanel;
    }

    private static JPanel mainButtons() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0, 1, 0, 10));
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        buttonPanel.setPreferredSize(new Dimension(200, 0));

        for (QueryButtons buttonType : QueryButtons.values()) {
            switch (buttonType) {
                case AIRPORT_INFO:
                    JButton airportInfoButton = new JButton("Get Airport Info");
                    airportInfoButton.addActionListener(e -> {
                        setVisiblePanel(selectAirportButtonPanel);
                    });
                    buttonPanel.add(airportInfoButton);
                    break;
                case FLIGHT_INFO:
                    JButton flightInfoButton = new JButton("Get Flight Info");
                    flightInfoButton.addActionListener(e -> {
                        chooseAirportForFlightPanel = createChooseAirportPanel("Pick Origin Airport", flightInfoButton);
                        setVisiblePanel(chooseAirportForFlightPanel);
                    });
                    buttonPanel.add(flightInfoButton);
                    break;
                case MAKE_RESERVATION:
                    JButton makeResButton = new JButton("Make Reservation");
                    makeResButton.addActionListener(e -> {
                        chooseAirportForFlightPanel = createChooseAirportPanel("Pick Origin Airport", makeResButton);
                        JPanel custNamePanel = createCustomerNamePanel(makeResButton);
                        setVisiblePanel(custNamePanel);
                    });
                    buttonPanel.add(makeResButton);
                    break;
                case DEL_RESERVATIONS:
                    JButton delResButton = new JButton("Delete Reservation");
                    delResButton.addActionListener(e -> {
                        queryOrDelReservationPanel = createQueryReservationPanel(delResButton);
                        setVisiblePanel(queryOrDelReservationPanel);
                    });
                    buttonPanel.add(delResButton);
                    break;
                case QUERY_RESERVATIONS:
                    JButton queryResButton = new JButton("Query Reservation");
                    queryResButton.addActionListener(e -> {
                        queryOrDelReservationPanel = createQueryReservationPanel(queryResButton);
                        setVisiblePanel(queryOrDelReservationPanel);
                    });
                    buttonPanel.add(queryResButton);
                    break;
            }
        }

        return buttonPanel;
    }

    private static JPanel createChooseAirportPanel(String instruction, JButton buttonSender) {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0, 1));
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        buttonPanel.setPreferredSize(new Dimension(200, 0));

        JLabel instructions = new JLabel(instruction);
        buttonPanel.add(instructions);

        for (String code : airportCodes) {
            JButton button = new JButton(code);
            button.addActionListener(e -> {
                if (instruction == "Pick Origin Airport") {
                    originAirport = code;
                    chooseAirportForFlightPanel = createChooseAirportPanel("Pick Destination Airport", buttonSender);
                    setVisiblePanel(chooseAirportForFlightPanel);
                } else if (instruction == "Pick Destination Airport" && buttonSender.getText() == "Delete Reservation") {
                    destAirport = code;
                    List<Reservation> resToDeleteAsList = QueryReservation.queryReservation(customerName, originAirport, destAirport);
                    if (resToDeleteAsList.size() == 0) {
                        outputTextArea.setText("No reservation exists for " + customerName + ", origin: " + originAirport
                                                + ", destination: " + destAirport);
                        setVisiblePanel(mainButtonPanel);
                    }
                    else {
                        Reservation resToDelete = resToDeleteAsList.get(0);
                        DeleteReservation deleteReservation = new client.request.DeleteReservation(resToDelete);
                        outputTextArea.setText("Reservation Deleted: \n" + resToDelete.toString());
                        setVisiblePanel(mainButtonPanel);
                    }
                } else if (instruction == "Pick Destination Airport" && buttonSender.getText() == "Query Reservation") {
                    destAirport = code;
                    List<Reservation> matchingReservations = QueryReservation.queryReservation(customerName, originAirport, destAirport);
                    displayReservations(matchingReservations);
                    setVisiblePanel(mainButtonPanel);
                } else if (instruction == "Pick Destination Airport") {
                    destAirport = code;
                    selectComparatorPanel = createComparatorSelectionPanel(buttonSender);
                    setVisiblePanel(selectComparatorPanel);
                }
            });
            buttonPanel.add(button);
        }

        return buttonPanel;
    }

    private static JPanel createComparatorSelectionPanel(JButton buttonSender) {
        JPanel comparatorPanel = new JPanel();
        comparatorPanel.setLayout(new GridLayout(0, 1));
        comparatorPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        comparatorPanel.setPreferredSize(new Dimension(200, 0));

        JLabel chooseSortLabel = new JLabel("Choose a Sort Method");
        comparatorPanel.add(chooseSortLabel);

        JButton airfareSort = new JButton("Airfare");
        airfareSort.addActionListener(e -> {
            sortComparator = new AirfareComparator();
            JPanel selectLegLimitPanel = createChooseLegLimitPanel(buttonSender);
             setVisiblePanel(selectLegLimitPanel);
        });
        comparatorPanel.add(airfareSort);

        JButton arrivalSort = new JButton("Arrival");
        arrivalSort.addActionListener(e -> {
            sortComparator = new ArrivalComparator();
            JPanel selectLegLimitPanel = createChooseLegLimitPanel(buttonSender);
            setVisiblePanel(selectLegLimitPanel);
        });
        comparatorPanel.add(arrivalSort);

        JButton departureSort = new JButton("Departure");
        departureSort.addActionListener(e -> {
            sortComparator = new DepartureComparator();
            JPanel selectLegLimitPanel = createChooseLegLimitPanel(buttonSender);
            setVisiblePanel(selectLegLimitPanel);
        });
        comparatorPanel.add(departureSort);

        return comparatorPanel;
    }

    private static JPanel createChooseLegLimitPanel(JButton buttonSender) {
        JPanel legLimitPanel = new JPanel();
        legLimitPanel.setLayout(new GridLayout(0, 1));
        legLimitPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        legLimitPanel.setPreferredSize(new Dimension(200, 0));

        JLabel chooseLegLimitLabel = new JLabel("Choose the Maximum Legs");
        legLimitPanel.add(chooseLegLimitLabel);

        for (int i = 0; i <= 2; i++) {
            JButton button = new JButton(String.valueOf(i));
            button.addActionListener(e -> {
                legLimit = Integer.parseInt(button.getText());

                plans = QueryFlight.queryFlight(originAirport, destAirport, sortComparator, legLimit);
                String plansAsString = createUsableFlightString(plans);
                outputTextArea.setText(plansAsString);

                if (buttonSender.getText().equals("Get Flight Info")) {
                    setVisiblePanel(mainButtonPanel);
                } else {
                    JPanel chooseResPanel = createChooseReservationPanel(plans.size());
                    setVisiblePanel(chooseResPanel);
                }

            });

            legLimitPanel.add(button);
        }

        return legLimitPanel;
    }

    private static JPanel createChooseReservationPanel(int numberOfPossibleReservations) {
        JPanel chooseReservationPanel = new JPanel();
        chooseReservationPanel.setLayout(new GridLayout(0, 1));
        chooseReservationPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        chooseReservationPanel.setPreferredSize(new Dimension(200, 0));

        if (numberOfPossibleReservations == 0) {
            outputTextArea.setText("No Reservations Available!");

            chooseReservationPanel.add(returnToMainButton);
        } else {
            JLabel reservationLabel = new JLabel("Choose a Reservation");
            chooseReservationPanel.add(reservationLabel);

            for (int i = 1; i <= numberOfPossibleReservations; i++) {
                FlightPlan flightPlan = plans.get(i - 1);
                JButton button = new JButton(String.valueOf(i));
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Reservation reservation = new Reservation(flightPlan, customerName, originAirport, destAirport);
                        MakeReservation makeReservations = new MakeReservation(reservation);
                        makeReservations.execute();
                        outputTextArea.setText("Reservation: \n" + reservation.toString() + "\nsaved!");
                        setVisiblePanel(mainButtonPanel);
                    }
                });
                chooseReservationPanel.add(button);
            }
        }

        return chooseReservationPanel;
    }

    private static JPanel createQueryReservationPanel(JButton buttonSender) {
        JPanel queryResPanel = new JPanel();
        queryResPanel.setLayout(new GridLayout(0, 2));
        queryResPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        queryResPanel.setPreferredSize(new Dimension(200, 0));

        JLabel nameLabel = new JLabel("Name:");
        queryResPanel.add(nameLabel);

        JTextArea nameField = new JTextArea();
        queryResPanel.add(nameField);

        JButton enter = new JButton("Enter");
        enter.addActionListener(e -> {
            chooseAirportForFlightPanel = createChooseAirportPanel("Pick Origin Airport", buttonSender);
            customerName = nameField.getText();
            setVisiblePanel(chooseAirportForFlightPanel);
        });
        queryResPanel.add(enter);

        return queryResPanel;
    }

    private static JPanel createCustomerNamePanel(JButton buttonSender) {
        JPanel customerNamePanel = new JPanel();
        customerNamePanel.setLayout(new GridLayout(0, 1));
        customerNamePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        customerNamePanel.setPreferredSize(new Dimension(200, 0));

        JLabel nameLabel = new JLabel("Enter Name");
        customerNamePanel.add(nameLabel);

        JTextArea textArea = new JTextArea();
        customerNamePanel.add(textArea);

        JButton button = new JButton("Enter");
        button.addActionListener(e -> {
            customerName = textArea.getText();
            chooseAirportForFlightPanel = createChooseAirportPanel("Pick Origin Airport", buttonSender);
            setVisiblePanel(chooseAirportForFlightPanel);
        });
        customerNamePanel.add(button);

        return customerNamePanel;
    }

    private static void setVisiblePanel(JPanel panel) {
        if (currentlyVisible != null) {
            mainFrame.getContentPane().remove(currentlyVisible);
        }

        currentlyVisible = panel;
        mainFrame.add(currentlyVisible, BorderLayout.LINE_START);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private static String createUsableFlightString(List<FlightPlan> flights) {
        String flightsAsString = "";
        int iteration = 1;

        for (FlightPlan flightPlan : flights) {
            flightsAsString += String.valueOf(iteration) + " - ";
            String flightPlanString = flightPlan.toString();
            flightsAsString += flightPlanString + "\n";

            iteration++;
        }

        return flightsAsString;
    }

    private static void displayReservations(List<Reservation> reservations) {
        String reservationListString = "";

        for (Reservation res : reservations) {
            reservationListString += res.toString() + "\n";
        }

        outputTextArea.setText(reservationListString);
    }
}
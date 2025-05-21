package com.example.myapplication;

import android.app.Notification;
import android.util.Log;

import androidx.collection.ArraySet;

import java.util.Objects;

public class Theater {
    ArraySet<Ticket> tickets;
    private String name;
    private String location;
    private String phone;
    private String email;
    private String website;
    private String description;
    private String[] hours; // of the shows, e.g. 18:00 and 22:00
    private int numOfZones;
    private int[][] pricesPerZone;
    private short seatsPerZone;
    private String address;
    private String city;
    private String state;
    private String zip;

    private String[] showNames = {"Show 1", "Show 2"};

    private boolean[] isZoneFriendlyToDisabledMov;
    private boolean[] isZoneFriendlyToDisabledSight;

    private int[][] seatIds;
    private int[][] seatPrices;
    private boolean[][][][] seatAvailabilityRoom1; // Arguments: 0 = Date, 1 = hour, 2 = seat id, 3 = availability status (available or not)
    private boolean[][][][] seatAvailabilityRoom2;

    private Customer[] customers;
    private int counterCustomers;

    public Theater() {
        this.tickets = new ArraySet<>();
        this.name = "My Theater";
        this.phone = "0000000000";
        this.email = "email@email.email";
        this.website = "www.mytheater.mytheater";
        this.description = "Description";
        this.hours = new String[2];
        this.hours[0] = "18:00";
        this.hours[1] = "22:00";
        this.numOfZones = 10;
        this.pricesPerZone = new int[][]{{50, 40, 30, 20, 10, 10, 10, 10, 10, 10}, {50, 40, 30, 20, 10, 10, 10, 10, 10, 10}};
        this.address = "address";
        this.city = "city";
        this.state = "state";
        this.zip = "zip";
        this.seatsPerZone = 30;
        this.location = address + ", " + city + ", " + state + " " + zip;

        this.seatIds = new int[numOfZones][seatsPerZone];
        this.seatPrices = new int[numOfZones][seatsPerZone];
        this.seatAvailabilityRoom1 = new boolean[10][2][numOfZones][seatsPerZone];
        this.seatAvailabilityRoom2 = new boolean[10][2][numOfZones][seatsPerZone];
        zeroInitialization(seatAvailabilityRoom1); //Set all seats as available
        zeroInitialization(seatAvailabilityRoom2);

        this.customers = new Customer[100];

    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getWebsite() {
        return website;
    }

    public String getDescription() {
        return description;
    }

    public String[] getHours() {
        return hours;
    }

    public String[] shows(){
        return showNames;
    }

    public void makeTemporaryBooking(String showName, int zone, int amountOfTickets, int time, int date, int customerId){
        // Instead of making a booking for an actual customer,
        // this uses an id.

        // Set seats to unavailable
    }

    public void makeBooking(String showName, int zone, int amountOfTickets, int time, int date){
        Log.d("Booking", "makeBooking theater method called");

        // TODO: implement booking
        // Create ticket...
        // Showname = "1" or "2" --> Room 1 or 2
        // Zone = 1, 2, 3, 4, 5, 6, 7, 8, 9, 10
        // Time = 0, 1 --> 0 = 18:00, 1 = 22:00
        // Date = 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12
        // Amount = 1, 2, 3, 4, 5, 6, 7, 8, 9, 10
        //Set seats to unavailable
        if (showName.contentEquals("1")){
            for (int i = 0; i < amountOfTickets; i++){
                seatAvailabilityRoom1[date][time][zone - 1][i] = false;
                Log.d("Unavailable", "Set unavailable seat " + 1 + zone + i);
            }
        }
        else if (showName.contentEquals("2")){
            for (int i = 0; i < amountOfTickets; i++){
                seatAvailabilityRoom2[date][time][zone - 1][i] = false;
                Log.d("Unavailable", "Set unavailable seat " + 2 + zone + i);
            }
        }
        //Create ticket
        Ticket ticket = new Ticket(showName, zone, String.valueOf(time), String.valueOf(date), false);
        tickets.add(ticket);



    }

    public void cancelBooking(String showName, int zone, int amountOfTickets, int time, int date, Customer customer){
        // TODO: implement canceling booking
    }

    public short return_the_id_of_the_customer_if_this_exists(Customer customer){
        for (int i = 0; i < counterCustomers; i++){
            if (customers[i] == customer){
                return (short)i;
            }
        }
        return -1;
    }

    public boolean hasAvailableSeats(int date, int hour, int amount, int room){
        //Searches the availability matrix for a specific date and hour and amount of tickets
        //and returns true if there are available seats
        //for that date.
        if (room == 1){
            int a = 0;
            for (int i = 0; i < numOfZones; i++){
                for (int j = 0; j < seatsPerZone; j++){
                    if (seatAvailabilityRoom1[date][hour][i][j] == true){
                        a++;
                        if (a == amount){
                            return true;
                        }
                    }
                }
            }
        }
        if (room == 2){
            int a = 0;
            for (int i = 0; i < numOfZones; i++){
                for (int j = 0; j < seatsPerZone; j++){
                    if (seatAvailabilityRoom2[date][hour][i][j] == true){
                        a++;
                        if (a == amount){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public String[] getInformationSummary(){
        // Returns a summary of the information of the theater
        // Array of strings
        String[] summary = new String[7];
        summary[0] = "Name: " + name;
        summary[1] = "Location: " + location;
        summary[2] = "Phone: " + phone;
        summary[3] = "Address: " + address;
        summary[4] = "City: " + city;
        summary[5] = "State: " + state;
        summary[6] = "Zip: " + zip;
        return null;
    }

    private void zeroInitialization(boolean[][][][] array) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < numOfZones; k++) {
                    for (int l = 0; l < seatsPerZone; l++) {
                        array[i][j][k][l] = true;
                    }
                }
            }
        }
    }

    public void setAvailability(int room, boolean[][][][] availabilityMatrix){
        if (room == 1){
            seatAvailabilityRoom1 = availabilityMatrix;
        }
        if (room == 2) {
            seatAvailabilityRoom2 = availabilityMatrix;
        }
    }

    public boolean[][][][] getAvailability(int room){
        if (room == 1){
            return seatAvailabilityRoom1;
        }
        if (room == 2){
            return seatAvailabilityRoom2;
        }
        return null;
    }




}

package main;

import handler.DatabaseConnection;

public class Main {

    public static void main(String[] args) {
        DatabaseConnection.openConnection();
        DatabaseConnection.closeConnection();
    }

}

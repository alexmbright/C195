package model;

public class Customer {

    private int id;
    private String name;
    private String address;
    private String postal;
    private String phone;
    private int divisionId;

    public Customer(int id, String name, String address, String postal, String phone, int divisionId) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.postal = postal;
        this.phone = phone;
        this.divisionId = divisionId;
    }



}

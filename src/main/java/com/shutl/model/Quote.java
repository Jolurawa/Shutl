package com.shutl.model;
import java.util.HashMap;

public class Quote {
    String pickupPostcode;
    String deliveryPostcode;
    String vehicle;
    Long price;
    HashMap<String, Integer>[] products;

    public Quote() {}

    public Quote(String pickupPostcode, String deliveryPostcode, String vehicle, HashMap<String, Integer>[] products) {
        this.pickupPostcode = pickupPostcode;
        this.deliveryPostcode = deliveryPostcode;
        this.vehicle = vehicle;
        this.products = products;
    }

    public Quote(String pickupPostcode, String deliveryPostcode, Long price, String vehicle, HashMap<String, Integer>[] products) {
        this.pickupPostcode = pickupPostcode;
        this.deliveryPostcode = deliveryPostcode;
        this.price = price;
        this.vehicle = vehicle;
        this.products = products;
    }

    public String getPickupPostcode() {
        return pickupPostcode;
    }

    public void setPickupPostcode(String pickupPostcode) {
        this.pickupPostcode = pickupPostcode;
    }

    public String getDeliveryPostcode() {
        return deliveryPostcode;
    }

    public void setDeliveryPostcode(String deliveryPostcode) {
        this.deliveryPostcode = deliveryPostcode;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }
    public String getVehicle() {
        return vehicle;
    }
    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }
    public HashMap<String, Integer>[] getProducts(){
        return products;
    }
    public void setProducts(HashMap<String, Integer>[] products){
        this.products =  products;
    }
}

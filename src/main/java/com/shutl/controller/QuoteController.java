package com.shutl.controller;

import com.shutl.model.Quote;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.HashMap;

@RestController
public class QuoteController {

    @RequestMapping(value = "/quote", method = POST)
    public @ResponseBody Quote quote(@RequestBody Quote quote) {

        Long price = Math.abs((Long.valueOf(quote.getDeliveryPostcode(), 36) - Long.valueOf(quote.getPickupPostcode(), 36))/100000000);

        //HashMap of vehicles to easily compare size
        HashMap<String, Integer> vehicles = new HashMap<String, Integer>();

        vehicles.put("bicycle",     1);
        vehicles.put("motorbike",   2);
        vehicles.put("parcel_car",  3);
        vehicles.put("small_van",   4);
        vehicles.put("large_van",   5);

        //Final price of delivery if using the vehicle selected by user
        Long priceAfterUserMarkup = ApplyMarkup(quote.getVehicle(), price);

        //Cheapest possible vehicle for the price after markup based on vehicle price limits
        String cheapestVehicle = SelectCheapestVehicle(priceAfterUserMarkup);

        //Total size of all products
        HashMap<String, Integer> totalVolumetrics = new HashMap<String, Integer>();
        totalVolumetrics =  CalculateTotalVolumetrics(quote.getProducts());

        //Smallest possible Vehicle based on product volumetrics
        String smallestPossibleVehicle = SelectVehicleVolumetric(totalVolumetrics.get("weight"), totalVolumetrics.get("length"), totalVolumetrics.get("width"), totalVolumetrics.get("height"));
        

        //If vehicle is not specified by user, determine vehicle by volumetrics
        if(quote.getVehicle().equals("")){

            //Final price of delivery if using smallest vehicle determined by system
            Long priceAfterSmallestPossibleVehicleMarkup = ApplyMarkup(smallestPossibleVehicle, price);
            return new Quote(quote.getPickupPostcode(), quote.getDeliveryPostcode(), priceAfterSmallestPossibleVehicleMarkup, smallestPossibleVehicle, quote.getProducts());       
        
        }else{

            //Check if chosen vehicle is within price limit and large enough for product, if not return with cheapest possible vehicle that is large enough.
            if(vehicles.get(quote.getVehicle()) >= vehicles.get(cheapestVehicle) && vehicles.get(quote.getVehicle()) >= vehicles.get(smallestPossibleVehicle)){
                return new Quote(quote.getPickupPostcode(), quote.getDeliveryPostcode(), priceAfterUserMarkup, quote.getVehicle(), quote.getProducts());
            }else if(vehicles.get(cheapestVehicle) >= vehicles.get(smallestPossibleVehicle)){
                //Final price of delivery if using largest cheapest vehicle determined by system
                Long priceAfterCheapestVehicleMarkup = ApplyMarkup(cheapestVehicle, price);
                return new Quote(quote.getPickupPostcode(), quote.getDeliveryPostcode(), priceAfterCheapestVehicleMarkup, cheapestVehicle, quote.getProducts());
            }else{
                //Final price of delivery if using smallest vehicle determined by system
                Long priceAfterSmallestPossibleVehicleMarkup = ApplyMarkup(smallestPossibleVehicle, price);
                return new Quote(quote.getPickupPostcode(), quote.getDeliveryPostcode(), priceAfterSmallestPossibleVehicleMarkup, smallestPossibleVehicle, quote.getProducts());       
            }
        }
    }


    //Increases price by appropriate percentage depending on vehicle
    Long ApplyMarkup(String vehicle, Long initialPrice){

        Long finalPrice;

        if(vehicle.equals("bicycle")){
            finalPrice = (initialPrice * 110)/100;    //Increase price by 10%
        }else if(vehicle.equals("motorbike")){
            finalPrice = (initialPrice * 115)/100;    //Increase price by 15%
        }else if(vehicle.equals("parcel_car")){
            finalPrice = (initialPrice * 120)/100;    //Increase price by 20%
        }else if(vehicle.equals("small_van")){
            finalPrice = (initialPrice * 130)/100;    //Increase price by 30%
        }else if(vehicle.equals("large_van")){
            finalPrice = (initialPrice * 140)/100;    //Increase price by 40%
        }else{
            finalPrice = initialPrice;                //Vehicle not recognised. No change 
        }

        return finalPrice;
    }

    //Determines the cheapest vehicle for the price after markup based on vehicle price limits
    String SelectCheapestVehicle(Long priceAfterMarkup){

        String vehicle;

        if(priceAfterMarkup <= 500){
            vehicle = "bicycle";
        }else if(priceAfterMarkup <= 750){
            vehicle = "motorbike";
        }else if(priceAfterMarkup <= 1000){
            vehicle = "parcel_car";
        }else if(priceAfterMarkup <= 1500){
            vehicle = "small_van";
        }else{
            vehicle = "large_van";
        }

        return vehicle;
    }
    //Determine smallest vehicle by product volumetrics
    String SelectVehicleVolumetric(int weight, int length, int width, int height){
        
        String vehicle;
        
        if(weight <= 3 && length <= 30 && width <= 25 && height <= 10){
            vehicle = "bicycle";
        }else if(weight <= 6 && length <= 35 && width <= 25 && height <= 25){
            vehicle = "motorbike";
        }else if(weight <= 50 && length <= 100 && width <= 100 && height <= 75){
            vehicle = "parcel_car";
        }else if(weight <= 400 && length <= 133 && width <= 133 && height <= 133){
            vehicle = "small_van";
        }else{
            vehicle = "large_van";
        }

        return vehicle;
    }

    //Calculate total volumetrics of all products
    HashMap<String, Integer> CalculateTotalVolumetrics(HashMap<String, Integer>[] products){
        HashMap<String, Integer> totalVolumetrics = new HashMap<String, Integer>();

        totalVolumetrics.put("weight", 0);
        totalVolumetrics.put("height", 0);
        totalVolumetrics.put("width", 0);
        totalVolumetrics.put("length", 0);

        for(int i = 0; i < products.length; i++){
            totalVolumetrics.put("weight", totalVolumetrics.get("weight") + products[i].get("weight"));
            totalVolumetrics.put("height", totalVolumetrics.get("height") + products[i].get("height"));
            totalVolumetrics.put("width", totalVolumetrics.get("width") + products[i].get("width"));
            totalVolumetrics.put("length", totalVolumetrics.get("length") + products[i].get("length"));
        }

        return totalVolumetrics;
    }
}

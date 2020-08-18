package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        double inHour = ticket.getInTime().getTime();
        double outHour = ticket.getOutTime().getTime();

        //TODO: Some tests are failing here. Need to check if this logic is correct
        double durationMillis = outHour - inHour;
        double durationHours = (durationMillis) /(1000 * 60 * 60) ;

        double ratePerHour;
        double discount = 1;

        // Premiere demi-heure gratuite
        if(durationHours < 0.5) {
            durationHours = 0;
        }

        // Reduction pour récurent
        if(ticket.isRecuring()) {
            discount = Fare.DISCOUNT_RECURING_CUSTOMER;
        }

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ratePerHour =  Fare.CAR_RATE_PER_HOUR;
                break;
            }
            case BIKE: {
                ratePerHour =  Fare.BIKE_RATE_PER_HOUR;
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }

        ticket.setPrice(durationHours * ratePerHour * discount);
    }
}
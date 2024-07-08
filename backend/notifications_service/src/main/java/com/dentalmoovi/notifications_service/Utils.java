package com.dentalmoovi.notifications_service;

import java.util.Random;

public class Utils {
    private Utils() { throw new IllegalStateException("Utility class"); }

    private static Random random = new Random();

    public static String generateRandom6Number(){
        //Generate randomNumber
        int randomNumber = random.nextInt(1000000);

        //generate a format to add zeros to the left in case randomNumber < 100000
        return String.format("%06d", randomNumber);
    }
    
}

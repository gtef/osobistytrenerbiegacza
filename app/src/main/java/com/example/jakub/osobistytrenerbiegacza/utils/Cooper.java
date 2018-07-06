package com.example.jakub.osobistytrenerbiegacza.utils;

/**
 * Created by Jakub on 2015-11-15.
 */
public class Cooper {
    public static final int VERY_GOOD = 5;
    public static final int GOOD = 4;
    public static final int AVERAGE = 3;
    public static final int BAD = 2;
    public static final int VERY_BAD = 1;
    public static final int NONE_INFO = 0;

    //TODO wyniki testu Coopera
    public static int getStaminaLevel(double totalDistance, int age) {
        return VERY_GOOD;
    }

    public static String getText(int staminaLevel) {
        switch(staminaLevel) {
            case VERY_GOOD: return "bardzo dobry";
            case GOOD: return "dobry";
            case AVERAGE: return "przeciętny";
            case BAD: return "zły";
            case VERY_BAD: return "bardzo zły";
            default: return "brak danych";
        }
    }
}

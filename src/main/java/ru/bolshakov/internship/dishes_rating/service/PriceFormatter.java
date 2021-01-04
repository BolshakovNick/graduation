package ru.bolshakov.internship.dishes_rating.service;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class PriceFormatter {
    private static final DecimalFormat FORMATTER = new DecimalFormat("0.00");

    static {
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        FORMATTER.setDecimalFormatSymbols(dfs);
    }

    public static String doPriceFormatting(Long price) {
        return FORMATTER.format((double) price / 100);
    }
}

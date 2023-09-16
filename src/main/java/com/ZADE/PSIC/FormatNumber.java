package com.ZADE.PSIC;

import java.text.DecimalFormat;

public class FormatNumber {
    public static String formatNumericValue(double numericValue) {
        DecimalFormat decimalFormat = new DecimalFormat("0.#########"); // Adjust the number of decimal places as needed
        return decimalFormat.format(numericValue);
    }
}

package vn.edu.fpt.prm.core.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class Formatter {
    public static String formatCurrency(int amount) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(amount) + "đ";
    }
}

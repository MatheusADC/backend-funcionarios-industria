
package com.amaral.industria.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class FormatUtil {

    private static final DateTimeFormatter DATA_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static String formatarData(LocalDate data) {
        if (data == null)
            return "";
        return data.format(DATA_FORMATTER);
    }

    public static String formatarValor(BigDecimal valor) {
        if (valor == null)
            return "";
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols(new Locale("pt", "BR"));
        simbolos.setGroupingSeparator('.');
        simbolos.setDecimalSeparator(',');
        DecimalFormat df = new DecimalFormat("#,##0.00", simbolos);
        return df.format(valor);
    }
}
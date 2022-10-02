package app.gifforexchangerate.service;

import java.util.Currency;
import java.util.Locale;

public class CurrencyCodeValidationService {

    public static boolean validateCode(String code) {
        return Currency.getAvailableCurrencies().stream()
                .anyMatch(currency -> currency.getCurrencyCode().equals(code.toUpperCase()));
    }
}

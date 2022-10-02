package app.gifforexchangerate.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
public class TestCurrencyCodeValidationService {

    @ParameterizedTest
    @ValueSource(strings = {"USD", "RUB", "rub", "usd", "bGl"})
    public void testCurrencyWithCorrect(String code) {
        boolean isCorrect = CurrencyCodeValidationService.validateCode(code);
        assertThat(isCorrect).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"USsD", "RUd","Gl"})
    public void testCurrencyWithNotCorrect(String code) {
        boolean isCorrect = CurrencyCodeValidationService.validateCode(code);
        assertThat(isCorrect).isFalse();
    }
}

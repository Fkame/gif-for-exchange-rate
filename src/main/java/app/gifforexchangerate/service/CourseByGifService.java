package app.gifforexchangerate.service;

import app.gifforexchangerate.client.CurrencyRateApiClient;
import app.gifforexchangerate.client.GifApiClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ValueNode;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CourseByGifService {

    @Value("${currency.base}")
    private String baseCode;

    @Value("${gif.app.id}")
    private String gifAppId;

    @Value("${currency.app.id}")
    private String currencyAppId;

    public static final String CURRENCY_BECAME_BIGGER = "rich";

    public static final String CURRENCY_BECAME_LOWER = "broke";

    private final CurrencyRateApiClient currencyClient;

    private final GifApiClient gifClient;

    public ResultWrap getGifForCurrencyRate(String toCompare) {

        boolean isValidCode = CurrencyCodeValidationService.validateCode(toCompare);
        if (!isValidCode) {
            return ResultWrap.createError(ErrorText.VALIDATION_ERROR);
        }

        String latestJson = currencyClient.getLatest(currencyAppId, baseCode, toCompare);
        LocalDate yesterdayDate = LocalDate.now().minusDays(1);
        String yesterdayJson =
                currencyClient.getHistorical(yesterdayDate.toString(), currencyAppId, baseCode, toCompare);

        BigDecimal toCompareLatestValue;
        BigDecimal toCompareYesterdayValue;
        try {
            toCompareLatestValue = this.parseCurrencyField(latestJson, toCompare);
            toCompareYesterdayValue = this.parseCurrencyField(yesterdayJson, toCompare);
            log.info("Parsed " + toCompare + ":" +
                    "\n\tlatestValue = " + toCompareLatestValue +
                    "\n\tyesterdayValue = " + toCompareYesterdayValue);
        } catch (Exception ex) {
            return ResultWrap.createError(ErrorText.API_CURRENCY_ERROR);
        }

        String gifJson = toCompareLatestValue.compareTo(toCompareYesterdayValue) == 1
                ? gifClient.getRandomGifByTag(gifAppId, CURRENCY_BECAME_BIGGER)
                : gifClient.getRandomGifByTag(gifAppId, CURRENCY_BECAME_LOWER);

        try {
            GifJsonParser gifJsonParser = new GifJsonParser();
            GifJsonParser.GifInfo gifInfo = gifJsonParser.parseNeededData(gifJson);

            log.info("Giphy data:\n\t" +
                    gifInfo.getUrl() +
                    "\n\t" + gifInfo.getTitle() +
                    "\n\t" + gifInfo.getUrlToGifFile());

            return ResultWrap.builder()
                    .gif(Optional.of(gifInfo.getUrlToGifFile()))
                    .build();
        } catch (Exception ex) {
           return ResultWrap.createError(ErrorText.API_GIPHY_ERROR);
        }
    }

    private BigDecimal parseCurrencyField(String json, String fieldToFound) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        TreeNode root = mapper.readTree(json);
        TreeNode arrayRates = root.get("rates");
        ValueNode value = (ValueNode) arrayRates.get(fieldToFound);

        return value.decimalValue();
    }

    public enum ErrorText {
        API_CURRENCY_ERROR("Не удалось получить ответ от API биржи"),
        API_GIPHY_ERROR("Не удалось получить ответ от API Giphy"),
        VALIDATION_ERROR("Ошибка валидации, введенного кода валюты не существует");

        private String errorText;

        ErrorText(String errorText) {
            this.errorText = errorText;
        }

        public String getErrorText() {
            return this.errorText;
        }
    }

    @Getter
    @Builder
    public static class ResultWrap {
        private ErrorText errorMessage;
        private Optional<String> gif;

        public static ResultWrap createError(ErrorText error) {
            return ResultWrap.builder()
                    .gif(Optional.empty())
                    .errorMessage(error)
                    .build();
        }
    }

}

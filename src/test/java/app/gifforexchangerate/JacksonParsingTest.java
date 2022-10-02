package app.gifforexchangerate;

import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ValueNode;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class JacksonParsingTest {

    String testJson = "{\n" +
            "    \"disclaimer\": \"https://openexchangerates.org/terms/\",\n" +
            "    \"license\": \"https://openexchangerates.org/license/\",\n" +
            "    \"timestamp\": 1449877801,\n" +
            "    \"base\": \"USD\",\n" +
            "    \"rates\": {\n" +
            "        \"AED\": 3.672538,\n" +
            "        \"AFN\": 66.809999,\n" +
            "        \"ALL\": 125.716501,\n" +
            "        \"AMD\": 484.902502,\n" +
            "        \"ANG\": 1.788575,\n" +
            "        \"AOA\": 135.295998,\n" +
            "        \"ARS\": 9.750101,\n" +
            "        \"AUD\": 1.390866\n" +
            "    }\n" +
            "}";

    @Test
    @SneakyThrows
    public void testParsing() {
        ObjectMapper mapper = new ObjectMapper();
        TreeNode root = mapper.readTree(testJson);
        TreeNode arrayRates = root.get("rates");
        ValueNode value = (ValueNode) arrayRates.get("AUD");
        log.info("Value = " + value.decimalValue());
    }

}

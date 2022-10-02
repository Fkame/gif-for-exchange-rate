package app.gifforexchangerate.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "currency-rate-api-client", url = "${currency.api.url}")
public interface CurrencyRateApiClient {

    @RequestMapping(method = RequestMethod.GET, value = "/latest.json")
    String getLatest(@RequestParam("app_id") String appId,
                              @RequestParam("base") String base,
                              @RequestParam("symbol") String comparingWith);

    @RequestMapping(method = RequestMethod.GET, value = "/historical/{date}.json")
    String getHistorical(@PathVariable("date") String date,
                                  @RequestParam("app_id") String appId,
                                  @RequestParam("base") String base,
                                  @RequestParam("symbol") String comparingWith);
}
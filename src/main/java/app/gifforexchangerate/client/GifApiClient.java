package app.gifforexchangerate.client;

import feign.Logger;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "gif-api-client", url = "${gif.api.url}", configuration = GifApiConfiguration.class)
public interface GifApiClient {

    @RequestMapping(method = RequestMethod.GET, value = "/random")
    String getRandomGifByTag(@RequestParam("api_key") String apiKey,
                             @RequestParam("tag") String tag);
}

@Configuration
class GifApiConfiguration {
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
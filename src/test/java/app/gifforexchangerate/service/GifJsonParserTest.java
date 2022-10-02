package app.gifforexchangerate.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class GifJsonParserTest {

    public static final String GIPHY_JSON_ANSWER = "/GiphyAnswer.json";

    @Test
    @SneakyThrows
    public void testParseNeededData() {
        URL jsonFile = this.getClass().getResource(GIPHY_JSON_ANSWER);
        String json = Files.readString(Path.of(jsonFile.toURI()));

        GifJsonParser jsonParser = new GifJsonParser();
        assertThat(jsonParser.parseNeededData(json)).isNotNull();

        GifJsonParser.GifInfo gifInfo = jsonParser.parseNeededData(json);
        assertThat(gifInfo.getTitle()).isEqualTo("its going to be okay x factor GIF");

        log.info("Object: \n\t" + gifInfo.getUrlToGifFile() +
                "\n\t" + gifInfo.getTitle() +
                "\n\t" + gifInfo.getUrl());

    }
}

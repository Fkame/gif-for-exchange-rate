package app.gifforexchangerate.controller;

import app.gifforexchangerate.service.CourseByGifService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static app.gifforexchangerate.service.CourseByGifService.*;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CoursesGifController {

    private final CourseByGifService courseByGifService;

    private static final String RESPONSE_TEMPLATE = "" +
            "<html>" +
            "<img src=\"%s\" alt=\"Ooops, no gif, sorry((\">" +
            "</html>";

    @GetMapping(value = "/course-by-gif", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> getGifByCurrency(
            @RequestParam(required = false, defaultValue = "RUB") String toCompare) {

        ResultWrap resultAndErrors = courseByGifService.getGifForCurrencyRate(toCompare);
        if (resultAndErrors.getGif().isPresent()) {
            String response = String.format(RESPONSE_TEMPLATE, resultAndErrors.getGif().get());
            return ResponseEntity.ok(response);
        }

        ErrorText status = resultAndErrors.getErrorMessage();
        if (status == ErrorText.VALIDATION_ERROR) {
            return ResponseEntity.badRequest().build();
        } else if (status == ErrorText.API_CURRENCY_ERROR) {
            return ResponseEntity.notFound().build();
        } else if (status == ErrorText.API_GIPHY_ERROR) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.internalServerError().body(null);
    }
}

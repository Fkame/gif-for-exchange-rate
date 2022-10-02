package app.gifforexchangerate.controller;

import app.gifforexchangerate.service.CourseByGifService;
import jdk.jshell.Snippet;
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

    @GetMapping(value = "/course-by-gif", produces = MediaType.IMAGE_GIF_VALUE)
    public ResponseEntity<String> getGifByCurrency(
            @RequestParam(required = false, defaultValue = "RUB") String toCompare) {

        ResultWrap resultAndErrors = courseByGifService.getGifForCurrencyRate(toCompare);
        if (resultAndErrors.getGif().isPresent()) {
            return ResponseEntity.ok(resultAndErrors.getGif().get());
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

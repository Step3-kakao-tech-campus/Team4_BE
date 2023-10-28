package com.ktc.matgpt.i18n;

import com.ktc.matgpt.feature_review.utils.ApiUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("/locale")
public class LocaleController {

    private final LocaleService localeService;


    @GetMapping("/change")
    public String changeLocale(@RequestParam String lang, HttpServletRequest request, HttpServletResponse response) {
        localeService.changeLocale(lang, request, response);
        return "redirect:/now";
    }

    @GetMapping("/now")
    public ResponseEntity<?> getLocale(Locale locale) {
        String message = localeService.getMessage("hello", locale);
        ApiUtils.ApiResult<?> apiResult = ApiUtils.success(message);
        return ResponseEntity.ok(apiResult);
    }
}

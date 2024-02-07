package com.disney.alice;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.disney.config.DisneyConfiguration;
import com.zebrunner.carina.utils.config.Configuration;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;

import com.disney.alice.model.RecognitionMetaType;

public class AliceDriver extends AliceClient {

    private final WebDriver driver;
    private final String languageModel;

    public AliceDriver(WebDriver driver) {
        super(Configuration.getRequired(DisneyConfiguration.Parameter.SAGEMAKER_ENDPOINT));
        languageModel = "english";
        this.driver = driver;
    }

    public AliceDriver(WebDriver driver, String languageCode) {
        super(Configuration.getRequired(DisneyConfiguration.Parameter.SAGEMAKER_ENDPOINT));
        if (languageCode.contains("-")) {
            languageCode = StringUtils.substringBefore(languageCode, "-");
        }
        languageModel = new Locale(languageCode).getDisplayLanguage().toLowerCase();
        this.driver = driver;
    }

    @Override
    public AliceAssertion screenshotAndRecognize() {
        File screenshot = Screenshot.capture(driver, ScreenshotType.EXPLICIT_VISIBLE)
                .orElseThrow()
                .toFile();
        List<RecognitionMetaType> recognition = Stream.of(recognize(screenshot, languageModel)).collect(Collectors.toCollection(ArrayList::new));
        return new AliceAssertion(recognition, screenshot.toString());
    }
}

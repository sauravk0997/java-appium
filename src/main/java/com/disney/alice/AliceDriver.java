package com.disney.alice;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.zebrunner.carina.utils.report.ReportContext;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;

import com.disney.alice.model.RecognitionMetaType;
import com.zebrunner.carina.utils.R;

public class AliceDriver extends AliceClient {

    private final WebDriver driver;
    private final String languageModel;

    public AliceDriver(WebDriver driver) {
        super(R.CONFIG.get("sagemaker_endpoint"));
        languageModel = "english";
        this.driver = driver;
    }

    public AliceDriver(WebDriver driver, String languageCode) {
        super(com.zebrunner.carina.utils.R.CONFIG.get("sagemaker_endpoint"));
        if(languageCode.contains("-")) {
            languageCode = StringUtils.substringBefore(languageCode, "-");
        }
        languageModel = new Locale(languageCode).getDisplayLanguage().toLowerCase();
        this.driver = driver;
    }

    @Override
    public AliceAssertion screenshotAndRecognize() {
        //todo in the carina-webdriver: this method should return Path instead of just fileName
        File screenshot = Path.of(ReportContext.getTestDir().getAbsolutePath())
                .resolve(Screenshot.capture(driver, ScreenshotType.EXPLICIT_VISIBLE)
                        .orElseThrow())
                .toFile();
        List<RecognitionMetaType> recognition = Stream.of(recognize(screenshot, languageModel)).collect(Collectors.toCollection(ArrayList::new));
        return new AliceAssertion(recognition, screenshot.toString());
    }
}

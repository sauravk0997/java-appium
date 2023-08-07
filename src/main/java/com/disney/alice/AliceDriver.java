package com.disney.alice;

import com.disney.alice.model.RecognitionMetaType;
import com.disney.qa.common.utils.UniversalUtils;
import com.qaprosoft.carina.core.foundation.utils.R;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AliceDriver extends AliceClient {

    private WebDriver driver;
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

    List<File> screenshotsList = new LinkedList<>();

    @Override
    public AliceAssertion screenshotAndRecognize() {
        var screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        screenshotsList.add(screenshotFile);
        List<RecognitionMetaType> recognition = Stream.of(recognize(screenshotFile, languageModel)).collect(Collectors.toCollection(ArrayList::new));
        return new AliceAssertion(recognition, screenshotFile.toString());
    }

    public void uploadAliceScreenshots() {
        screenshotsList.forEach(UniversalUtils::uploadScreenshot);
    }

    public void uploadLastScreenshot() {
        UniversalUtils.uploadScreenshot(screenshotsList.get(screenshotsList.size() - 1));
    }

    public List<File> getScreenshotsList() {
        return screenshotsList;
    }
}
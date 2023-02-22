package com.disney.alice;

import com.disney.qa.common.utils.UniversalUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class AliceDriver extends AliceClient {

    private WebDriver driver;

    public AliceDriver(WebDriver driver) {
        this.driver = driver;
    }

    List<File> screenshotsList = new LinkedList<>();

    @Override
    public AliceAssertion screenshotAndRecognize() {
        var screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        screenshotsList.add(screenshotFile);
        return new AliceAssertion(recognizeScreenshot(screenshotFile).response, screenshotFile.toString());
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
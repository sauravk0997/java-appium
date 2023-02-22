package com.disney.qa.disney.web.preview;

import com.disney.qa.disney.web.DisneyWebParameters;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusPreviewPage extends DisneyPlusBasePage {

    public DisneyPlusPreviewPage(WebDriver driver) {
        super(driver);
    }

    public DisneyPlusPreviewPage open(WebDriver driver) {
        getHomeUrl();
        waitForPageToFinishLoading();
        return new DisneyPlusPreviewPage(driver);
    }


    @java.lang.SuppressWarnings("squid:S00112")
    public void getDisneyStageUrl(String env) {
        switch (env) {
            case "PROD":
                getDriver().get(DisneyWebParameters.DISNEY_PROD_WEB_PREVIEW.getValue());
                break;
            case "QA":
                //Expandable in the future for QA side of the site.
                break;
            default:
                break;
        }
    }

}
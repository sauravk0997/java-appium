package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.disney.apple.pages.common.DisneyPlusVideoPlayerIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.foundation.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusVideoPlayerIOSPageBase.class)
public class DisneyPlusAppleTVVideoPlayerPage extends DisneyPlusVideoPlayerIOSPageBase {

    public DisneyPlusAppleTVVideoPlayerPage(WebDriver driver) {
        super(driver);
    }

    @ExtendedFindBy(accessibilityId = "contentRatingInfoView")
    private ExtendedWebElement contentRatingInfoView;

    public void waitUntilDetailsPageIsLoadedFromTrailer(long timeout, int polling) {
        DisneyPlusAppleTVDetailsPage disneyPlusAppleTVDetailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        fluentWait(getDriver(), timeout, polling, "Details page did not load after " + timeout)
                .until(it -> disneyPlusAppleTVDetailsPage.isOpened());
    }

    public DisneyPlusAppleTVVideoPlayerPage waitForContentToEnd(long timeout, int polling) {
        fluentWait(getDriver(), timeout, polling, "Up Next End Card did not load after " + timeout).until(it -> isUpNextHeaderPresent());
        return new DisneyPlusAppleTVVideoPlayerPage(getDriver());
    }
}

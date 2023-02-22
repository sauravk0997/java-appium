package com.disney.qa.disney.android.pages.tv.globalnav;

import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.disney.android.pages.tv.DisneyPlusAndroidTVCommonPage;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusAndroidTVMoviesPageBase extends DisneyPlusAndroidTVCommonPage {

    public DisneyPlusAndroidTVMoviesPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        boolean isPresent = landingPageTextView.isElementPresent(DELAY);
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isPresent;
    }
}

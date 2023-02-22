package com.disney.qa.disney.android.pages.tv.globalnav;

import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.disney.android.pages.tv.DisneyPlusAndroidTVCommonPage;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusAndroidTVOriginalsPageBase extends DisneyPlusAndroidTVCommonPage {

    @FindBy(id = "originalsLogo")
    private ExtendedWebElement originalsLogo;

    public DisneyPlusAndroidTVOriginalsPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        boolean isPresent = originalsLogo.isElementPresent();
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isPresent;
    }
}

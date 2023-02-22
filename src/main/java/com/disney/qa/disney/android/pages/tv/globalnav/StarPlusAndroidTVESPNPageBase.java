package com.disney.qa.disney.android.pages.tv.globalnav;

import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.disney.android.pages.tv.DisneyPlusAndroidTVCommonPage;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class StarPlusAndroidTVESPNPageBase extends DisneyPlusAndroidTVCommonPage {

    @FindBy(id = "heroContainer")
    private ExtendedWebElement espnHero;

    public StarPlusAndroidTVESPNPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        boolean isPresent = espnHero.isElementPresent();
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isPresent;
    }
}

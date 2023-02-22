package com.disney.qa.disney.android.pages.tv.pages;

import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.disney.android.pages.tv.DisneyPlusAndroidTVCommonPage;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusAndroidTVCompletePurchasePageBase extends DisneyPlusAndroidTVCommonPage {

    @FindBy(id = "completePurchaseLogo")
    private ExtendedWebElement completePurchaseLogo;

    public DisneyPlusAndroidTVCompletePurchasePageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        boolean isPresent = completePurchaseLogo.isElementPresent();
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isPresent;
    }
}

package com.disney.qa.disney.android.pages.tv;

import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.disney.android.pages.common.DisneyPlusPaywallPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_TV, parentClass = DisneyPlusPaywallPageBase.class)
public class DisneyPlusAndroidTVPaywallPage extends DisneyPlusPaywallPageBase {

    @FindBy(id = "planSelectContent")
    private ExtendedWebElement planSelectContent;

    @FindBy(id = "interstitialButtonLogOut")
    private ExtendedWebElement logOutPayWall;

    @FindBy(id = "paywallBtnRestore")
    private ExtendedWebElement paywallBtnRestore;

    public DisneyPlusAndroidTVPaywallPage(WebDriver driver) {
        super(driver);
    }

    public void clickOnLogOut() {
        logOutPayWall.click();
    }

    public boolean isCompleteSubscriptionScreenOpened() {
        return logOutPayWall.isElementPresent();
    }

    public boolean isRestartSubscriptionScreenOpen() {
        return paywallBtnRestore.isElementPresent();
    }

    public ExtendedWebElement getPaywallButton() {
        return paywallBtnRestore;
    }

    @Override
    public boolean isOpened() {
        boolean isPresent = planSelectContent.isElementPresent();
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isPresent;
    }
}

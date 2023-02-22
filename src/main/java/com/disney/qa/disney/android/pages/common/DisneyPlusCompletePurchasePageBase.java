package com.disney.qa.disney.android.pages.common;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusCompletePurchasePageBase extends DisneyPlusCommonPageBase{

    @FindBy(id = "interstitialBackgroundImage")
    private ExtendedWebElement paywallBackground;

    @FindBy(id = "interstitialButtonLogOut")
    private ExtendedWebElement logOutBtn;

    @FindBy(id = "interstitialButtonPrimary")
    private ExtendedWebElement completePurchaseBtn;

    @FindBy(id = "interstitialDescriptionSub")
    private ExtendedWebElement subInterstitialDescription;

    @FindBy(id = "interstitialDescriptionMain")
    private ExtendedWebElement mainInterstitialDescription;

    public DisneyPlusCompletePurchasePageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened(){
        return paywallBackground.isElementPresent();
    }

    public boolean isLogoutBtnPresent() {
        return logOutBtn.isElementPresent();
    }

    public void clickLogoutBtn() {
        logOutBtn.click();
    }

    public boolean isSubDescriptionPresent() {
        return subInterstitialDescription.isElementPresent();
    }

    public boolean isMainDescriptionPresent() {
        return mainInterstitialDescription.isElementPresent();
    }

    public boolean isCompletePurchaseBtnPresent() {
        return completePurchaseBtn.isElementPresent();
    }

    public void clickCompletePurchaseBtn() {
        completePurchaseBtn.click();
    }

    public String getDescriptionMainText(){
        return getElementText(mainInterstitialDescription);
    }

    public String getDescriptionSubText(){
        return getElementText(subInterstitialDescription);
    }

    public void proceedWithRestore(){
        try{
            waitUntil(ExpectedConditions.visibilityOfElementLocated(errorDialogMessage.getBy()), 5);
            clickConfirmButton();
        } catch (TimeoutException e){
            LOGGER.info("AUTO RESTORE WAS NOT PROMPTED");
            try {
                waitUntil(ExpectedConditions.visibilityOfElementLocated(completePurchaseBtn.getBy()), 5);
                completePurchaseBtn.click();
            } catch (TimeoutException t){
                LOGGER.info("Complete Purchase screen not displayed. Restoring purchase.");
            }
        }
        initPage(DisneyPlusPaywallPageBase.class).clickRestorePurchaseBtn();
    }
}

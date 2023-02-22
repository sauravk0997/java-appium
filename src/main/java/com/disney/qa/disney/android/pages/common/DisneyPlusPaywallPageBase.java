package com.disney.qa.disney.android.pages.common;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusPaywallPageBase extends DisneyPlusCommonPageBase {

    @FindBy(id = "completePurchaseLogo")
    private ExtendedWebElement paywallLogo;

    @FindBy(id = "paywallBtnRestoreMobile")
    private ExtendedWebElement restorePurchaseBtn;

    @FindBy(id = "paywallTitle")
    private ExtendedWebElement paywallPurchasePageTitle;

    @FindBy(xpath = "//*[@resource-id='com.disney.disneyplus:id/paywallButton']/*/*[contains(@text, \"%s\")]")
    private ExtendedWebElement paywallButton;

    @FindBy(id = "interstitialButtonLogOut")
    private ExtendedWebElement logOutPayWall;

    @FindBy(id = "interstitialContainer")
    private ExtendedWebElement interstitialContainer;

    @FindBy(id = "positiveButton")
    private ExtendedWebElement positiveBtn;

    public DisneyPlusPaywallPageBase(WebDriver driver){
        super(driver);
    }

    @Override
    public boolean isOpened(){
        return paywallPurchasePageTitle.isElementPresent() || interstitialContainer.isElementPresent();
    }

    public void clickRestorePurchaseBtn() {
        restorePurchaseBtn.click();
    }

    public void clickOnPurchaseButton(String monthlyYearly){
        paywallButton.format(monthlyYearly).click();
    }

    public boolean isPaywallErrorDisplayed(){
        return errorDialogMessage.isElementPresent();
    }

    public String getErrorText(){
        return errorDialogMessage.getText();
    }

    public void clickLogout() {
        logOutPayWall.click();
    }

    public void clickCancelBtn() {
        clickActionButton();
    }

    public void clickPositiveBtn() { positiveBtn.click(); }

    public boolean isCompleteSubscriptionScreenOpen() {
        return logOutPayWall.isElementPresent();
    }
}
